package com.ANP.service;

import java.util.ArrayList;

import com.ANP.repository.EmployeeDAO;
import com.ANP.util.ANPUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class JwtUserDetailsService implements UserDetailsService {

    @Autowired
    EmployeeDAO employeeDAO ;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
       String returnedMobileNumber = employeeDAO.isMobilePresent(username);
       System.out.println("JwtUserDetailsService: loadUserByUsername returnedMobileNumber[" + returnedMobileNumber + "]");
       if(!ANPUtils.isNullOrEmpty(returnedMobileNumber)) {
           return (new User(returnedMobileNumber, "12345xxf3$", new ArrayList<>()));
       }
       return null;
    }
}