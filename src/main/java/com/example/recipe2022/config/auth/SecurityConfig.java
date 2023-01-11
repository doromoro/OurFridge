package com.example.recipe2022.config.auth;

import com.example.recipe2022.handler.OAuth2AuthenticationFailureHandler;
import com.example.recipe2022.handler.OAuth2AuthenticationSuccessHandler;
import com.example.recipe2022.model.enumer.Role;
import com.example.recipe2022.service.CustomOAuth2AuthService;
import com.example.recipe2022.service.CustomOAuth2UserService;
import com.example.recipe2022.service.CustomOidcUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@RequiredArgsConstructor        // 생성자 주입 어노테이션 반대가 내가 알기론 @Autowired
@EnableWebSecurity              // 스프링시큐리티 사용을 위한 어노테이션
@Configuration                  // 설정파일을 만들기 위한 애노테이션 or Bean을 등록하기 위한 애노테이션
public class SecurityConfig {   // 스프링 기반의 애플리케이션의 보안(인증과 권한, 인가 등)을 담당하는 스프링 하위 프레임워크

    private final CustomOAuth2AuthService customOAuth2AuthService;

    private final CustomOidcUserService customOidcUserService;

    private final CustomOAuth2UserService customOAuth2UserService;

    private final OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;

    private final OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler;

    @Bean   // Spring IoC 컨테이너가 관리하는 자바 객체
    PasswordEncoder passwordEncoder() {     //비밀번호를 암호화하는 역할이다. 구현체들이 하는 역할은 바로 이 암호화를 어떻게 할지, 암호화 알고리즘에 해당
        return new BCryptPasswordEncoder(); // BCrypt라는 해시 함수를 이용하여 패스워드를 암호화하는 구현체
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web -> web.ignoring().antMatchers(AUTH_WHITELIST)); //antMacher : 제공된 ant패턴과 일치할 때만 호출되도록 HttpSecurity를 구성할 수 있음
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http     //소셜 로그인
                .httpBasic().disable()
                .formLogin().disable()
                .csrf().disable()       // CSRF 설정 Disable, Post나 Put과 같이 리소스를 변경하는 요청의 경우 내가 내보냈던 리소스에서 올라온 요청인지 확인
                .headers().frameOptions().disable()
                .and()
                .cors()     //CorsFilter : 허가된 사이트나 클라이언트의 요청인지 검사하는 역할
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // 여기서는 세션을 사용하지 않기 때문에 세션 설정을 Stateless 로 설정
                .and()
                .oauth2Login()
                .userInfoEndpoint()
                .oidcUserService(customOidcUserService)
                .userService(customOAuth2AuthService)
                .and()
                .successHandler(oAuth2AuthenticationSuccessHandler) // exception handling 할 때 우리가 만든 클래스를 추가
                .failureHandler(oAuth2AuthenticationFailureHandler);
        http
                .formLogin().disable()
                .httpBasic().disable()
                .cors().disable()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/","/css/**","/images/**","/js/**","/h2-console/**").permitAll()
                .antMatchers("/api/v1/**").hasRole(Role.USER.name())
                .antMatchers("/member/login").permitAll()
                .antMatchers("/member/join").permitAll()
                .antMatchers("/member").hasRole(Role.USER.name())
                .anyRequest().authenticated()    // 나머지 API 는 전부 인증 필요
                .and()
                .logout().logoutSuccessUrl("/") // LogoutFilter : Request가 로그아웃하겠다고 하는것인지 체크한다.
                .and()
                .oauth2Login().userInfoEndpoint().userService(customOAuth2UserService);
        return http.build();
    }

    // 로그인이 필요없는 URL 모음 white list
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
            "/h2/**",

            "/**/**"
    };
}