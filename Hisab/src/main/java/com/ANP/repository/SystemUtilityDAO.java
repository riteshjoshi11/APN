package com.ANP.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
@Repository
public class SystemUtilityDAO {

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    SystemDAO systemDAO;

    public int insertDeletedTablesInSystem(String listType)
    {
        Map<String, Object> param = new HashMap<>();
        return namedParameterJdbcTemplate.update("update systemconfigurations set `value` = concat(ifnull(value,''),',"+ listType +"')" +
                " where `key` = 'archiveandpurgetablelist' ", param);
    }

    public Map<String,String> getDeletedTablesListInMap()
    {
        Map<String, Object> param = new HashMap<>();
        Map<String,String> purgeTableList = new HashMap<>();
        String commaSeperatedList = namedParameterJdbcTemplate.queryForObject("select value from systemconfigurations where `key` = 'ArchiveAndPurgeTableList'",param, String.class);
        purgeTableList.put("archiveandpurgetablelist",commaSeperatedList);

        //systemDAO.actualDeletion(purgeTableList,15);
        return purgeTableList;

    }
}
