package com.stankarp.ratings.security;

import com.stankarp.ratings.security.jwt.JwtAuthEntryPoint;
import com.stankarp.ratings.security.jwt.JwtAuthTokenFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
        prePostEnabled = true
)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtAuthEntryPoint unauthorizedHandler;

    @Bean
    public JwtAuthTokenFilter authenticationJwtTokenFilter() {
        return new JwtAuthTokenFilter();
    }

    @Override
    public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable().
                authorizeRequests()
                .antMatchers("/api/auth/**").permitAll()
                .antMatchers(HttpMethod.GET, "/ratings", "/ratings/**").permitAll()
                .antMatchers(HttpMethod.GET, "/performers", "/performers/**").permitAll()
                .antMatchers(HttpMethod.GET,"/albums", "/albums/**").permitAll()
                .antMatchers(HttpMethod.GET,"/users", "/users/search/findByUsername").permitAll()
                .antMatchers(HttpMethod.GET,"/", "/decade/**", "/year/**", "/years/**/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
    }

//    private SimpleUrlAuthenticationFailureHandler failureHandler;
//
//    private SavedRequestAwareAuthenticationSuccessHandler successHandler;
//
//    private RestAuthenticationEntryPoint restAuthenticationEntryPoint;
//
//    private UserDetailsService userDetailsService;
//
//    public SecurityConfig(SimpleUrlAuthenticationFailureHandler failureHandler,
//                          SavedRequestAwareAuthenticationSuccessHandler successHandler,
//                          RestAuthenticationEntryPoint restAuthenticationEntryPoint,
//                          UserDetailsService userDetailsService) {
//        this.failureHandler = failureHandler;
//        this.successHandler = successHandler;
//        this.userDetailsService = userDetailsService;
//        this.restAuthenticationEntryPoint = restAuthenticationEntryPoint;
//    }
//
//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.authenticationProvider(authenticationProvider());
//        auth.inMemoryAuthentication()
//                .withUser("admin").password(encoder().encode("adminPass")).roles("ADMIN")
//                .and()
//                .withUser("user").password(encoder().encode("userPass")).roles("USER");
//    }
//
//    @Bean
//    public DaoAuthenticationProvider authenticationProvider() {
//        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
//        authProvider.setUserDetailsService(userDetailsService);
//        authProvider.setPasswordEncoder(encoder());
//        return authProvider;
//    }
//
//    @Bean
//    public PasswordEncoder encoder() {
//        return new BCryptPasswordEncoder();
//    }
//
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http
//            .csrf().disable()
//            .exceptionHandling()
//            .authenticationEntryPoint(restAuthenticationEntryPoint)
//        .and()
//            .authorizeRequests()
//            .antMatchers("/**").permitAll()
////            .antMatchers("/users/**").authenticated()
////            .antMatchers("/admin/**").hasRole("ADMIN")
//        .and()
//            .formLogin()
//            .successHandler(successHandler)
//            .failureHandler(failureHandler)
//        .and()
//            .logout();
//    }

}