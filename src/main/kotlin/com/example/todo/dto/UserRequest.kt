package com.example.todo.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

/**
 * 회원가입 요청 DTO (Data Transfer Object)
 * 
 * 클라이언트로부터 회원가입 요청을 받을 때 사용하는 데이터 클래스입니다.
 * 입력값 검증(validation)을 위한 어노테이션이 포함되어 있습니다.
 * 
 * @property email 사용자 이메일 (필수, 이메일 형식 검증)
 * @property password 사용자 비밀번호 (필수, 최소 6자 이상)
 * @property nickname 사용자 닉네임 (필수)
 */
data class SignupRequest(
    @field:NotBlank(message = "이메일은 필수입니다")
    @field:Email(message = "올바른 이메일 형식이 아닙니다")
    val email: String,

    @field:NotBlank(message = "비밀번호는 필수입니다")
    @field:Size(min = 6, message = "비밀번호는 최소 6자 이상이어야 합니다")
    val password: String,

    @field:NotBlank(message = "닉네임은 필수입니다")
    val nickname: String
)

/**
 * 로그인 요청 DTO (Data Transfer Object)
 * 
 * 클라이언트로부터 로그인 요청을 받을 때 사용하는 데이터 클래스입니다.
 * 입력값 검증(validation)을 위한 어노테이션이 포함되어 있습니다.
 * 
 * @property email 사용자 이메일 (필수, 이메일 형식 검증)
 * @property password 사용자 비밀번호 (필수)
 */
data class LoginRequest(
    @field:NotBlank(message = "이메일은 필수입니다")
    @field:Email(message = "올바른 이메일 형식이 아닙니다")
    val email: String,

    @field:NotBlank(message = "비밀번호는 필수입니다")
    val password: String
)
