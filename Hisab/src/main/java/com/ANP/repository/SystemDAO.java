package com.ANP.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.GenericStoredProcedure;
import org.springframework.jdbc.object.StoredProcedure;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Types;

@Repository
public class SystemDAO {

    @Autowired
    DataSource dataSource;

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
}
