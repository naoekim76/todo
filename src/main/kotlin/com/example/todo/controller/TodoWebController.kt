package com.example.todo.controller

import com.example.todo.dto.TodoRequest
import com.example.todo.service.TodoService
import jakarta.servlet.http.HttpSession
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*

/**
 * Todo 웹 페이지 컨트롤러
 * 
 * Todo 관련 웹 페이지 요청을 처리하는 컨트롤러입니다.
 * 사용자 인증 상태를 확인하고 세션에서 사용자 ID를 가져와 Todo 서비스에 전달합니다.
 * 
 * @param todoService Todo 서비스 (비즈니스 로직 처리)
 */
@Controller
@RequestMapping("/todos")
class TodoWebController(
    private val todoService: TodoService
) {

    /**
     * Todo 목록 페이지 제공
     * 
     * GET /todos 요청을 처리하여 현재 로그인한 사용자의 Todo 목록 페이지를 반환합니다.
     * 
     * @param model 뷰에 전달할 데이터를 담는 모델 객체
     * @param session HTTP 세션 객체 (사용자 인증 정보 확인)
     * @return 로그인 상태면 Todo 목록 페이지, 비로그인 상태면 로그인 페이지로 리다이렉트
     */
    @GetMapping
    fun list(model: Model, session: HttpSession): String {
        // 세션에서 사용자 ID 가져오기 (없으면 로그인 페이지로 리다이렉트)
        val userId = session.getAttribute("userId") as? Long
            ?: return "redirect:/login"
        val userNickname = session.getAttribute("userNickname") as? String

        // 사용자의 Todo 목록 조회
        val todos = todoService.getAllTodos(userId)
        model.addAttribute("todos", todos)
        model.addAttribute("userNickname", userNickname)
        return "list"
    }

    /**
     * Todo 생성 폼 페이지 제공
     * 
     * GET /todos/create 요청을 처리하여 Todo 생성 폼 페이지를 반환합니다.
     * 
     * @param model 뷰에 전달할 데이터를 담는 모델 객체
     * @param session HTTP 세션 객체 (사용자 인증 정보 확인)
     * @return 로그인 상태면 Todo 생성 폼 페이지, 비로그인 상태면 로그인 페이지로 리다이렉트
     */
    @GetMapping("/create")
    fun createForm(model: Model, session: HttpSession): String {
        // 세션에서 사용자 ID 가져오기 (없으면 로그인 페이지로 리다이렉트)
        val userId = session.getAttribute("userId") as? Long
            ?: return "redirect:/login"

        // 빈 Todo 요청 객체 생성
        model.addAttribute("todoRequest", TodoRequest("", "", false))
        return "create"
    }

    /**
     * Todo 생성 처리
     * 
     * POST /todos/create 요청을 처리하여 새로운 Todo 항목을 생성합니다.
     * 
     * @param todoRequest Todo 생성 요청 데이터 (제목, 설명, 완료 상태)
     * @param session HTTP 세션 객체 (사용자 인증 정보 확인)
     * @return 로그인 상태면 Todo 목록 페이지로 리다이렉트, 비로그인 상태면 로그인 페이지로 리다이렉트
     */
    @PostMapping("/create")
    fun create(@ModelAttribute todoRequest: TodoRequest, session: HttpSession): String {
        // 세션에서 사용자 ID 가져오기 (없으면 로그인 페이지로 리다이렉트)
        val userId = session.getAttribute("userId") as? Long
            ?: return "redirect:/login"

        // Todo 생성
        todoService.createTodo(todoRequest, userId)
        return "redirect:/todos"
    }

    /**
     * Todo 완료 상태 토글 처리
     * 
     * POST /todos/{id}/toggle 요청을 처리하여 특정 Todo 항목의 완료 상태를 변경합니다.
     * 
     * @param id 상태를 변경할 Todo의 ID
     * @param session HTTP 세션 객체 (사용자 인증 정보 확인)
     * @return 로그인 상태면 Todo 목록 페이지로 리다이렉트, 비로그인 상태면 로그인 페이지로 리다이렉트
     */
    @PostMapping("/{id}/toggle")
    fun toggleDone(@PathVariable id: Long, session: HttpSession): String {
        // 세션에서 사용자 ID 가져오기 (없으면 로그인 페이지로 리다이렉트)
        val userId = session.getAttribute("userId") as? Long
            ?: return "redirect:/login"

        // 현재 Todo 정보 조회
        val todo = todoService.getTodoById(id, userId)
        // 완료 상태를 반전시킨 업데이트 요청 생성
        val updatedRequest = TodoRequest(
            title = todo.title,
            description = todo.description,
            isDone = !todo.isDone
        )
        // Todo 업데이트
        todoService.updateTodo(id, updatedRequest, userId)
        return "redirect:/todos"
    }

    /**
     * Todo 수정 폼 페이지 제공
     * 
     * GET /todos/{id}/edit 요청을 처리하여 Todo 수정 폼 페이지를 반환합니다.
     * 
     * @param id 수정할 Todo의 ID
     * @param model 뷰에 전달할 데이터를 담는 모델 객체
     * @param session HTTP 세션 객체 (사용자 인증 정보 확인)
     * @return 로그인 상태면 Todo 수정 폼 페이지, 비로그인 상태면 로그인 페이지로 리다이렉트
     */
    @GetMapping("/{id}/edit")
    fun editForm(@PathVariable id: Long, model: Model, session: HttpSession): String {
        // 세션에서 사용자 ID 가져오기 (없으면 로그인 페이지로 리다이렉트)
        val userId = session.getAttribute("userId") as? Long
            ?: return "redirect:/login"

        // 기존 Todo 정보 조회하여 폼에 채워넣기
        val todo = todoService.getTodoById(id, userId)
        val todoRequest = TodoRequest(
            title = todo.title,
            description = todo.description ?: "",
            isDone = todo.isDone
        )
        model.addAttribute("todoRequest", todoRequest)
        model.addAttribute("todoId", id)
        return "edit"
    }

    /**
     * Todo 수정 처리
     * 
     * POST /todos/{id}/edit 요청을 처리하여 기존 Todo 항목을 수정합니다.
     * 
     * @param id 수정할 Todo의 ID
     * @param todoRequest Todo 수정 요청 데이터 (제목, 설명, 완료 상태)
     * @param session HTTP 세션 객체 (사용자 인증 정보 확인)
     * @return 로그인 상태면 Todo 목록 페이지로 리다이렉트, 비로그인 상태면 로그인 페이지로 리다이렉트
     */
    @PostMapping("/{id}/edit")
    fun edit(@PathVariable id: Long, @ModelAttribute todoRequest: TodoRequest, session: HttpSession): String {
        // 세션에서 사용자 ID 가져오기 (없으면 로그인 페이지로 리다이렉트)
        val userId = session.getAttribute("userId") as? Long
            ?: return "redirect:/login"

        // Todo 수정
        todoService.updateTodo(id, todoRequest, userId)
        return "redirect:/todos"
    }

    /**
     * Todo 삭제 처리
     * 
     * POST /todos/{id}/delete 요청을 처리하여 특정 Todo 항목을 삭제합니다.
     * 
     * @param id 삭제할 Todo의 ID
     * @param session HTTP 세션 객체 (사용자 인증 정보 확인)
     * @return 로그인 상태면 Todo 목록 페이지로 리다이렉트, 비로그인 상태면 로그인 페이지로 리다이렉트
     */
    @PostMapping("/{id}/delete")
    fun delete(@PathVariable id: Long, session: HttpSession): String {
        // 세션에서 사용자 ID 가져오기 (없으면 로그인 페이지로 리다이렉트)
        val userId = session.getAttribute("userId") as? Long
            ?: return "redirect:/login"

        // Todo 삭제
        todoService.deleteTodo(id, userId)
        return "redirect:/todos"
    }
}
