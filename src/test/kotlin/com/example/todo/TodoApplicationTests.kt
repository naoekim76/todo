package com.example.todo

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest

/**
 * Todo 애플리케이션 통합 테스트 클래스
 * 
 * Spring Boot 애플리케이션의 전체 컨텍스트를 로드하여 통합 테스트를 수행합니다.
 * @SpringBootTest 어노테이션은 전체 애플리케이션 컨텍스트를 로드하여 테스트합니다.
 */
@SpringBootTest
class TodoApplicationTests {

    /**
     * 애플리케이션 컨텍스트 로드 테스트
     * 
     * Spring 애플리케이션 컨텍스트가 정상적으로 로드되는지 확인합니다.
     * 이 테스트가 성공하면 모든 빈이 올바르게 구성되었음을 의미합니다.
     */
    @Test
    fun contextLoads() {
        // 컨텍스트 로드 확인 (별도의 검증 로직 없이 예외가 발생하지 않으면 성공)
    }

}
