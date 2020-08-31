package com.ANP.repository;

import ch.qos.logback.core.net.SyslogOutputStream;
import com.ANP.bean.EmployeeBean;
import com.ANP.bean.OrgDetails;
import com.ANP.bean.Organization;
import com.ANP.util.CustomAppException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class OrgDAO {

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    /*
        return orgKey
     */
    public long createOrganization(Organization organization, EmployeeBean employeeBean) {
        System.out.println("createOrganization: Organization Bean:" + organization);
        isDuplicate(organization, employeeBean);
        KeyHolder holder = new GeneratedKeyHolder();
        namedParameterJdbcTemplate.update("insert into organization (orgname,state,city,clientid) " +
                "values (:orgName,:state,:city,:clientId)", new BeanPropertySqlParameterSource(organization), holder);
        long generatedOrgKey = holder.getKey().longValue();
        System.out.println("CreateOrganization: Generated Key=" + generatedOrgKey);
        return generatedOrgKey;
    }

    public Organization getOrganization(Long id) {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("id", id);
        List<Organization> organizations = namedParameterJdbcTemplate.query("select * from organization where id = :id",
                param, new OrgMapper());
        return organizations.get(0);
    }

    private static final class OrgMapper implements RowMapper<Organization> {
        public Organization mapRow(ResultSet rs, int rowNum) throws SQLException {
            Organization org = new Organization();
            org.setOrgId(rs.getLong("id"));
            org.setOrgName(rs.getString("orgname"));
            org.setState(rs.getString("state"));
            org.setState(rs.getString("city"));
            org.setClientId(rs.getString("clientid"));
            return org;
        }
    }

    /*
     * This method will return organization(s) for a given mobileNumber
     * It may result multiple organizations.
     */
    public List<Organization> getOrganizationsForMobileNo(String mobileNumber) {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("mobileNumber", mobileNumber);
        List<Organization> organizations = namedParameterJdbcTemplate.query("select * from organization where id IN" +
                " (select orgid from employee where mobile=:mobileNumber or mobile2=:mobileNumber)", param, new OrgMapper());
        return organizations;
    }

    private void isDuplicate(Organization organization, EmployeeBean employeeBean) {
        Map<String, Object> sparam = new HashMap<String, Object>();
        sparam.put("mobile", employeeBean.getMobile());
        sparam.put("orgName", organization.getOrgName());
        sparam.put("city", organization.getCity());

        Integer countRows = namedParameterJdbcTemplate.queryForObject("select count(org.id) as countorg from organization org, employee " +
                " emp where org.id = emp.orgid and emp.mobile = :mobile and " +
                " emp.type = 1 and org.orgname= :orgName and org.city= :city", sparam, Integer.class);
        System.out.println(countRows);
        if (countRows >= 1) {
            throw new CustomAppException("duplicate organization", "SERVER.ORGANIZATION.DUPLICATE_ORGANIZATION", HttpStatus.EXPECTATION_FAILED);
        }
    }

    /*
     * This method will be invoked as part of organization create method
     * and create a bare shell orgDetails
     * The details will be filled up later
     */
    public int createOrganizationDetails(long orgId) {
        Map<String, Object> param = new HashMap<>();
        return namedParameterJdbcTemplate.update("insert into orgdetails(orgid) values (" + orgId + ")", param);
    }

    public void updateOrganizationDetails(OrgDetails orgDetails) {
        int numberOfRecordsUpdates =  namedParameterJdbcTemplate.update("update orgdetails set mobile1 = :mobile1," +
                "email=:email, mobile2=:mobile2, gstnumber = :gstNumber, pannumber = :panNumber," +
                "companytype = :companyTypeInt, businessnature= :businessNatureInt, numberofemployees = :numberOfEmployeesInt," +
                "extradetails = :extraDetails, accountdetails=:accountDetails, billingaddress=:billingAddress," +
                "caname = :cAName, caemail = :cAEmail, camobile = :cAMobile, autoemailgstreport=:autoEmailGSTReport, website=:website " +
                " where orgid = :orgId and id=:orgDetailId", new BeanPropertySqlParameterSource(orgDetails));
        if(numberOfRecordsUpdates!=1) {
            throw new CustomAppException("Organization Details could not be updated for given ID","SERVER.ORGDETAILS.ORGDETAILS_NOT_UPDATED_WITHID", HttpStatus.EXPECTATION_FAILED);
        }
    }

    public OrgDetails getOrgDetails(long orgId) {
        Map<String, Object> param = new HashMap<>();
        param.put("orgId", orgId);
        List<OrgDetails> orgDetailsList = namedParameterJdbcTemplate.query("select orgdet.* " +
             //   " (select bus.`name` from businessnature bus where orgdet.businessnature = bus.id) as busnature," +
             //   " (select noemp.`name` from noofemployee noemp where orgdet.numberofemployees = noemp.id) as noemployee," +
            //    " (select com.`name` from companytype com where orgdet.companytype = com.id) as comptype" +
                " from orgdetails orgdet where orgid=:orgId ", param, new OrgDetailsMapper());

        if(orgDetailsList!=null && orgDetailsList.size()>0) {
            System.out.println(orgDetailsList.size());
            return orgDetailsList.get(0);
        }
        throw new CustomAppException("Organization Details could not be found for given ID","SERVER.ORGDETAILS.ORGDETAILS_NOT_FOUND_WITHID", HttpStatus.EXPECTATION_FAILED);
     }

    private static final class OrgDetailsMapper implements RowMapper<OrgDetails> {
         public OrgDetails mapRow(ResultSet rs, int rowNum) throws SQLException {
            OrgDetails orgDetails = new OrgDetails();
            orgDetails.setOrgDetailId(rs.getLong("orgdet.id"));
            orgDetails.setMobile1(rs.getString("mobile1"));
            orgDetails.setEmail(rs.getString("email"));
            orgDetails.setAccountDetails(rs.getString("accountdetails"));
            orgDetails.setBillingAddress(rs.getString("billingaddress"));
            orgDetails.setcAEmail(rs.getString("caemail"));
            orgDetails.setcAMobile(rs.getString("camobile"));
            orgDetails.setcAName(rs.getString("caname"));
            orgDetails.setExtraDetails(rs.getString("extradetails"));
            orgDetails.setGstNumber(rs.getString("gstnumber"));
            orgDetails.setMobile2(rs.getString("mobile2"));
            orgDetails.setOrgId(rs.getLong("orgid"));
            orgDetails.setPanNumber(rs.getString("pannumber"));
            orgDetails.setWebsite(rs.getString("website"));
            orgDetails.setCompanyTypeInt(rs.getInt("companytype"));
            orgDetails.setBusinessNatureInt(rs.getInt("businessnature"));
            orgDetails.setNumberOfEmployeesInt(rs.getInt("numberofemployees"));
            orgDetails.setAutoEmailGSTReport(rs.getBoolean("autoemailgstreport"));
            return orgDetails;
        }
    }
 }
