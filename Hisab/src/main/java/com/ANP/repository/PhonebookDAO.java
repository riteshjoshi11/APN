package com.ANP.repository;

import com.ANP.bean.*;
import com.ANP.controller.PhonebookController;
import com.ANP.util.CustomAppException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.*;


@Repository
public class PhonebookDAO {

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private static final Logger logger = LoggerFactory.getLogger(PhonebookDAO.class);

    /*
     * Returns PhonebookBean
     * employeeId,lastSyncDate, syncStatus will come from Phonebook table
     * Now - we need to build  List<ProcessedContact>
     * One ProcessedContact - denotes a real phonebook entry in your mobile meaning A Contact Name can have more than one mobile, Email, Website
     * While we do not store the information in the same format in DB, it is on different rows (denormalized form)
     *  ( Nitesh Yadav, Email, nitesh.indore@gmail.com)
     *  ( Nitesh Yadav, Mobile, 9158900354)
     * To build this - take a Map: - Key --> ContactName and Value --> ProcessedContact
     * Run a Query and get the resultSet (not the Mapper) - iterate through each row of resultSet.
     * For Each row: get ContactName and search in the map with ContactName as key
     * if not found then create an object of ProcessContact after getting ContactName, Key, Value from resultSet and put the object into the Map
     * If you found it - call ProcessedContact.addARawPhonebookContact
     * Once all rows are done. just call map.values() to get the Collection object.
     * Set this collection object into the PhonebookBean
     */
    public PhonebookBean listProcessedContactsForUI(long orgId, String employeeId) {
        PhonebookBean phonebookBean = new PhonebookBean();
        Map<String, ProcessedContact> processedContactMap = getContactMap(orgId, employeeId, false);
        phonebookBean.setProcessedContactList(processedContactMap.values());
        phonebookBean.setEmployeeId(employeeId);
        phonebookBean.setOrgId(orgId);
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("orgId", orgId);
        param.put("employeeId", employeeId);
        Map<String,Object> syncMap = namedParameterJdbcTemplate.query ("select sync_status, lastsyncdate from" +
                " phonebook where orgid = :orgId " +
                "and employeeid = :employeeId", param, new ResultSetExtractor<Map<String, Object>>(){
            @Override
            public Map<String, Object> extractData(ResultSet rs) throws SQLException {
                Map<String,Object> mapForPhoneBookBean = new HashMap<>();
                while (rs.next()) {
                    mapForPhoneBookBean.put("sync_status", rs.getString("sync_status"));
                    mapForPhoneBookBean.put("lastsyncdate", rs.getTimestamp("lastsyncdate"));
                }
                return mapForPhoneBookBean;
            }});
        phonebookBean.setSyncStatus((String)syncMap.get("sync_status"));
        phonebookBean.setLastSyncDate((Timestamp)syncMap.get("lastsyncdate"));

        return phonebookBean;
    }

    //ResultSet Extractor for processedContactMap : Map<ContactName,ProcessedContact>
    private static final class ProcessedContactMapper implements ResultSetExtractor<Map<String, ProcessedContact>> {
        public Map<String, ProcessedContact> extractData(ResultSet rs) throws SQLException {
            Map<String, ProcessedContact> contactMap = new HashMap<>();
            while (rs.next()) {
                String contactName = rs.getString("phonebook_contact.contact_name".toUpperCase());
                String keyForProcessedContact = rs.getString("phonebook_contact.key");
                String valueForProcessedContact = rs.getString("phonebook_contact.value");

                RawPhonebookContact rawPhonebookContact = new RawPhonebookContact();
                rawPhonebookContact.setContactName(contactName);
                rawPhonebookContact.setKey(keyForProcessedContact);
                rawPhonebookContact.setValue(valueForProcessedContact);

                if (contactMap.get(contactName) == null) {
                    ProcessedContact processedContact = new ProcessedContact();
                    processedContact.setContactName(contactName);
                    processedContact.addARawPhonebookContact(rawPhonebookContact);
                    contactMap.put(contactName, processedContact);
                } else {
                    contactMap.get(contactName).addARawPhonebookContact(rawPhonebookContact);
                    contactMap.get(contactName).setContactName(contactName);
                }
            }
            return contactMap;
        }
    }

    //Mapper for ProcessedContacts
    public Map<String, ProcessedContact> getContactMap(long orgId, String employeeId, Boolean filterIsDeleted) {
        String sql;
        if (filterIsDeleted = true) {
            sql =   " select phonebook_contact.key, phonebook_contact.value, phonebook_contact.contact_name " +
                    " from phonebook_contact,phonebook where " +
                    " phonebook.id = phonebook_contact.phonebookid and phonebook.orgId = :orgId and " +
                    " phonebook.employeeid = :employeeId and (isdeleted is null or isdeleted<> true) ";
        } else {
            sql =   " select phonebook_contact.key, phonebook_contact.value, phonebook_contact.contact_name " +
                    " from phonebook_contact,phonebook where " +
                    " phonebook.id = phonebook_contact.phonebookid and phonebook.orgId = :orgId " +
                    " and phonebook.employeeid = :employeeId ";

        }
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("orgId", orgId);
        param.put("employeeId", employeeId);

        return (namedParameterJdbcTemplate.query(sql, param,
                new ProcessedContactMapper()));
    }

