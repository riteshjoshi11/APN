package com.ANP.controller;

import com.ANP.bean.DashboardBean;
import com.ANP.repository.DashboardDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/dashboard")
public class DashboardController {

    @Autowired
    DashboardDAO dashboardDAO ;

    @PostMapping(path = "/buildDashboard", produces = "application/json")
    //Please ask for logged in Employee ID as well to give CashInHand value in the dashboard
    public DashboardBean buildDashboard(@RequestParam long orgId, String employeeID) {
        return dashboardDAO.prepareDashBoard(orgId,employeeID);
    }
}
