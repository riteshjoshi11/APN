package com.ANP.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Date;


/*
 * This class will serve a common purpose for DAO layer.
 * If you have common DB access logic, put it here
 */
@Repository
public class CommonDAO {
    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public void softDeleteSingleRecord(String tableName, long orgID, long tablePrimaryKey) {
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        mapSqlParameterSource.addValue("orgID", orgID);
        mapSqlParameterSource.addValue("tablePrimaryKey", tablePrimaryKey);
        mapSqlParameterSource.addValue("currentDate", new Date());
        String updateQuery = "update " + tableName +  " set  isdeleted = 1, deletedate=:currentDate  where orgid=:orgID and id=:tablePrimaryKey" ;
        namedParameterJdbcTemplate.update(updateQuery, mapSqlParameterSource) ;
    }
}
