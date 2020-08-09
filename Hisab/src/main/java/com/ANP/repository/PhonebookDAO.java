package com.ANP.repository;

import com.ANP.bean.PhonebookBean;
import com.ANP.bean.ProcessedContact;
import com.ANP.bean.RawPhonebookContact;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;


@Repository
public class PhonebookDAO {

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

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

        Map<String, Object> param = new HashMap<String, Object>();
        param.put("orgId", orgId);
        param.put("employeeId", employeeId);
        PhonebookBean phonebookBean = new PhonebookBean();

        phonebookBean.setProcessedContactList(namedParameterJdbcTemplate.query("select phonebook_contact.key, phonebook_contact.value, phonebook_contact.contact_name" +
                " from phonebook_contact,phonebook where " +
                "phonebook.id = phonebook_contact.phonebookid and phonebook.orgId = :orgId and phonebook.employeeid = :employeeId",param,
                new ResultSetExtractor<List<ProcessedContact>>(){
                    public List<ProcessedContact> extractData(ResultSet rs) throws SQLException{
                        Map<String,ProcessedContact> contactMap = new HashMap<>();
                        while(rs.next()) {
                            String contactName = rs.getString("phonebook_contact.contact_name".toUpperCase());
                            String keyForProcessedContact = rs.getString("phonebook_contact.key");
                            String valueForProcessedContact = rs.getString("phonebook_contact.value");

                            RawPhonebookContact rawPhonebookContact = new RawPhonebookContact();
                            rawPhonebookContact.setKey(keyForProcessedContact);
                            rawPhonebookContact.setValue(valueForProcessedContact);

                            if (contactMap.get(contactName) == null) {
                                ProcessedContact processedContact = new ProcessedContact();
                                processedContact.addARawPhonebookContact(rawPhonebookContact);
                                contactMap.put(contactName,processedContact);
                            }
                            else{
                                contactMap.get(contactName).addARawPhonebookContact(rawPhonebookContact);
                            }
                        }
                        List contactList = new ArrayList(contactMap.values());
                        System.out.println(contactList);
                        return contactList;
                    }
        }));
        return phonebookBean;
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
    public void syncPhonebook(long orgId, String employeeId, List<RawPhonebookContact> rawPhonebookContacts) {

    }

    /*
     * Simply read the Phonebook_Contact (however you need to join with Phonebook) table and return a full list using RowMapper
     */
    private List<RawPhonebookContact> listRawContactsForUI(long orgId, String employeeId) {

        Map<String, Object> param = new HashMap<String, Object>();
        param.put("orgId", orgId);
        param.put("employeeId", employeeId);
        return namedParameterJdbcTemplate.query("select phonebook_contact.key, phonebook_contact.value, phonebook_contact.contact_name" +
                " from phonebook_contact,phonebook where phonebook.id = phonebook_contact.phonebookid and" +
                " phonebook.orgId = :orgId and phonebook.employeeid = :employeeId",param, new PhoneBookListingMapper());

    }

    private final class PhoneBookListingMapper implements RowMapper<RawPhonebookContact> {
    public RawPhonebookContact mapRow(ResultSet rs, int rowNum) throws SQLException{
        RawPhonebookContact rawPhonebookContact = new RawPhonebookContact();
        rawPhonebookContact.setContactName(rs.getString("phonebook_contact.contact_name"));
        rawPhonebookContact.setKey(rs.getString("phonebook_contact.key"));
        rawPhonebookContact.setValue(rs.getString("phonebook_contact.value"));
        return rawPhonebookContact;
    }


    }
}
