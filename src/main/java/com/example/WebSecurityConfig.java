package com.example;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Autowired
    private LoginSuccessHandler loginSuccessHandler;
    @Autowired
    private CustomLoginFailureHandler loginFailureHandler;

    @Autowired
    private DataSource dataSource;

//    @Bean
//    @Override
//    public AuthenticationManager authenticationManagerBean() throws Exception {
//        return super.authenticationManagerBean();
//    }
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Autowired
    public void configAuthentication(AuthenticationManagerBuilder authBuilder) throws Exception {
        authBuilder.jdbcAuthentication()
                .dataSource(dataSource)
                .passwordEncoder(new BCryptPasswordEncoder())
                .usersByUsernameQuery("select username, password, enabled from user where username=?")
                .authoritiesByUsernameQuery("SELECT user.username,permission.name FROM user,role,user_role,permission,role_permission WHERE user.id=user_role.user_id AND role.id=user_role.role_id AND role.id=role_permission.role_id AND permission.id=role_permission.permission_id  AND user.username=?");
    }

//    protected void configure(HttpSecurity http) throws Exception {
//        http.authorizeRequests()
//                .antMatchers("/webjars/**").permitAll()
//                .antMatchers("/common/**").permitAll()
//                .antMatchers("/login").permitAll()
//                .antMatchers("/logout").permitAll()
//                .antMatchers("/verify").permitAll()
//                .antMatchers("/home").authenticated()
//                .antMatchers("/user/info").authenticated()
//                .antMatchers("/change/password").authenticated()
//                .antMatchers("/new/password").authenticated()
//                .anyRequest()
//                .access("@rbacService.hasPermission(request , authentication)")
//                .and()
//                .formLogin().loginPage("/login")
//                .successHandler(loginSuccessHandler)
//                .failureHandler(loginFailureHandler)
//                .permitAll()
//                .and()
//                .logout().permitAll()
//                .and()
//                .exceptionHandling().accessDeniedPage("/403");
//    }
    @Bean
    protected SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/webjars/**").permitAll()
                .antMatchers("/common/**").permitAll()
                .antMatchers("/login").permitAll()
                .antMatchers("/logout").permitAll()
                .antMatchers("/verify").permitAll()
                .antMatchers("/home").authenticated()
                .antMatchers("/user/info").authenticated()
                .antMatchers("/change/password").authenticated()
                .antMatchers("/new/password").authenticated()
                .anyRequest()
                .access("@rbacService.hasPermission(request , authentication)")
                .and()
                .formLogin().loginPage("/login")
                .successHandler(loginSuccessHandler)
                .failureHandler(loginFailureHandler)
                .permitAll()
                .and()
                .logout().permitAll()
                .and()
                .exceptionHandling().accessDeniedPage("/403");

        return http.build();
    }

}
