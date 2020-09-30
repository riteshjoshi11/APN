package com.ANP.repository;


import com.ANP.bean.SystemBean;
import com.ANP.bean.UIItem;
import com.ANP.util.ANPConstants;
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
    public void softDeleteOrganizationTransaction(SystemBean systemBean)
    {

        boolean retainGSTTransaction = false;
        boolean retainStaffSalaryTransaction = false;
        boolean RetainStaffAccountBalance = false;
        boolean RetainCustomerBalance = false;
        boolean RetainStaffSalaryBalance = false;

        for(UIItem uiItem : systemBean.getSelectedDeleteOptions()) {
            if (!ANPUtils.isNullOrEmpty(uiItem.getUiItemCode()) && uiItem.getUiItemCode().equalsIgnoreCase("1")) {
                retainGSTTransaction = true;
            }
            else if (!ANPUtils.isNullOrEmpty(uiItem.getUiItemCode()) && uiItem.getUiItemCode().equalsIgnoreCase("2")) {
                retainStaffSalaryTransaction = true;
            }
            else if (!ANPUtils.isNullOrEmpty(uiItem.getUiItemCode()) && uiItem.getUiItemCode().equalsIgnoreCase("3")) {
                RetainStaffAccountBalance = true;
            }
            else if (!ANPUtils.isNullOrEmpty(uiItem.getUiItemCode()) && uiItem.getUiItemCode().equalsIgnoreCase("4")) {
                RetainCustomerBalance = true;
            }
            else if (!ANPUtils.isNullOrEmpty(uiItem.getUiItemCode()) && uiItem.getUiItemCode().equalsIgnoreCase("5")) {
                RetainStaffSalaryBalance = true;
            }
        }

        StoredProcedure procedure = new GenericStoredProcedure();
        procedure.setDataSource(dataSource);
        procedure.setSql("SoftDeleteOrgData_Procedure");
        procedure.setFunction(false);

        SqlParameter[] declareparameters = {
                new SqlParameter("RetainGSTTransaction",Types.BOOLEAN),
                new SqlParameter("RetainStaffSalaryTransaction",Types.BOOLEAN),
                new SqlParameter("RetainStaffAccountBalance",Types.BOOLEAN),
                new SqlParameter("RetainCustomerBalance",Types.BOOLEAN),
                new SqlParameter("RetainStaffSalaryBalance",Types.BOOLEAN),
                new SqlParameter("ParamOrgId",Types.INTEGER),
        };
        procedure.setParameters(declareparameters);
        procedure.compile();
        procedure.execute(retainGSTTransaction,retainStaffSalaryTransaction,RetainStaffAccountBalance,
                RetainCustomerBalance,RetainStaffSalaryBalance,systemBean.getOrgID());
    }

    public void softDeleteAuditData(long orgId, long recordNo, String identifier, boolean deleteAll) {

        if(ANPUtils.isNullOrEmpty(identifier)){
            throw new CustomAppException("identifier cannot be null","SERVER.SOFT_DELETE_AUDIT.NOTAVAILABLE", HttpStatus.EXPECTATION_FAILED);
        }

        if( !(identifier.equalsIgnoreCase (SYSTEM_AUDIT_CUSTOMER) || identifier.equalsIgnoreCase(SYSTEM_AUDIT_EMPLOYEE)) ) {
            throw new CustomAppException("supply right value for identifier","SERVER.SOFT_DELETE_AUDIT.IDENTFIER_NOT_CORRECT", HttpStatus.EXPECTATION_FAILED);
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
        storedProcedure.execute(orgId,recordNo,identifier.toUpperCase(),deleteAll);
    }


}
