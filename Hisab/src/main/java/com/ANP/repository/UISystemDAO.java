package com.ANP.repository;


import com.ANP.util.ANPUtils;
import com.ANP.util.CustomAppException;

import org.apache.catalina.Store;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

import static com.ANP.util.ANPConstants.SYSTEM_AUDIT_CUSTOMER;
import static com.ANP.util.ANPConstants.SYSTEM_AUDIT_EMPLOYEE;

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
                    new SqlParameter("DeleteCompanyData",Types.BOOLEAN),
                    new SqlParameter("DeleteSalaryData",Types.BOOLEAN),
                    new SqlParameter("DeleteEmployeeSalaryBalance",Types.BOOLEAN),
                    new SqlParameter("DeleteEmployeeCompanyBalance",Types.BOOLEAN),
                    new SqlParameter("DeleteCustomerBalance",Types.BOOLEAN),
            };
            procedure.setParameters(declareparameters);
            procedure.compile();
            procedure.execute(orgId, deleteCompanyData, deleteSalaryData, deleteAuditData, deleteEmployeeSalaryBalance,
                    deleteEmployeeCompanyBalance, deleteCustomerBalance);
        }

        public void softDeleteAuditData(long orgId, long recordNo, String identifier, boolean deleteAll)
        {
            if(ANPUtils.isNullOrEmpty(identifier)){
                throw new CustomAppException("identifier cannot be null","SERVER.SOFT_DELETE_AUDIT.NOTAVAILABLE", HttpStatus.EXPECTATION_FAILED);
            }
            else if(identifier.equals("customer")||identifier.equals("CUSTOMER")) {
                identifier = SYSTEM_AUDIT_CUSTOMER;
            }
            else if(identifier.equals("employee")||identifier.equals("EMPLOYEE")){
                identifier = SYSTEM_AUDIT_EMPLOYEE;
            }
            else {
                throw new CustomAppException("supply right value for identifier","SERVER.SOFT_DELETE_AUDIT.NOTAVAILABLE", HttpStatus.EXPECTATION_FAILED);
            }
            StoredProcedure storedProcedure = new GenericStoredProcedure();
            storedProcedure.setDataSource(dataSource);
            storedProcedure.setSql("SoftDeleteAuditData_Procedure");
            storedProcedure.setFunction(false);
            System.out.println(identifier);
            SqlParameter[] declareparameters = {
                    new SqlParameter("ParamOrgId",Types.INTEGER),
                    new SqlParameter("RecordNo",Types.INTEGER),
                    new SqlParameter("Identifier",Types.VARCHAR),
                    new SqlParameter("DeleteAll",Types.BOOLEAN),
            };
            storedProcedure.setParameters(declareparameters);
            storedProcedure.compile();
            storedProcedure.execute(orgId,recordNo,identifier,deleteAll);
        }


}
