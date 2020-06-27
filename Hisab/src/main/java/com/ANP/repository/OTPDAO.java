package com.ANP.repository;

import com.ANP.bean.OTPBean;
import com.ANP.util.CustomAppException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.validation.Valid;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Repository
public class OTPDAO {

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public int createOTP(OTPBean otpBean) {
        //Delete old OTP
        namedParameterJdbcTemplate.update("delete from otphandler where mobileNumber=:mobileNumber",
                new BeanPropertySqlParameterSource(otpBean));
       //Add NEW OTP
        return namedParameterJdbcTemplate.update("insert into otphandler(mobileNumber,otp) " +
                "values (:mobileNumber,:otp)", new BeanPropertySqlParameterSource(otpBean));
    }

    public void validateOTP(@Valid OTPBean otpBean) {
        List<OTPBean> otpBeanList = namedParameterJdbcTemplate.query(
                "select generatedtime from otphandler where mobileNumber=:mobileNumber "
                        + " and otp=:otp",new BeanPropertySqlParameterSource(otpBean), new OTPDAO.OTPMapper());
        if(otpBeanList!=null && otpBeanList.size()>0) {
            java.util.Date generatedTimeFromDB =  otpBeanList.get(0).getTimeGenerated();
            //TODO for Paras: compare generatedTimeFromDB is less than 10 min from the current system timestamp
            Date dateNow = new Date();
            long timeDiff = dateNow.getTime() - generatedTimeFromDB.getTime();
            long timeDiffInMinutes = TimeUnit.MILLISECONDS.toMinutes(timeDiff);
            if(timeDiffInMinutes<10) {
                   namedParameterJdbcTemplate.update("delete from otphandler where mobileNumber=:mobileNumber",
                    new BeanPropertySqlParameterSource(otpBean));
            } else {
                throw new CustomAppException("OTP has been expired","SERVER.VALIDATEOTP.OTP_EXPIRED", HttpStatus.EXPECTATION_FAILED);
            }
            //If yes then set true else false
        } else {
            throw new CustomAppException("The OTP is invalid","SERVER.VALIDATEOTP.OTP_INVALID", HttpStatus.EXPECTATION_FAILED);
        }

    }

    private static final class OTPMapper implements RowMapper<OTPBean> {
        public OTPBean mapRow(ResultSet rs, int rowNum) throws SQLException {
            OTPBean otpBean = new OTPBean();
            otpBean.setTimeGenerated(rs.getTimestamp("generatedtime"));
            return otpBean;
        }
    }
}
