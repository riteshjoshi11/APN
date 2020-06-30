package com.ANP.repository;

import ch.qos.logback.core.net.SyslogOutputStream;
import com.ANP.bean.EmployeeBean;
import com.ANP.bean.OrgDetails;
import com.ANP.bean.Organization;
import com.ANP.service.OrganizationHandler;
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
        isDuplicate(organization,employeeBean);
        KeyHolder holder = new GeneratedKeyHolder();
        namedParameterJdbcTemplate.update("insert into organization (orgname,state,city) " +
                "values (:orgName,:state,:city)", new BeanPropertySqlParameterSource(organization), holder);
        long generatedOrgKey = holder.getKey().longValue();
        System.out.println("CreateOrganization: Generated Key=" + generatedOrgKey);
        return generatedOrgKey ;
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
                " (select orgid from employee where mobile=:mobileNumber)", param, new OrgMapper());
        return organizations;
    }

    private void isDuplicate(Organization organization, EmployeeBean employeeBean) {
        Map<String, Object> sparam = new HashMap<String, Object>();
        sparam.put("mobile", employeeBean.getMobile());
        sparam.put("orgName", organization.getOrgName());
        sparam.put("city", organization.getCity());

        Integer countRows  = namedParameterJdbcTemplate.queryForObject("select count(org.id) as countorg from organization org, employee " +
                " emp where org.id = emp.orgid and emp.mobile = :mobile and " +
                " emp.type = 1 and org.orgname= :orgName and org.city= :city",sparam, Integer.class);
        System.out.println(countRows);
        if(countRows>=1){
            throw new CustomAppException("duplicate organization","SERVER.ORGANIZATION.DUPLICATE_ORGANIZATION", HttpStatus.EXPECTATION_FAILED);
        }
    }

    public int createOrganizationDetails(long orgId) {
            Map<String, Object> param = new HashMap<>();
            return namedParameterJdbcTemplate.update("insert into orgdetails(orgid) values (" + orgId + ")", param);
    }

    public int updateOrganizationDetails(OrgDetails orgDetails)
    {
        Map<String,Object> param = new HashMap<>();
       // Map param = new HashMap<String, Object>();
        param.put("mobile1",orgDetails.getMobile1());
        param.put("email",orgDetails.getEmail());
        param.put("mobile2",orgDetails.getMobile2());
        param.put("gstNumber",orgDetails.getGstNumber());
        param.put("panNumber",orgDetails.getPanNumber());
        param.put("companyType",orgDetails.getCompanyType());
        param.put("businessNature",orgDetails.getBusinessNature());
        param.put("numberofemployees",orgDetails.getNumberOfEmployees());
        param.put("extraDetails",orgDetails.getExtraDetails());
        param.put("accountDetails",orgDetails.getAccountDetails());
        param.put("billingAddress",orgDetails.getBillinAddress());
        param.put("cAName",orgDetails.getcAName());
        param.put("cAMobile",orgDetails.getcAMobile());
        param.put("cAEmail",orgDetails.getcAEmail());
        param.put("orgId",orgDetails.getOrgId());
        return namedParameterJdbcTemplate.update("update orgdetails set mobile1 = :mobile1," +
                "email=:email, mobile2=:mobile2, gstnumber = :gstNumber, pannumber = :panNumber," +
                "companytype = :companyType, businessnature= :businessNature, numberofemployees = :numberofemployees," +
                "extradetails = :extraDetails, accountdetails=:accountDetails, billingaddress=:billingAddress," +
                "caname = :cAName, caemail = :cAEmail, camobile = :cAMobile where orgid = :orgId",param);
    }

    public List<OrgDetails> getOrgDetails(OrgDetails orgDetails){

        Map<String,Object> param = new HashMap<>();
        param.put("orgId",orgDetails.getOrgId());
        return namedParameterJdbcTemplate.query("select * " +
                        " from orgdetails where orgid=:orgID ", param, new OrgDetailsMapper()) ;

    }

    private static final class OrgDetailsMapper implements RowMapper<OrgDetails> {
        public OrgDetails mapRow(ResultSet rs, int rowNum) throws SQLException {
            OrgDetails orgDetails = new OrgDetails();
            orgDetails.setMobile1(rs.getString("mobile1"));
            orgDetails.setEmail(rs.getString("email"));
            orgDetails.setAccountDetails(rs.getString("accountdetails"));
            orgDetails.setBillinAddress(rs.getString("billingaddress"));
            orgDetails.setBusinessNature(rs.getString("businessnature"));
            orgDetails.setcAEmail(rs.getString("caemail"));
            orgDetails.setcAMobile(rs.getString("camobile"));
            orgDetails.setcAName(rs.getString("caname"));
            orgDetails.setCompanyType(rs.getString("companytype"));
            orgDetails.setExtraDetails(rs.getString("extradetails"));
            orgDetails.setGstNumber(rs.getString("gstnumber"));
            orgDetails.setNumberOfEmployees(rs.getString("numberofemployees"));
            orgDetails.setMobile2(rs.getString("mobile2"));
            orgDetails.setOrgId(rs.getLong("orgid"));
            orgDetails.setPanNumber(rs.getString("pannumber"));
            return orgDetails;
        }
    }
}
