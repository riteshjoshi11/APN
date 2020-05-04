package com.ANP.controller;

import com.ANP.bean.DashboardBean;
import com.ANP.repository.DashboardDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/dashboard")
public class DashboardController {

    @Autowired
    DashboardDAO dashboardDAO ;

    @PostMapping(path = "/buildDashboard", produces = "application/json")
    public DashboardBean buildDashboard(@RequestBody long orgId) {
        return dashboardDAO.prepareDashBoard(orgId);
    }
}
