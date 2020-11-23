package com.ANP.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;


/*
 * This class will serve a common purpose for DAO layer.
 * If you have common DB access logic, put it here
 */
@Repository
public class CommonDAO {
    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Transactional(rollbackFor = Exception.class)
    public void softDeleteSingleRecord(String tableName, long orgID, long tablePrimaryKey) {
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        mapSqlParameterSource.addValue("orgID", orgID);
        mapSqlParameterSource.addValue("tablePrimaryKey", tablePrimaryKey);
        mapSqlParameterSource.addValue("currentDate", new Date());
        String updateQuery = "update " + tableName + " set  isdeleted = 1, deletedate=:currentDate  where orgid=:orgID and id=:tablePrimaryKey";
        namedParameterJdbcTemplate.update(updateQuery, mapSqlParameterSource);
    }

    /*
        This method gives the company / business registration date
        It will find out the first employee creation date for owner
     */
    public Date getOrgCreationDate(Long orgId) {
        String sql = "select createdate from employee where orgid=? and type=1 limit 1";
        try {
            return namedParameterJdbcTemplate.getJdbcTemplate().queryForObject(sql, new Object[]{orgId}, java.util.Date.class);
        } catch (IncorrectResultSizeDataAccessException e) {
            //ignore the exception i.e it returns no value or more than one
        }
        return null;
    }
}
