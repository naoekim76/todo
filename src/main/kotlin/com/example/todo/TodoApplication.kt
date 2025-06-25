package com.example.todo

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

/**
 * Todo 애플리케이션의 메인 클래스
 * 
 * @SpringBootApplication 어노테이션은 다음 세 가지 어노테이션을 포함합니다:
 * - @Configuration: 이 클래스가 빈 정의의 소스임을 나타냅니다.
 * - @EnableAutoConfiguration: Spring Boot가 클래스패스 설정, 다른 빈, 다양한 속성 설정에 기반하여 빈을 자동으로 구성하도록 지시합니다.
 * - @ComponentScan: com.example.todo 패키지에서 컴포넌트, 구성, 서비스 등을 찾도록 Spring에 지시합니다.
 */
@SpringBootApplication
class TodoApplication

/**
 * 애플리케이션의 진입점(entry point)
 * 
 * Spring Boot 애플리케이션을 시작하는 메인 함수입니다.
 * runApplication 함수는 TodoApplication 클래스를 참조하여 Spring 애플리케이션 컨텍스트를 생성합니다.
 * 
 * @param args 명령줄 인수
 */
fun main(args: Array<String>) {
    runApplication<TodoApplication>(*args)
}
