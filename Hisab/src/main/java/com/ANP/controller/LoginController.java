package com.ANP.controller;

import com.ANP.bean.IntermediateLoginBean;
import com.ANP.bean.OTPBean;
import com.ANP.bean.SuccessLoginBean;
import com.ANP.bean.Token;
import com.ANP.repository.OTPDAO;
import com.ANP.service.JwtUserDetailsService;
import com.ANP.service.LoginHandler;
import com.ANP.util.HisabError;
import com.ANP.util.HisabException;
import com.ANP.util.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/login")
public class LoginController {

    @Autowired
    LoginHandler loginHandler;

    @Autowired
    OTPDAO otpdao;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenUtil tokenUtil;

    @Autowired
    private JwtUserDetailsService userDetailsService;


    @PostMapping(path = "/sendOTP", produces = "application/json" )
    public ResponseEntity sendOTP(@RequestParam String mobileNumber) {
        ResponseEntity<String> responseEntity = null;
        if(loginHandler.sendOTP(mobileNumber)) {
            responseEntity = new ResponseEntity<>("Success", HttpStatus.OK);
        } else {
            responseEntity = new ResponseEntity<>("Failure", HttpStatus.OK);
        }
        return responseEntity;
    }

    @PostMapping(path = "/verifyOTP", produces = "application/json" )
    public ResponseEntity verifyOTP(OTPBean otpBean) throws HisabException {
        ResponseEntity<Object> responseEntity = null;
        boolean isValidOtp = otpdao.validateOTP(otpBean);
        TokenUtil tu = new TokenUtil();
        if (isValidOtp) {
            Token token = new Token();

            //  authenticate(otpBean.getMobileNumber(),otpBean.getOtp());

            final UserDetails userDetails = userDetailsService
                    .loadUserByUsername(otpBean.getMobileNumber());

            String tok = tokenUtil.generateToken(userDetails);

            token.setToken(tok);
            responseEntity = new ResponseEntity<>(token, HttpStatus.OK);
        } else throw new HisabException(HisabError.INVALID_OTP);

        return responseEntity;
    }

    /*
        Once mobile number is verified using OTP then this method will be invoked by UI.
     */
    @PostMapping(path = "/getUserRegistrationStatusOnVerifiedOTP", produces = "application/json" )
    public IntermediateLoginBean getUserRegistrationStatusOnVerifiedOTP(@RequestParam String mobileNumber) {
        return loginHandler.isMobileRegistered(mobileNumber);
    }

    /*
    Once user has either selected his/her organization from the one or more organization, this will give the details of that organization.
    */
    @PostMapping(path = "/getLoggedInUserDetails", produces = "application/json" )
    public SuccessLoginBean getLoggedInUserDetails(@RequestParam  String mobileNumber, @RequestParam long orgId) {
        return loginHandler.getLoggedInUserDetails(mobileNumber,orgId);
    }

    private void authenticate(String username, String password) throws Exception {
        try {
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(username, password);

            System.out.println("  principal " + usernamePasswordAuthenticationToken.getPrincipal() + " criden " + usernamePasswordAuthenticationToken.getCredentials());

            authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }

}
