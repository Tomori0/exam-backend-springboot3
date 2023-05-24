package com.up9e.exam.config;

import com.up9e.exam.filter.JwtAuthenticationTokenFilter;
import com.up9e.exam.service.impl.ExamUserDetailsServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final ExamAuthenticationProvider examAuthenticationProvider;

    private final ExamUserDetailsServiceImpl examUserDetailsService;

    private final JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter;

    private final ExamAuthenticationSuccessHandler examAuthenticationSuccessHandler;

    private final ExamAuthenticationFailureHandler examAuthenticationFailureHandler;

    public SecurityConfig(ExamAuthenticationProvider examAuthenticationProvider,
                          ExamUserDetailsServiceImpl examUserDetailsService,
                          JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter,
                          ExamAuthenticationSuccessHandler examAuthenticationSuccessHandler,
                          ExamAuthenticationFailureHandler examAuthenticationFailureHandler) {
        this.examAuthenticationProvider = examAuthenticationProvider;
        this.examUserDetailsService = examUserDetailsService;
        this.jwtAuthenticationTokenFilter = jwtAuthenticationTokenFilter;
        this.examAuthenticationSuccessHandler = examAuthenticationSuccessHandler;
        this.examAuthenticationFailureHandler = examAuthenticationFailureHandler;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .formLogin()
                .loginProcessingUrl("/auth/login")
                .successHandler(examAuthenticationSuccessHandler)
                .failureHandler(examAuthenticationFailureHandler)
                .and()
                .logout()
                .and()
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/login").permitAll()
                        .requestMatchers("/auth/register").permitAll()
                        .requestMatchers("/auth/verify").permitAll()
                        .requestMatchers("/auth/resetPassword").permitAll()
                        .requestMatchers("/auth/verifyResetPassword").permitAll()
                        .requestMatchers("/auth/resend").permitAll()
                        .anyRequest().authenticated()
                )
                .csrf().disable()
                .cors()
                .and()
                .exceptionHandling().authenticationEntryPoint(new ExamAuthenticationEntryPoint())
                .and()
                .authenticationProvider(examAuthenticationProvider)
                .userDetailsService(examUserDetailsService)
                .addFilterBefore(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.addAllowedOriginPattern("*");
        corsConfiguration.addAllowedHeader("*");
        corsConfiguration.addAllowedMethod("*");
        corsConfiguration.setAllowCredentials(true);
        source.registerCorsConfiguration("/**", corsConfiguration);
        return new CorsFilter(source);
    }

}
