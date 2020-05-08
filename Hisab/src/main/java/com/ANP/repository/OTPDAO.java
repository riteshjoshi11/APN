package com.ANP.repository;

import com.ANP.bean.CustomerBean;
import com.ANP.bean.OTPBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

@Repository
public class OTPDAO {

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public int createOTP(OTPBean otpBean) {
        //Delete old OTP
        namedParameterJdbcTemplate.update("delete from otphandler where mobileNumber=:mobilenumber" +
                "values(:mobileNumber)", new BeanPropertySqlParameterSource(otpBean));
       //Add NEW OTP
        return namedParameterJdbcTemplate.update("insert into otphandler(mobileNumber,otp) " +
                "values (:mobileNumber,:otp)", new BeanPropertySqlParameterSource(otpBean));
    }

    public boolean validateOTP(OTPBean otpBean) {
        boolean otpValid=false;
        List<OTPBean> otpBeanList = namedParameterJdbcTemplate.query(
                "select generatedtime from otphandler where mobileNumber=:mobileNumber "
                        + " and otp=:otp",new BeanPropertySqlParameterSource(otpBean), new OTPDAO.OTPMapper());
        if(otpBeanList!=null && otpBeanList.size()>0) {
            java.util.Date generatedTimeFromDB =  otpBeanList.get(1).getTimeGenerated();
            //TODO for Paras: compare generatedTimeFromDB is less than 10 min from the current system timestamp
            //If yes then set true else false
        }
        return otpValid;
    }

    private static final class OTPMapper implements RowMapper<OTPBean> {
        public OTPBean mapRow(ResultSet rs, int rowNum) throws SQLException {
            OTPBean otpBean = new OTPBean();
            otpBean.setTimeGenerated(rs.getTimestamp("generatedtime"));
            return otpBean;
        }
    }

}
