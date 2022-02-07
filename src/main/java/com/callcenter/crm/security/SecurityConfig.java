package com.callcenter.crm.security;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import static com.callcenter.crm.security.UserRole.AGENT;
import static com.callcenter.crm.security.UserRole.TECHNICIAN;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private PasswordEncoder passwordEncoder;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers(HttpMethod.GET, "/api/v1/service-request/**").permitAll()
                .antMatchers(HttpMethod.PUT, "/api/v1/service-request/**").hasRole(TECHNICIAN.name())
                .antMatchers("/api/v1/service-request/**").hasRole(AGENT.name())
                .anyRequest()
                .authenticated()
                .and()
                .httpBasic();
    }

    @Bean
    @Override
    protected UserDetailsService userDetailsService() {
        UserDetails dummyAgent = User.builder()
                .username("dummyAgent")
                .password(passwordEncoder.encode("password"))
                .roles(AGENT.name())
                .build();

        UserDetails technician = User.builder()
                .username("technician")
                .password(passwordEncoder.encode("password"))
                .roles(TECHNICIAN.name())
                .build();

        return new InMemoryUserDetailsManager(dummyAgent, technician);
    }
}
