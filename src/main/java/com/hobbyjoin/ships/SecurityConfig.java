package com.hobbyjoin.ships;

import com.hobbyjoin.ships.model.user.UserDetailsServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    private UserDetailsServiceImpl userDetailsService;

    public SecurityConfig(UserDetailsServiceImpl userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
            auth.userDetailsService(userDetailsService);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.authorizeRequests()
        .antMatchers(HttpMethod.GET,"/save").hasRole("ADMIN")
        .antMatchers(HttpMethod.GET,"/my-account").authenticated()
        .antMatchers(HttpMethod.GET,"/clear-standing-ships").hasRole("ADMIN")
        .antMatchers(HttpMethod.GET,"/destination/delete/*").hasRole("ADMIN")
        .antMatchers(HttpMethod.POST,"/destination/delete/*").hasRole("ADMIN")
        .antMatchers(HttpMethod.GET,"/destination/edit/*").hasRole("HARBOR_MASTER")
        .antMatchers(HttpMethod.POST,"/destination/edit/*").hasRole("HARBOR_MASTER")
        .antMatchers(HttpMethod.GET,"/destination/add").hasRole("ADMIN")
        .antMatchers(HttpMethod.POST,"/destination/add").hasRole("ADMIN")
        .and().formLogin().loginPage("/login").permitAll();

    }
}
