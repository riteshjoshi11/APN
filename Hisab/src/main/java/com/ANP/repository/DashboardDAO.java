package com.ANP.repository;

import com.ANP.bean.CalculationTrackerBean;
import com.ANP.bean.DashboardBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.GenericStoredProcedure;
import org.springframework.jdbc.object.StoredProcedure;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.Types;
import java.util.Map;

@Repository
public class DashboardDAO {
    @Autowired
    CalculationTrackerDAO calculationTrackerDAO ;

    @Autowired
    AccountDAO accountDAO ;

    @Autowired
    DataSource dataSource;

    public DashboardBean prepareDashBoard(long orgId, String employeeID) {
        System.out.println(orgId);
        DashboardBean dashboardBean = new DashboardBean();

        BigDecimal cashWithYou = accountDAO.getCashWithYou(employeeID, orgId) ;
        if(cashWithYou!=null) {
            dashboardBean.setCashWithYou(cashWithYou);
        }
        StoredProcedure procedure = new GenericStoredProcedure();
        procedure.setDataSource(dataSource);
        procedure.setSql("CustomerVendorBalanceTracking_Procedure");
        procedure.setFunction(false);

        SqlParameter[] declareparameters = {
                new SqlParameter(Types.INTEGER),
                new SqlOutParameter("positiveBalance", Types.DECIMAL),
                new SqlOutParameter("negativeBalance", Types.DECIMAL),
                new SqlOutParameter("totalBalance", Types.DECIMAL),
        };

        procedure.setParameters(declareparameters);
        procedure.compile();
        Map<String, Object> result = procedure.execute(orgId);
        System.out.println("Status " + result);
        System.out.println("Status  " + result.get("positiveBalance"));

        dashboardBean.setNeedToPay((BigDecimal) result.get("positiveBalance"));
        dashboardBean.setNeedToCollect((BigDecimal)result.get("negativeBalance"));


        StoredProcedure procedure1 = new GenericStoredProcedure();
        procedure1.setDataSource(dataSource);
        procedure1.setSql("TotalCashInHand_Procedure");
        procedure1.setFunction(false);

        SqlParameter[] declareparameters1 = {
                new SqlParameter(Types.INTEGER),
                new SqlOutParameter("totalCashInHand", Types.DECIMAL),
        };
        procedure1.setParameters(declareparameters1);
        procedure1.compile();
        Map<String, Object> result1 = procedure1.execute(orgId);
        System.out.println("Status tot cash in hand " + result1.get("totalCashInHand"));
        dashboardBean.setTotalCashInHand((BigDecimal)result1.get("totalCashInHand"));


        StoredProcedure procedure2 = new GenericStoredProcedure();
        procedure2.setDataSource(dataSource);
        procedure2.setSql("EmployeeSalaryDueTotal_Procedure");
        procedure2.setFunction(false);

        SqlParameter[] declareparameters2 = {
                new SqlParameter(Types.INTEGER),
                new SqlOutParameter("totalSalaryDue", Types.DECIMAL),
        };
        procedure2.setParameters(declareparameters2);
        procedure2.compile();
        Map<String, Object> result2 = procedure2.execute(orgId);
        System.out.println("Status tot salary due  " + result2.get("totalSalaryDue"));
        dashboardBean.setTotalSalaryDue((BigDecimal)result2.get("totalSalaryDue"));


        //build the dashboard bean
        // -----------------------
        // call StoredProcedure: CustomerVendorBalanceTracking_Procedure -- You need to pass orgID
        //Return @positiveBalance, @negativeBalance, @totalBalance
        //This is how you have to map output into dashboardBean:
        //@positiveBalance:[dashboardBean:needToPay]
        //@negativeBalance:[dashboardBean:needToCollect]
        //@totalBalance - Not in Use currently
        // -----------------------

        // call StoredProcedure: TotalCashInHand_Procedure -- You need to pass orgID
        //Return @totalCashInHand
        //This is how you have to map output into dashboardBean:
        //@totalCashInHand: [dashboardBean:totalCashInHand]

        // call StoredProcedure: EmployeeSalaryDueTotal_Procedure -- You need to pass orgID
        //Return @totalSalaryDue
        //This is how you have to map output into dashboardBean:
        //@totalSalaryDue: [dashboardBean:totalSalaryDue]

        CalculationTrackerBean calculationTrackerBean = calculationTrackerDAO.getCalculationTracker(orgId);
        if(calculationTrackerBean!=null) {
            dashboardBean.setPaidExpense(calculationTrackerBean.getPaidExpense());
            dashboardBean.setUnpaidExpense(calculationTrackerBean.getUnPaidExpense());
            dashboardBean.setTotalExpense(calculationTrackerBean.getTotalExpense());
        }
        return dashboardBean;
    }
}
