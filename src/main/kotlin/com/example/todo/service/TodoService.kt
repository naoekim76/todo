package com.example.todo.service

import com.example.todo.dto.TodoRequest
import com.example.todo.dto.TodoResponse
import com.example.todo.entity.Todo
import com.example.todo.entity.User
import com.example.todo.repository.TodoRepository
import com.example.todo.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * Todo 서비스 클래스
 * 
 * Todo 관련 비즈니스 로직을 처리하는 서비스 클래스입니다.
 * 사용자별 데이터 격리를 보장하며, Todo 항목의 CRUD 작업을 수행합니다.
 * 
 * @property todoRepository Todo 데이터 액세스를 위한 레포지토리
 * @property userRepository User 데이터 액세스를 위한 레포지토리
 */
@Service
@Transactional
class TodoService(
    private val todoRepository: TodoRepository,
    private val userRepository: UserRepository
) {

    /**
     * Todo 생성
     * 
     * 새로운 Todo 항목을 생성하고 저장합니다.
     * 
     * @param request Todo 생성 요청 데이터 (제목, 설명, 완료 상태)
     * @param userId Todo 작성자의 ID
     * @return 생성된 Todo 정보
     * @throws IllegalArgumentException 사용자를 찾을 수 없는 경우
     */
    fun createTodo(request: TodoRequest, userId: Long): TodoResponse {
        // 사용자 조회
        val user = userRepository.findById(userId)
            .orElseThrow { IllegalArgumentException("User not found") }

        // Todo 엔티티 생성
        val todo = Todo(
            title = request.title,
            description = request.description,
            isDone = request.isDone,
            user = user
        )
        // 저장 및 응답 변환
        val savedTodo = todoRepository.save(todo)
        return savedTodo.toResponse()
    }

    /**
     * 사용자의 모든 Todo 조회
     * 
     * 특정 사용자의 모든 Todo 항목을 조회합니다.
     * 읽기 전용 트랜잭션으로 처리됩니다.
     * 
     * @param userId 조회할 사용자의 ID
     * @return 사용자의 Todo 목록
     */
    @Transactional(readOnly = true)
    fun getAllTodos(userId: Long): List<TodoResponse> {
        return todoRepository.findByUserId(userId).map { it.toResponse() }
    }

    /**
     * 특정 Todo 조회
     * 
     * 특정 ID와 사용자 ID에 해당하는 Todo 항목을 조회합니다.
     * 읽기 전용 트랜잭션으로 처리됩니다.
     * 
     * @param id 조회할 Todo의 ID
     * @param userId 조회할 사용자의 ID
     * @return 조회된 Todo 정보
     * @throws IllegalArgumentException Todo를 찾을 수 없는 경우
     */
    @Transactional(readOnly = true)
    fun getTodoById(id: Long, userId: Long): TodoResponse {
        val todo = todoRepository.findByIdAndUserId(id, userId)
            ?: throw IllegalArgumentException("Todo not found with id: $id")
        return todo.toResponse()
    }

    /**
     * Todo 업데이트
     * 
     * 특정 ID와 사용자 ID에 해당하는 Todo 항목을 업데이트합니다.
     * 
     * @param id 업데이트할 Todo의 ID
     * @param request 업데이트할 Todo 데이터 (제목, 설명, 완료 상태)
     * @param userId 사용자의 ID
     * @return 업데이트된 Todo 정보
     * @throws IllegalArgumentException Todo를 찾을 수 없는 경우
     */
    fun updateTodo(id: Long, request: TodoRequest, userId: Long): TodoResponse {
        // 기존 Todo 조회
        val existingTodo = todoRepository.findByIdAndUserId(id, userId)
            ?: throw IllegalArgumentException("Todo not found with id: $id")

        // 업데이트된 Todo 생성 (불변 객체 패턴)
        val updatedTodo = existingTodo.copy(
            title = request.title,
            description = request.description,
            isDone = request.isDone
        )
        // 저장 및 응답 변환
        val savedTodo = todoRepository.save(updatedTodo)
        return savedTodo.toResponse()
    }

    /**
     * Todo 삭제
     * 
     * 특정 ID와 사용자 ID에 해당하는 Todo 항목을 삭제합니다.
     * 
     * @param id 삭제할 Todo의 ID
     * @param userId 사용자의 ID
     * @throws IllegalArgumentException Todo를 찾을 수 없는 경우
     */
    fun deleteTodo(id: Long, userId: Long) {
        val todo = todoRepository.findByIdAndUserId(id, userId)
            ?: throw IllegalArgumentException("Todo not found with id: $id")
        todoRepository.delete(todo)
    }

    /**
     * Todo 엔티티를 TodoResponse DTO로 변환
     * 
     * Todo 엔티티 객체를 클라이언트에게 반환하기 위한 DTO 형태로 변환합니다.
     * 
     * @return 변환된 TodoResponse 객체
     */
    private fun Todo.toResponse(): TodoResponse {
        return TodoResponse(
            id = this.id,
            title = this.title,
            description = this.description,
            isDone = this.isDone
        )
    }
}