    /*
     * Get the List RawPhoneBookContacts from the DB for OrgId and EmployeeID Combination
     * Now you have two Lists - one coming from Input and other is from DB. Lets say: inputPhoneBookContactList and dbPhonebookContactList
     * Now, Ultimately we need to get three different Lists 1. New Contacts 2. Changed Contact 3. Deleted Contacts.
     * Instead of getting three list separately - it will be good if we keep building the query to be fired on the db.
     * Now start iterating : inputPhoneBookContactList
     * See if an item from inputPhoneBookContactList exists in dbPhonebookContactList (Check using Contains method of List interface)
     * if contains YES - Check if the value
     */
    @Transactional
    public void syncPhonebook(PhoneBookListingBean phoneBookListingBean) {
        if (phoneBookListingBean.getRawPhonebookContacts() == null || phoneBookListingBean.getRawPhonebookContacts().size() == 0) {
            throw new CustomAppException("INPUT CONTACT LIST IS EMPTY", "SERVER.SYNC_PHONEBOOK.NULLOREMPTY.CONTACT", HttpStatus.BAD_REQUEST);
        }

        Long phonebookId = getId(phoneBookListingBean.getOrgId(), phoneBookListingBean.getEmployeeId());
        Boolean firstTimeCreated = false;
        if (phonebookId == null || phonebookId <= 0) {
            PhonebookBean phonebookBean = new PhonebookBean();

            phonebookBean.setEmployeeId(phoneBookListingBean.getEmployeeId());
            phonebookBean.setOrgId(phoneBookListingBean.getOrgId());
            phonebookBean.setMobileNumber(phoneBookListingBean.getMobileNumber());
            phonebookId = this.createPhoneBookEntry(phonebookBean);
            firstTimeCreated = true;
        }

        Map<String, Object> paramSync = new HashMap<>();
        paramSync.put("orgId", phoneBookListingBean.getOrgId());
        paramSync.put("employeeId", phoneBookListingBean.getEmployeeId());
        paramSync.put("syncStatus",PhonebookBean.SYNC_STATUS_ENUM.Syncing.toString());

        namedParameterJdbcTemplate.update("update phonebook set sync_status = :syncStatus" +
                " where orgId = :orgId and employeeId = :employeeId",paramSync );

        List<RawPhonebookContact> listForCreation = new ArrayList<>();
        List<RawPhonebookContact> listForDeletion = new ArrayList<>();

        //getting processed contact after filtering the deleted contacts.
        Map<String, ProcessedContact> contactMap = getContactMap(phoneBookListingBean.getOrgId(), phoneBookListingBean.getEmployeeId(), true);
        if(!firstTimeCreated) {
            //Identification of create and update scenario
            for (RawPhonebookContact rawPhonebookContact : (phoneBookListingBean.getRawPhonebookContacts())) {

                if (contactMap.get(rawPhonebookContact.getContactName().toUpperCase()) == null) {
                    //no match found that means contact does not exist
                    listForCreation.add(rawPhonebookContact);
                    continue;
                }

                if (contactMap.get(rawPhonebookContact.getContactName().toUpperCase()) != null) {
                    //We are here that means contactName exists
                    ProcessedContact processedContact = contactMap.get(rawPhonebookContact.getContactName().toUpperCase());
                    if (! (processedContact.getRawPhonebookContacts().contains(rawPhonebookContact))) {
                        //We are here means the KEY/VALUE DOES NOT EXISTS FOR THE CONTACT

                        //SO NEED TO CREATE
                        listForCreation.add(rawPhonebookContact);
                        continue;
                    }
                }

                logger.trace("We are here that means Contact might need to be deleted that will be decided in next logic");

            }

            //Identification of delete scenario
            List<RawPhonebookContact> dbPhonebookContactList = listRawContactsFromDB(phoneBookListingBean.getOrgId(), phoneBookListingBean.getEmployeeId());
            for (RawPhonebookContact rawPhonebookContact : dbPhonebookContactList) {
                if (!(phoneBookListingBean.getRawPhonebookContacts()).contains(rawPhonebookContact)) {
                    rawPhonebookContact.setDeleted(true);
                    listForDeletion.add(rawPhonebookContact);
                    //delete batch
                }
            }

            logger.trace("Nitesh deletion data=" + listForDeletion);
            if(!listForDeletion.isEmpty()) {
                deleteBatch(listForDeletion, phonebookId);
            }

            logger.trace("Nitesh Creation data=" + listForCreation);
            if(!listForCreation.isEmpty()) {
                createBatch(listForCreation, phonebookId);
            }
        } else  {
            System.out.print("The first time contact sync scenario");
            if(!(phoneBookListingBean.getRawPhonebookContacts()).isEmpty()) {
                createBatch(phoneBookListingBean.getRawPhonebookContacts(), phonebookId);
            }
        }

        //updating sync method

        //Map<String, Object> param = new HashMap<>();
        //param.put("orgId", orgId);
        //param.put("employeeId", employeeId);
        paramSync.put("syncStatus",PhonebookBean.SYNC_STATUS_ENUM.Fully_Synced.toString());
        namedParameterJdbcTemplate.update("update phonebook set lastsyncdate = now(), sync_status = :syncStatus" +
                " where orgId = :orgId and employeeId = :employeeId",paramSync );
    }

