package com.stankarp.ratings.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {


    private SimpleUrlAuthenticationFailureHandler failureHandler;

    private SavedRequestAwareAuthenticationSuccessHandler successHandler;

    private RestAuthenticationEntryPoint restAuthenticationEntryPoint;

    private UserDetailsService userDetailsService;

    public SecurityConfig(SimpleUrlAuthenticationFailureHandler failureHandler,
                          SavedRequestAwareAuthenticationSuccessHandler successHandler,
                          RestAuthenticationEntryPoint restAuthenticationEntryPoint,
                          UserDetailsService userDetailsService) {
        this.failureHandler = failureHandler;
        this.successHandler = successHandler;
        this.userDetailsService = userDetailsService;
        this.restAuthenticationEntryPoint = restAuthenticationEntryPoint;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider());
        auth.inMemoryAuthentication()
                .withUser("admin").password(encoder().encode("adminPass")).roles("ADMIN")
                .and()
                .withUser("user").password(encoder().encode("userPass")).roles("USER");
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(encoder());
        return authProvider;
    }

    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            .exceptionHandling()
            .authenticationEntryPoint(restAuthenticationEntryPoint)
        .and()
            .authorizeRequests()
            .antMatchers("/**").permitAll()
//            .antMatchers("/users/**").authenticated()
//            .antMatchers("/admin/**").hasRole("ADMIN")
        .and()
            .formLogin()
            .successHandler(successHandler)
            .failureHandler(failureHandler)
        .and()
            .logout();
    }

}
