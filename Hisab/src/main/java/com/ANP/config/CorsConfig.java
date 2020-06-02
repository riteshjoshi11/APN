package com.ANP.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class CorsConfig {

  //TODO Allow only request from specific channel now its open for all
  @Bean
  public WebMvcConfigurer corsConfigurer() {
    return new WebMvcConfigurer() {
      @Override
      public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**");
      }
    };
  }

//
//  @EnableWebSecurity
//  @Configuration
//  class WebSecurityConfig extends WebSecurityConfigurerAdapter {
//
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//      System.out.println("configuration filter");
//
//      http.csrf().disable()
//              .addFilterAfter(new AuthorizationFilter(), UsernamePasswordAuthenticationFilter.class)
//              .authorizeRequests()
//
//              .antMatchers(HttpMethod.POST, "/login/verifyOTP").permitAll()
//              .antMatchers(HttpMethod.POST, "/apn/sendOTP").permitAll()
//              .antMatchers(HttpMethod.GET,"/swagger-ui.html").permitAll()
//
//              .anyRequest().authenticated();
//
//
//    }
//  }


}
