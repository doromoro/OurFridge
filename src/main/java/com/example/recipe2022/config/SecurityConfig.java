package com.example.recipe2022.config;

import com.example.recipe2022.config.jwt.JwtAuthenticationFilter;
import com.example.recipe2022.config.jwt.JwtTokenProvider;
import com.example.recipe2022.handler.oauth.OAuth2CustomFailureHandler;
import com.example.recipe2022.handler.oauth.OAuth2CustomSuccessHandler;
import com.example.recipe2022.service.oauth2.CustomOidcUserService;
import com.example.recipe2022.service.oauth2.OAuth2Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;


@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
@Slf4j
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;
    private final RedisTemplate<String, Object> redisTemplate;
    private final CustomOidcUserService customOidcUserService;
    private final OAuth2Service oAuth2Service;
    private final OAuth2CustomFailureHandler oAuth2CustomFailureHandler;
    private final OAuth2CustomSuccessHandler oAuth2CustomSuccessHandler;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http    // 일반
                .httpBasic().disable()
                .csrf().disable()
                .cors().and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests().requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
                .antMatchers("/", "/**").permitAll()
                .and()
                .oauth2Login()
                .loginPage("/social")
                .userInfoEndpoint()
                .oidcUserService(customOidcUserService)
                .userService(oAuth2Service)
                .and()
                .successHandler(oAuth2CustomSuccessHandler)
                .failureHandler(oAuth2CustomFailureHandler);
        http    // 일반
                .httpBasic().disable()
                .csrf().disable()
                .cors().and()
                .formLogin().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests().requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
                .antMatchers("/", "/**").permitAll()
                .and()
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider, redisTemplate), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web -> web.ignoring().antMatchers(AUTH_WHITELIST));
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration conf = new CorsConfiguration();
        conf.setAllowedOrigins(Arrays.asList("https://localhost.com:3000"));
        conf.addAllowedOriginPattern("*");
        conf.setAllowedMethods(Arrays.asList("GET","POST"));
        conf.setAllowCredentials(true);
        conf.addAllowedHeader("Authorization");
        conf.addAllowedHeader("Set-Cookie");
        conf.addExposedHeader("Authorization");
        conf.addExposedHeader("Set-Cookie");// you can configure many allowed CORS headers
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", conf);
        return source;
    }
    private static final String[] AUTH_WHITELIST = {
            "/v2/api-docs",
            "/v3/api-docs/**",
            "/configuration/ui",
            "/swagger-resources/**",
            "/configuration/security",
            "/swagger-ui.html",
            "/webjars/**",
            "/file/**",
            "/image/**",
            "/swagger/**",
            "/swagger-ui/**",
            "/h2/**"
    };
}