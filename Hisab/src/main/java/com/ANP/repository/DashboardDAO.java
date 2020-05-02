package com.ANP.repository;

import com.ANP.bean.CalculationTrackerBean;
import com.ANP.bean.DashboardBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class DashboardDAO {

    @Autowired
    NamedParameterJdbcTemplate namedParameterJdbcTemplate ;

    @Autowired
    CalculationTrackerDAO calculationTrackerDAO ;

    public DashboardBean prepareDashBoard(long orgId) {
        DashboardBean dashboardBean = new DashboardBean() ;

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
        dashboardBean.setPaidExpense(calculationTrackerBean.getPaidExpense());
        dashboardBean.setUnpaidExpense(calculationTrackerBean.getUnPaidExpense());
        dashboardBean.setTotalExpense(calculationTrackerBean.getTotalExpense());

        return dashboardBean;
    }
}