    /*
     * Simply read the Phonebook_Contact (however you need to join with Phonebook) table and return a full list using RowMapper
     */
    private List<RawPhonebookContact> listRawContactsFromDB(long orgId, String employeeId) {

        Map<String, Object> param = new HashMap<String, Object>();
        param.put("orgId", orgId);
        param.put("employeeId", employeeId);
        return namedParameterJdbcTemplate.query("select phonebook_contact.key, phonebook_contact.value, phonebook_contact.contact_name" +
                " from phonebook_contact,phonebook where phonebook.id = phonebook_contact.phonebookid and" +
                " phonebook.orgId = :orgId and phonebook.employeeid = :employeeId and (phonebook_contact.isdeleted=false or phonebook_contact.isdeleted is null) ", param, new PhoneBookListingMapper());

    }

    private static final class PhoneBookListingMapper implements RowMapper<RawPhonebookContact> {
        public RawPhonebookContact mapRow(ResultSet rs, int rowNum) throws SQLException {
            RawPhonebookContact rawPhonebookContact = new RawPhonebookContact();
            rawPhonebookContact.setContactName(rs.getString("phonebook_contact.contact_name"));
            rawPhonebookContact.setKey(rs.getString("phonebook_contact.key"));
            rawPhonebookContact.setValue(rs.getString("phonebook_contact.value"));
            return rawPhonebookContact;
        }
    }


    private void createBatch(List<RawPhonebookContact> rawPhonebookContactList, long phonebookId) {
        String sql = "insert into phonebook_contact(`contact_name`,`key`,`value`,`phonebookid`) values(?,?,?,?) ";
        namedParameterJdbcTemplate.getJdbcTemplate().batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i)
                    throws SQLException {
                ps.setString(1, rawPhonebookContactList.get(i).getContactName().trim().toUpperCase());
                ps.setString(2, rawPhonebookContactList.get(i).getKey().trim().toUpperCase());
                ps.setString(3, rawPhonebookContactList.get(i).getValue().trim().toUpperCase());
                ps.setLong(4, phonebookId);
            }

            @Override
            public int getBatchSize() {
                return (rawPhonebookContactList.size());
            }
        });
    }


    private void deleteBatch(List<RawPhonebookContact> rawPhonebookContactList, long phonebookId) {
        String sql = "update `phonebook_contact` set `isdeleted` = true where `contact_name` = ? and `phonebookid` = ? and `key` = ? and `value` = ?";
        namedParameterJdbcTemplate.getJdbcTemplate().batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i)
                    throws SQLException {
                ps.setString(1, rawPhonebookContactList.get(i).getContactName());
                ps.setLong(2, phonebookId);
                ps.setString(3, rawPhonebookContactList.get(i).getKey());
                ps.setString(4, rawPhonebookContactList.get(i).getValue());
            }

            @Override
            public int getBatchSize() {
                return (rawPhonebookContactList.size());
            }
        });
    }

    private Long getId(long orgId, String employeeId) {
        Map<String, Object> param = new HashMap<>();
        param.put("orgid", orgId);
        param.put("employeeid", employeeId);
        try {
            return namedParameterJdbcTemplate.queryForObject("select id from phonebook where orgid = :orgid " +
                    " and employeeId = :employeeid", param, Long.class);
        } catch (org.springframework.dao.EmptyResultDataAccessException e) {
            //ignore exception
        }
        return null;
    }

    /*
     * create records into the master table for Phonebook
     */
    private Long createPhoneBookEntry(PhonebookBean phonebookBean) {
        KeyHolder holder = new GeneratedKeyHolder();
        namedParameterJdbcTemplate.update("insert into phonebook (orgid,employeeid,sync_status) " +
                "values (:orgId,:employeeId,'"+ PhonebookBean.SYNC_STATUS_ENUM.Syncing.toString() +"')",
                new BeanPropertySqlParameterSource(phonebookBean), holder);
        long generatedOrgKey = holder.getKey().longValue();
        logger.trace("createPhoneBookEntry: Generated Key=" + generatedOrgKey);
        return generatedOrgKey;
    }
}
