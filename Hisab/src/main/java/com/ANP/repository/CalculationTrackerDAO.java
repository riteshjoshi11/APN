package com.ANP.repository;

import com.ANP.bean.CalculationTrackerBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class CalculationTrackerDAO {

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public CalculationTrackerBean getCalculationTracker(long orgId) {
        List<CalculationTrackerBean> trackerBeanList =  namedParameterJdbcTemplate.query(
                "select * from calculationtracker where orgid=" + orgId,
                new BeanPropertySqlParameterSource(new CalculationTrackerBean()), new CalculationMapper());

        CalculationTrackerBean trackerBean = null ;
        if(trackerBeanList!=null && trackerBeanList.size()>0) {
            trackerBean = trackerBeanList.get(1);
        }
        return trackerBean ;
    }

    private static final class CalculationMapper implements RowMapper<CalculationTrackerBean> {
        public CalculationTrackerBean mapRow(ResultSet rs, int rowNum) throws SQLException {
            CalculationTrackerBean calculationTrackerBean = new CalculationTrackerBean();
            calculationTrackerBean.setPaidExpense(rs.getFloat("paidexpense"));
            calculationTrackerBean.setUnPaidExpense(rs.getFloat("unpaidexpense"));
            calculationTrackerBean.setTotalExpense(rs.getFloat("totalexpense"));
            return calculationTrackerBean;
        }
    }

}
