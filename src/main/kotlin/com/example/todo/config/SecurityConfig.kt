package com.example.todo.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain

/**
 * Spring Security 설정 클래스
 * 
 * 이 클래스는 애플리케이션의 보안 설정을 정의합니다.
 * @Configuration 어노테이션은 이 클래스가 Spring 구성 클래스임을 나타냅니다.
 * @EnableWebSecurity 어노테이션은 Spring Security 웹 보안 지원을 활성화합니다.
 */
@Configuration
@EnableWebSecurity
class SecurityConfig {

    /**
     * 비밀번호 인코더 빈 설정
     * 
     * 사용자 비밀번호를 안전하게 저장하기 위해 BCrypt 알고리즘을 사용하는 인코더를 제공합니다.
     * BCrypt는 단방향 해시 함수로, 동일한 입력에 대해 매번 다른 해시를 생성하여 보안성을 높입니다.
     * 
     * @return BCrypt 비밀번호 인코더
     */
    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    /**
     * 보안 필터 체인 설정
     * 
     * HTTP 요청에 대한 보안 규칙을 정의합니다.
     * 
     * @param http HttpSecurity 객체
     * @return 구성된 SecurityFilterChain
     */
    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            // HTTP 요청 권한 설정
            .authorizeHttpRequests { auth ->
                auth
                    // 로그인, 회원가입, H2 콘솔 접근은 인증 없이 허용
                    .requestMatchers("/", "/**", "/login", "/signup", "/h2-console/**").permitAll()
                    // 그 외 모든 요청은 인증 필요
                    .anyRequest().authenticated()
            }
            // 기본 폼 로그인 비활성화 (커스텀 로그인 사용)
            .formLogin { form ->
                form.disable()
            }
            // 기본 로그아웃 비활성화 (커스텀 로그아웃 사용)
            .logout { logout ->
                logout.disable()
            }
            // CSRF 보호 비활성화 (개발 편의성을 위해, 실제 운영 환경에서는 활성화 권장)
            .csrf { csrf ->
                csrf.disable()
            }
            // H2 콘솔 사용을 위한 프레임 옵션 비활성화
            .headers { headers ->
                headers.frameOptions().disable()
            }

        return http.build()
    }
}
