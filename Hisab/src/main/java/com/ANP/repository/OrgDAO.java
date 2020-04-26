package com.ANP.repository;

import com.ANP.bean.AccountBean;
import com.ANP.bean.Customer;
import com.ANP.bean.Organization;
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

    //TODO Create org
    public boolean createOrg() {
//        boolean result=false;
//       int accountCreated=  namedParameterJdbcTemplate.update(
//                "insert into customer (ownerid,accountnickname,type,details,currentbalance,lastbalance,orgid,createdbyid) values(:ownerid,:accountnickname,:type,:details,:currentbalance,:lastbalance,:orgid,:userID)",
//                new BeanPropertySqlParameterSource(accountBean));
//            if(accountCreated>0){
//                result=true;
//            }
        return false;
    }

    public Organization getOrganization(Long id) {

        Map<String, Object> param = new HashMap<String, Object>();

        param.put("id", id);


        List<Organization> organizations = namedParameterJdbcTemplate.query(
                "select * from organization where id = :id",
                param, new OrgMapper());
        return organizations.get(0);
    }

    private static final class OrgMapper implements RowMapper<Organization> {
        public Organization mapRow(ResultSet rs, int rowNum) throws SQLException {
            Organization org = new Organization();
            org.setId(rs.getInt("id"));
            org.setOrgname(rs.getString("orgname"));
            org.setCompanyid(rs.getString("companyid"));
            return org;
        }
    }


}
