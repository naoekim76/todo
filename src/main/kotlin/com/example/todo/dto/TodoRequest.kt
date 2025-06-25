package com.example.todo.dto

import jakarta.validation.constraints.NotBlank

/**
 * Todo 생성 및 수정 요청 DTO (Data Transfer Object)
 * 
 * 클라이언트로부터 Todo 항목 생성 및 수정 요청을 받을 때 사용하는 데이터 클래스입니다.
 * 입력값 검증(validation)을 위한 어노테이션이 포함되어 있습니다.
 * 
 * @property title Todo 제목 (필수 입력 필드)
 * @property description Todo 설명 (선택적 입력 필드)
 * @property isDone Todo 완료 상태 (기본값: false)
 */
data class TodoRequest(
    @field:NotBlank(message = "Title is required")
    val title: String,
    val description: String? = null,
    val isDone: Boolean = false
)
