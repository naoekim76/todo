package com.example.todo.entity

import jakarta.persistence.*

/**
 * Todo 엔티티 클래스
 * 
 * 할 일 항목을 데이터베이스에 저장하기 위한 JPA 엔티티 클래스입니다.
 * 'todos' 테이블과 매핑됩니다.
 * 
 * @property id Todo의 고유 식별자 (기본 키)
 * @property title Todo 제목
 * @property description Todo 설명 (최대 1000자, null 가능)
 * @property isDone Todo 완료 상태
 * @property user Todo 작성자 (User 엔티티와의 다대일 관계)
 */
@Entity
@Table(name = "todos")
data class Todo(
    /**
     * Todo의 고유 식별자 (기본 키)
     * 
     * 자동 증가(AUTO_INCREMENT) 전략을 사용하여 생성됩니다.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    /**
     * Todo 제목
     * 
     * 필수 입력 필드입니다 (NOT NULL).
     */
    @Column(nullable = false)
    val title: String,

    /**
     * Todo 설명
     * 
     * 최대 1000자까지 입력 가능하며, null 값을 허용합니다.
     */
    @Column(length = 1000)
    val description: String? = null,

    /**
     * Todo 완료 상태
     * 
     * 기본값은 false(미완료)이며, 필수 필드입니다 (NOT NULL).
     */
    @Column(nullable = false)
    val isDone: Boolean = false,

    /**
     * Todo 작성자
     * 
     * User 엔티티와의 다대일(N:1) 관계를 나타냅니다.
     * 지연 로딩(LAZY) 전략을 사용하여 필요할 때만 User 정보를 로드합니다.
     * 'user_id' 컬럼을 외래 키로 사용하며, 필수 필드입니다 (NOT NULL).
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    val user: User
)
