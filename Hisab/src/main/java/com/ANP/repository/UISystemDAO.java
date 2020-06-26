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
/*
    This class is mainly used by the Controller/UI to soft delete organization data
 */
public class UISystemDAO {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    DataSource dataSource;

   @Transactional(rollbackFor = Exception.class)
        public void softDeleteOrganizationTransaction(long orgId, boolean deleteCompanyData, boolean deleteSalaryData, boolean deleteAuditData ,
                                                      boolean deleteEmployeeSalaryBalance, boolean deleteEmployeeCompanyBalance, boolean deleteCustomerBalance)
        {
            StoredProcedure procedure = new GenericStoredProcedure();
            procedure.setDataSource(dataSource);
            procedure.setSql("SoftDeleteOrgData_Procedure");
            procedure.setFunction(false);

            SqlParameter[] declareparameters = {
                    new SqlParameter("ParamOrgId",Types.INTEGER),
                    new SqlParameter("deleteCompanyData",Types.BOOLEAN),
                    new SqlParameter("deleteSalaryData",Types.BOOLEAN),
                    new SqlParameter("deleteAuditData",Types.BOOLEAN),
                    new SqlParameter("deleteEmployeeSalaryBalance",Types.BOOLEAN),
                    new SqlParameter("deleteEmployeeCompanyBalance",Types.BOOLEAN),
                    new SqlParameter("deleteCustomerBalance",Types.BOOLEAN),
            };
            procedure.setParameters(declareparameters);
            procedure.compile();
            procedure.execute(orgId, deleteCompanyData, deleteSalaryData, deleteAuditData, deleteEmployeeSalaryBalance,
                    deleteEmployeeCompanyBalance, deleteCustomerBalance);
        }


}
