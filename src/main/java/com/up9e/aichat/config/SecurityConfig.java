package com.up9e.aichat.config;

import com.up9e.aichat.filter.JwtAuthenticationTokenFilter;
import com.up9e.aichat.service.impl.UserDetailsServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final AIChatAuthenticationProvider aiChatAuthenticationProvider;
    private final UserDetailsServiceImpl userDetailsService;
    private final JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter;
    private final AIChatAuthenticationSuccessHandler aiChatAuthenticationSuccessHandler;
    private final AIChatAuthenticationFailureHandler aiChatAuthenticationFailureHandler;

    public SecurityConfig(AIChatAuthenticationProvider aiChatAuthenticationProvider,
                          UserDetailsServiceImpl userDetailsService,
                          JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter,
                          AIChatAuthenticationSuccessHandler aiChatAuthenticationSuccessHandler,
                          AIChatAuthenticationFailureHandler aiChatAuthenticationFailureHandler) {
        this.aiChatAuthenticationProvider = aiChatAuthenticationProvider;
        this.userDetailsService = userDetailsService;
        this.jwtAuthenticationTokenFilter = jwtAuthenticationTokenFilter;
        this.aiChatAuthenticationSuccessHandler = aiChatAuthenticationSuccessHandler;
        this.aiChatAuthenticationFailureHandler = aiChatAuthenticationFailureHandler;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .formLogin()
                .loginProcessingUrl("/auth/login")
                .successHandler(aiChatAuthenticationSuccessHandler)
                .failureHandler(aiChatAuthenticationFailureHandler)
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
                .exceptionHandling().authenticationEntryPoint(new AIChatAuthenticationEntryPoint())
                .and()
                .authenticationProvider(aiChatAuthenticationProvider)
                .userDetailsService(userDetailsService)
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
