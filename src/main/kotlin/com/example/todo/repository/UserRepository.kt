package com.example.todo.repository

import com.example.todo.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

/**
 * User 레포지토리 인터페이스
 * 
 * User 엔티티에 대한 데이터 액세스 기능을 제공하는 JPA 레포지토리 인터페이스입니다.
 * JpaRepository를 상속받아 기본적인 CRUD 기능(findAll, findById, save, delete 등)을 제공합니다.
 * 이메일 기반 사용자 조회 및 중복 확인을 위한 추가 메서드를 정의합니다.
 */
@Repository
interface UserRepository : JpaRepository<User, Long> {
    /**
     * 이메일로 사용자 조회
     * 
     * 주어진 이메일에 해당하는 사용자를 조회합니다.
     * 로그인 처리 시 사용자 인증에 사용됩니다.
     * 
     * @param email 조회할 사용자의 이메일
     * @return 해당 이메일을 가진 사용자, 없으면 null
     */
    fun findByEmail(email: String): User?

    /**
     * 이메일 존재 여부 확인
     * 
     * 주어진 이메일이 이미 등록되어 있는지 확인합니다.
     * 회원가입 시 이메일 중복 체크에 사용됩니다.
     * 
     * @param email 확인할 이메일
     * @return 이메일이 존재하면 true, 없으면 false
     */
    fun existsByEmail(email: String): Boolean
}
