package com.ANP.repository;

import com.ANP.bean.CalculationTrackerBean;
import com.ANP.bean.Expense;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class CalculationTrackerDAO {

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    /*
    * This method will only be invoked once during create organization
     */
    @Transactional(rollbackFor = Exception.class)
    public int createCalculationTracker(long orgID) {
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        mapSqlParameterSource.addValue("orgID", orgID);
        return namedParameterJdbcTemplate.update("INSERT INTO calculationtracker(orgId) VALUES(:orgID);", mapSqlParameterSource);
    }

    //Add the balance amount onto the existing "paid" balance
    //Operations: (ADD,SUBTRACT)
    @Transactional(rollbackFor = Exception.class)
    public int updatePaidExpenseBalance(long orgID, BigDecimal balance, String operation) {
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        mapSqlParameterSource.addValue("orgID", orgID);
        mapSqlParameterSource.addValue("balance", balance);
        if("ADD".equalsIgnoreCase(operation)) {
            return namedParameterJdbcTemplate.update("update calculationtracker set " +
                    " paidexpense = paidexpense + :balance where orgid=:orgID", mapSqlParameterSource);
        } else {
            return namedParameterJdbcTemplate.update("update calculationtracker set " +
                    " paidexpense = paidexpense - :balance where orgid=:orgID", mapSqlParameterSource);
        }
    }

    //Add the balance amount onto the existing "unpaid" balance
    //Operations: (ADD,SUBTRACT)
    @Transactional(rollbackFor = Exception.class)
    public int updateUnPaidExpenseBalance(long orgID, BigDecimal balance,String operation) {
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        mapSqlParameterSource.addValue("orgID", orgID);
        mapSqlParameterSource.addValue("balance", balance);
        if("ADD".equalsIgnoreCase(operation)) {
            return namedParameterJdbcTemplate.update("update calculationtracker set " +
                    " unpaidexpense = unpaidexpense + :balance where orgid=:orgID", mapSqlParameterSource);
        } else {
            return namedParameterJdbcTemplate.update("update calculationtracker set " +
                    " unpaidexpense = unpaidexpense - :balance where orgid=:orgID", mapSqlParameterSource);
        }
    }

    //Add the balance amount onto the existing "unpaid" balance
    @Transactional(rollbackFor = Exception.class)
    public int makeUnpaidToPaid(long orgID, BigDecimal balance) {
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        mapSqlParameterSource.addValue("orgID", orgID);
        mapSqlParameterSource.addValue("balance", balance);

        return namedParameterJdbcTemplate.update("update calculationtracker set " +
                " unpaidexpense = unpaidexpense + :balance where orgid=:orgID", mapSqlParameterSource);
    }


    public CalculationTrackerBean getCalculationTracker(long orgId) {
        List<CalculationTrackerBean> trackerBeanList =  namedParameterJdbcTemplate.query(
                "select * from calculationtracker where orgid=" + orgId,
                new BeanPropertySqlParameterSource(new CalculationTrackerBean()), new CalculationMapper());

        CalculationTrackerBean trackerBean = null ;
        if(trackerBeanList!=null && trackerBeanList.size()>0) {
            trackerBean = trackerBeanList.get(0);
        }
        return trackerBean ;
    }

    private static final class CalculationMapper implements RowMapper<CalculationTrackerBean> {
        public CalculationTrackerBean mapRow(ResultSet rs, int rowNum) throws SQLException {
            CalculationTrackerBean calculationTrackerBean = new CalculationTrackerBean();
            calculationTrackerBean.setPaidExpense(rs.getBigDecimal("paidexpense"));
            calculationTrackerBean.setUnPaidExpense(rs.getBigDecimal("unpaidexpense"));
            calculationTrackerBean.setTotalExpense(rs.getBigDecimal("totalexpense"));
            return calculationTrackerBean;
        }
    }
}
