package com.swip.swipwms.querytool.config;


import com.swip.swipwms.model.Admin;
import com.swip.swipwms.model.Employee;
import com.swip.swipwms.repository.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        for(Employee emp : UserRepository.getAllEmployees()) {
            auth.inMemoryAuthentication().withUser(emp.getName()).password(emp.getPassword()).roles("EMPLOYEE");
        }

        for(Admin admin : UserRepository.getAllAdmins()) {
            auth.inMemoryAuthentication().withUser(admin.getName()).password(admin.getPassword()).roles("ADMIN");
        }
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors()
                .and()
                .csrf().disable()
                .httpBasic().disable()
                .exceptionHandling()
                .and()
                .authorizeRequests()
                .antMatchers(HttpMethod.GET, "/warehouse/**").permitAll()
                .antMatchers(HttpMethod.GET, "/user/**").permitAll()
                .antMatchers("/").permitAll()
                .antMatchers("/listAllItems").permitAll()
                .antMatchers("/listItemsByWarehouse/**").permitAll()
                .antMatchers("/browseByCategory/**").permitAll()
                .antMatchers("/searchItemPage/**").permitAll()
                .antMatchers("/loginPage").permitAll()
                .antMatchers("/orderPage").hasRole("EMPLOYEE")
                .antMatchers("/postOrderPage").hasRole("EMPLOYEE")
                .anyRequest()
                .authenticated()
                .and()
                .formLogin()
                .loginPage("/loginPage")
                .loginProcessingUrl("/authenticate")
                .usernameParameter("username")
                .passwordParameter("password")
                .failureUrl("/loginPage?loginFailed=true")
                .and()
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/");
    }

    @Bean
    public NoOpPasswordEncoder noOpPasswordEncoder() {
        return (NoOpPasswordEncoder) NoOpPasswordEncoder.getInstance();
    }
}