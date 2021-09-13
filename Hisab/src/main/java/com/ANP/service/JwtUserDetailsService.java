package com.ANP.service;

import java.util.ArrayList;

import com.ANP.repository.EmployeeDAO;
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
        // if ("javainuse".equals(username))
        //  {
   //     System.out.println("Comming in user details service ");
     //   return new User(username, "12345xxf3$",
       //         new ArrayList<>());
//        } else {
//            throw new UsernameNotFoundException("User not found with username: " + username);
//
//        }
       //if not present in the DB then return empty
       String returnedMobileNumber = employeeDAO.isMobilePresent(username);
        return new User(returnedMobileNumber, "12345xxf3$", new ArrayList<>());
    }





}