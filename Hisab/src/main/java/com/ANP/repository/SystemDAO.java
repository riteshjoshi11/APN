package com.ANP.repository;


import com.ANP.util.ANPUtils;
import org.apache.catalina.Store;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlInOutParameter;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.jdbc.object.GenericStoredProcedure;
import org.springframework.jdbc.object.StoredProcedure;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.sql.Types;
import java.util.*;

@Repository
public class SystemDAO {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    DataSource dataSource;




   @Transactional(rollbackFor = Exception.class)
        public void deleteOrganizationTransaction(long orgId, boolean deleteCompanyData, boolean deleteSalaryData, boolean deleteAuditData ,
                                                  boolean deleteEmployeeSalaryBalance, boolean deleteEmployeeCompanyBalance, boolean deleteCustomerBalance)
        {
            StoredProcedure procedure = new GenericStoredProcedure();
            procedure.setDataSource(dataSource);
            procedure.setSql("DeleteOrganizationTransaction_Procedure");
            procedure.setFunction(false);

            SqlParameter[] declareparameters = {
                    new SqlParameter(Types.INTEGER),
                    new SqlParameter(Types.BOOLEAN),
                    new SqlParameter(Types.BOOLEAN),
                    new SqlParameter(Types.BOOLEAN),
                    new SqlParameter(Types.BOOLEAN),
                    new SqlParameter(Types.BOOLEAN),
                    new SqlParameter(Types.BOOLEAN),
            };
            procedure.setParameters(declareparameters);
            procedure.compile();
            procedure.execute(orgId, deleteCompanyData, deleteSalaryData, deleteAuditData, deleteEmployeeSalaryBalance,
                    deleteEmployeeCompanyBalance, deleteCustomerBalance);
        }

    @Transactional(rollbackFor = Exception.class)
    public void actualDeletion(Map<String,String> commaSeperatedList , int noOfDaysAfterDelete)
    {
        String list = commaSeperatedList.get("archiveandpurgetablelist");

        List<String> result = Arrays.asList(list.split("\\s*,\\s*"));

        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate).withProcedureName("PurgeProcess_Procedure");
        Map<String, Object> inParamMap = new HashMap<String, Object>();

        for(Object o : result)
        {
            if(!o.equals("")) {
                System.out.println("no fo days" + noOfDaysAfterDelete);
                System.out.println("object" + o);
                inParamMap.put("noofdaysafterdeletion", noOfDaysAfterDelete);
                inParamMap.put("purgearchivetablename", o);
                SqlParameterSource in = new MapSqlParameterSource(inParamMap);

                Map<String, Object> simpleJdbcCallResult = simpleJdbcCall.execute(in);
                System.out.println(simpleJdbcCallResult);
            }
        }
    }
}
