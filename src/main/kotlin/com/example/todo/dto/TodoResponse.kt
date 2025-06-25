package com.example.todo.dto

/**
 * Todo 응답 DTO (Data Transfer Object)
 * 
 * 서버에서 클라이언트로 Todo 항목 정보를 반환할 때 사용하는 데이터 클래스입니다.
 * Todo 엔티티의 정보를 클라이언트에게 제공하기 위한 형태로 변환한 객체입니다.
 * 
 * @property id Todo의 고유 식별자
 * @property title Todo 제목
 * @property description Todo 설명 (null 가능)
 * @property isDone Todo 완료 상태
 */
data class TodoResponse(
    val id: Long,
    val title: String,
    val description: String?,
    val isDone: Boolean
)
