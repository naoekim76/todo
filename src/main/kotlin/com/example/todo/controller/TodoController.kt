package com.example.todo.controller

import com.example.todo.dto.TodoRequest
import com.example.todo.dto.TodoResponse
import com.example.todo.service.TodoService
import jakarta.servlet.http.HttpSession
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

/**
 * Todo REST API 컨트롤러
 * 
 * Todo 항목에 대한 CRUD 작업을 위한 REST API 엔드포인트를 제공합니다.
 * 모든 응답은 JSON 형식으로 반환됩니다.
 * 
 * @param todoService Todo 서비스 (비즈니스 로직 처리)
 */
@RestController
@RequestMapping("/api/todos")
class TodoController(
    private val todoService: TodoService
) {

    /**
     * Todo 생성 API
     * 
     * POST /api/todos 요청을 처리하여 새로운 Todo 항목을 생성합니다.
     * 
     * @param request Todo 생성 요청 데이터 (제목, 설명)
     * @return 생성된 Todo 정보와 201 Created 상태 코드
     */
    @PostMapping
    fun createTodo(@Valid @RequestBody request: TodoRequest, session: HttpSession): ResponseEntity<TodoResponse> {
        val userId = session.getAttribute("userId") as? Long
            ?: return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()
        
        val todoResponse = todoService.createTodo(request, userId)
        return ResponseEntity.status(HttpStatus.CREATED).body(todoResponse)
    }

    /**
     * 모든 Todo 조회 API
     * 
     * GET /api/todos 요청을 처리하여 현재 사용자의 모든 Todo 항목을 조회합니다.
     * 
     * @return Todo 목록과 200 OK 상태 코드
     */
    @GetMapping
    fun getAllTodos(session: HttpSession): ResponseEntity<List<TodoResponse>> {
        val userId = session.getAttribute("userId") as? Long
            ?: return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()
        
        val todos = todoService.getAllTodos(userId)
        return ResponseEntity.ok(todos)
    }

    /**
     * 특정 Todo 조회 API
     * 
     * GET /api/todos/{id} 요청을 처리하여 특정 ID의 Todo 항목을 조회합니다.
     * 
     * @param id 조회할 Todo의 ID
     * @return 찾은 경우 Todo 정보와 200 OK 상태 코드, 찾지 못한 경우 404 Not Found 상태 코드
     */
    @GetMapping("/{id}")
    fun getTodoById(@PathVariable id: Long, session: HttpSession): ResponseEntity<TodoResponse> {
        val userId = session.getAttribute("userId") as? Long
            ?: return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()
        
        return try {
            val todo = todoService.getTodoById(id, userId)
            ResponseEntity.ok(todo)
        } catch (e: IllegalArgumentException) {
            // Todo를 찾지 못한 경우 404 반환
            ResponseEntity.notFound().build()
        }
    }

    /**
     * Todo 업데이트 API
     * 
     * PUT /api/todos/{id} 요청을 처리하여 특정 ID의 Todo 항목을 업데이트합니다.
     * 
     * @param id 업데이트할 Todo의 ID
     * @param request 업데이트할 Todo 데이터 (제목, 설명, 완료 상태)
     * @return 업데이트된 Todo 정보와 200 OK 상태 코드, 찾지 못한 경우 404 Not Found 상태 코드
     */
    @PutMapping("/{id}")
    fun updateTodo(
        @PathVariable id: Long,
        @Valid @RequestBody request: TodoRequest,
        session: HttpSession
    ): ResponseEntity<TodoResponse> {
        val userId = session.getAttribute("userId") as? Long
            ?: return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()
        
        return try {
            val updatedTodo = todoService.updateTodo(id, request, userId)
            ResponseEntity.ok(updatedTodo)
        } catch (e: IllegalArgumentException) {
            // Todo를 찾지 못한 경우 404 반환
            ResponseEntity.notFound().build()
        }
    }

    /**
     * Todo 삭제 API
     * 
     * DELETE /api/todos/{id} 요청을 처리하여 특정 ID의 Todo 항목을 삭제합니다.
     * 
     * @param id 삭제할 Todo의 ID
     * @return 성공 시 204 No Content 상태 코드, 찾지 못한 경우 404 Not Found 상태 코드
     */
    @DeleteMapping("/{id}")
    fun deleteTodo(@PathVariable id: Long, session: HttpSession): ResponseEntity<Void> {
        val userId = session.getAttribute("userId") as? Long
            ?: return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()
        
        return try {
            todoService.deleteTodo(id, userId)
            // 성공적으로 삭제된 경우 204 반환 (응답 본문 없음)
            ResponseEntity.noContent().build()
        } catch (e: IllegalArgumentException) {
            // Todo를 찾지 못한 경우 404 반환
            ResponseEntity.notFound().build()
        }
    }
}
