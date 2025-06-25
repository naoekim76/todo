package com.example.todo.service

import com.example.todo.dto.LoginRequest
import com.example.todo.dto.SignupRequest
import com.example.todo.entity.User
import com.example.todo.repository.UserRepository
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * User 서비스 클래스
 * 
 * 사용자 관련 비즈니스 로직을 처리하는 서비스 클래스입니다.
 * 회원가입, 로그인, 사용자 조회 등의 기능을 제공합니다.
 * 
 * @property userRepository User 데이터 액세스를 위한 레포지토리
 * @property passwordEncoder 비밀번호 암호화를 위한 인코더
 */
@Service
@Transactional
class UserService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder
) {

    /**
     * 회원가입 처리
     * 
     * 새로운 사용자를 등록합니다. 이메일 중복을 확인하고, 비밀번호를 암호화하여 저장합니다.
     * 
     * @param request 회원가입 요청 데이터 (이메일, 비밀번호, 닉네임)
     * @return 생성된 사용자 정보
     * @throws IllegalArgumentException 이미 등록된 이메일인 경우
     */
    fun signup(request: SignupRequest): User {
        // 이메일 중복 확인
        if (userRepository.existsByEmail(request.email)) {
            throw IllegalArgumentException("이미 등록된 이메일입니다")
        }

        // 사용자 엔티티 생성 (비밀번호 암호화)
        val user = User(
            email = request.email,
            password = passwordEncoder.encode(request.password),
            nickname = request.nickname
        )

        // 저장 및 반환
        return userRepository.save(user)
    }

    /**
     * 로그인 처리
     * 
     * 이메일과 비밀번호를 검증하여 로그인을 처리합니다.
     * 읽기 전용 트랜잭션으로 처리됩니다.
     * 
     * @param request 로그인 요청 데이터 (이메일, 비밀번호)
     * @return 인증된 사용자 정보
     * @throws IllegalArgumentException 이메일이 존재하지 않거나 비밀번호가 일치하지 않는 경우
     */
    @Transactional(readOnly = true)
    fun login(request: LoginRequest): User {
        // 이메일로 사용자 조회
        val user = userRepository.findByEmail(request.email)
            ?: throw IllegalArgumentException("이메일 또는 비밀번호가 잘못되었습니다")

        // 비밀번호 검증
        if (!passwordEncoder.matches(request.password, user.password)) {
            throw IllegalArgumentException("이메일 또는 비밀번호가 잘못되었습니다")
        }

        return user
    }

    /**
     * ID로 사용자 조회
     * 
     * 주어진 ID에 해당하는 사용자를 조회합니다.
     * 읽기 전용 트랜잭션으로 처리됩니다.
     * 
     * @param id 조회할 사용자의 ID
     * @return 조회된 사용자 정보
     * @throws IllegalArgumentException 사용자를 찾을 수 없는 경우
     */
    @Transactional(readOnly = true)
    fun findById(id: Long): User {
        return userRepository.findById(id)
            .orElseThrow { IllegalArgumentException("사용자를 찾을 수 없습니다") }
    }
}
