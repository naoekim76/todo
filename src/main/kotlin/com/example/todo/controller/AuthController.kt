package com.example.todo.controller

import com.example.todo.dto.LoginRequest
import com.example.todo.dto.SignupRequest
import com.example.todo.service.UserService
import jakarta.servlet.http.HttpSession
import jakarta.validation.Valid
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.mvc.support.RedirectAttributes

/**
 * 인증 관련 컨트롤러
 * 
 * 회원가입, 로그인, 로그아웃 등 사용자 인증 관련 요청을 처리합니다.
 * 
 * @param userService 사용자 서비스 (회원가입, 로그인 처리 등)
 */
@Controller
class AuthController(
    private val userService: UserService
) {

    /**
     * 회원가입 폼 페이지 제공
     * 
     * GET /signup 요청을 처리하여 회원가입 폼 페이지를 반환합니다.
     * 
     * @param model 뷰에 전달할 데이터를 담는 모델 객체
     * @return 회원가입 페이지 이름 ("signup")
     */
    @GetMapping("/signup")
    fun signupForm(model: Model): String {
        model.addAttribute("signupRequest", SignupRequest("", "", ""))
        return "signup"
    }

    /**
     * 회원가입 처리
     * 
     * POST /signup 요청을 처리하여 사용자 회원가입을 수행합니다.
     * 
     * @param signupRequest 회원가입 요청 데이터 (이메일, 비밀번호, 닉네임)
     * @param bindingResult 입력값 검증 결과
     * @param model 뷰에 전달할 데이터를 담는 모델 객체
     * @return 성공 시 로그인 페이지로 리다이렉트, 실패 시 회원가입 페이지 유지
     */
    @PostMapping("/signup")
    fun signup(
        @Valid @ModelAttribute signupRequest: SignupRequest,
        bindingResult: BindingResult,
        model: Model
    ): String {
        // 입력값 검증 실패 시 회원가입 페이지로 돌아감
        if (bindingResult.hasErrors()) {
            return "signup"
        }

        return try {
            // 회원가입 처리
            userService.signup(signupRequest)
            // 성공 시 로그인 페이지로 리다이렉트
            "redirect:/login"
        } catch (e: IllegalArgumentException) {
            // 실패 시 (이메일 중복 등) 에러 메시지 표시
            model.addAttribute("error", e.message)
            "signup"
        }
    }

    /**
     * 로그인 폼 페이지 제공
     * 
     * GET /login 요청을 처리하여 로그인 폼 페이지를 반환합니다.
     * 
     * @param model 뷰에 전달할 데이터를 담는 모델 객체
     * @return 로그인 페이지 이름 ("login")
     */
    @GetMapping("/login")
    fun loginForm(model: Model): String {
        model.addAttribute("loginRequest", LoginRequest("", ""))
        return "login"
    }

    /**
     * 로그인 처리
     * 
     * POST /login 요청을 처리하여 사용자 로그인을 수행합니다.
     * 
     * @param loginRequest 로그인 요청 데이터 (이메일, 비밀번호)
     * @param bindingResult 입력값 검증 결과
     * @param model 뷰에 전달할 데이터를 담는 모델 객체
     * @param session HTTP 세션 객체 (로그인 상태 저장)
     * @return 성공 시 Todo 목록 페이지로 리다이렉트, 실패 시 로그인 페이지 유지
     */
    @PostMapping("/login")
    fun login(
        @Valid @ModelAttribute loginRequest: LoginRequest,
        bindingResult: BindingResult,
        model: Model,
        session: HttpSession
    ): String {
        // 입력값 검증 실패 시 로그인 페이지로 돌아감
        if (bindingResult.hasErrors()) {
            return "login"
        }

        return try {
            // 로그인 처리
            val user = userService.login(loginRequest)
            // 세션에 사용자 정보 저장
            session.setAttribute("userId", user.id)
            session.setAttribute("userNickname", user.nickname)
            // 성공 시 Todo 목록 페이지로 리다이렉트
            "redirect:/todos"
        } catch (e: IllegalArgumentException) {
            // 실패 시 (이메일/비밀번호 불일치 등) 에러 메시지 표시
            model.addAttribute("error", e.message)
            "login"
        }
    }

    /**
     * 로그아웃 처리
     * 
     * POST /logout 요청을 처리하여 사용자 로그아웃을 수행합니다.
     * 
     * @param session HTTP 세션 객체 (무효화 처리)
     * @return 로그인 페이지로 리다이렉트
     */
    @PostMapping("/logout")
    fun logout(session: HttpSession): String {
        // 세션 무효화 (모든 세션 데이터 삭제)
        session.invalidate()
        return "redirect:/login"
    }

    /**
     * 홈 페이지 리다이렉트
     * 
     * 루트 경로(/) 요청을 Todo 목록 페이지로 리다이렉트합니다.
     * 
     * @return Todo 목록 페이지로 리다이렉트
     */
    @GetMapping("/")
    fun home(): String {
        return "redirect:/todos"
    }
}
