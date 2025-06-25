package com.example.todo.entity

import jakarta.persistence.*

/**
 * User 엔티티 클래스
 * 
 * 사용자 정보를 데이터베이스에 저장하기 위한 JPA 엔티티 클래스입니다.
 * 'users' 테이블과 매핑됩니다.
 * 
 * @property id 사용자의 고유 식별자 (기본 키)
 * @property email 사용자 이메일 (로그인 ID로 사용)
 * @property password 암호화된 사용자 비밀번호
 * @property nickname 사용자 닉네임
 * @property todos 사용자가 작성한 Todo 목록
 */
@Entity
@Table(name = "users")
data class User(
    /**
     * 사용자의 고유 식별자 (기본 키)
     * 
     * 자동 증가(AUTO_INCREMENT) 전략을 사용하여 생성됩니다.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    /**
     * 사용자 이메일
     * 
     * 로그인 ID로 사용되며, 중복을 허용하지 않습니다 (UNIQUE).
     * 필수 입력 필드입니다 (NOT NULL).
     */
    @Column(unique = true, nullable = false)
    val email: String,

    /**
     * 암호화된 사용자 비밀번호
     * 
     * BCrypt 알고리즘으로 암호화되어 저장됩니다.
     * 필수 입력 필드입니다 (NOT NULL).
     */
    @Column(nullable = false)
    val password: String,

    /**
     * 사용자 닉네임
     * 
     * 화면에 표시되는 사용자 이름입니다.
     * 필수 입력 필드입니다 (NOT NULL).
     */
    @Column(nullable = false)
    val nickname: String,

    /**
     * 사용자가 작성한 Todo 목록
     * 
     * User 엔티티와 Todo 엔티티 간의 일대다(1:N) 관계를 나타냅니다.
     * 'user' 필드에 의해 매핑되며, 사용자가 삭제되면 관련 Todo도 모두 삭제됩니다 (CASCADE).
     * 고아 객체 제거(orphanRemoval)가 활성화되어 있어 컬렉션에서 제거된 Todo는 데이터베이스에서도 삭제됩니다.
     */
    @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL], orphanRemoval = true)
    val todos: MutableList<Todo> = mutableListOf()
)
