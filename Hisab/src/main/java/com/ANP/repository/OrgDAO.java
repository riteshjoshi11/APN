package com.ANP.repository;

import com.ANP.bean.Organization;
import com.ANP.service.OrganizationHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
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

    public int createOrganization(Organization organization) {
        return namedParameterJdbcTemplate.update("insert into organization (orgname,state,city) " +
                "values (:orgName,:state,city)", new BeanPropertySqlParameterSource(organization));
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
            org.setOrgID(rs.getLong("id"));
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

}
