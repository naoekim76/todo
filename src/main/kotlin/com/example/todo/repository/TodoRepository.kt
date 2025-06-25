package com.example.todo.repository

import com.example.todo.entity.Todo
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

/**
 * Todo 레포지토리 인터페이스
 * 
 * Todo 엔티티에 대한 데이터 액세스 기능을 제공하는 JPA 레포지토리 인터페이스입니다.
 * JpaRepository를 상속받아 기본적인 CRUD 기능(findAll, findById, save, delete 등)을 제공합니다.
 * 사용자별 데이터 격리를 위한 추가 메서드를 정의합니다.
 */
@Repository
interface TodoRepository : JpaRepository<Todo, Long> {
    /**
     * 특정 사용자의 모든 Todo 항목 조회
     * 
     * 주어진 사용자 ID에 해당하는 모든 Todo 항목을 조회합니다.
     * 
     * @param userId 조회할 사용자의 ID
     * @return 해당 사용자의 Todo 목록
     */
    fun findByUserId(userId: Long): List<Todo>

    /**
     * 특정 ID와 사용자 ID에 해당하는 Todo 항목 조회
     * 
     * 주어진 Todo ID와 사용자 ID에 해당하는 Todo 항목을 조회합니다.
     * 사용자별 데이터 격리를 위해 사용되며, 다른 사용자의 Todo에 접근하는 것을 방지합니다.
     * 
     * @param id 조회할 Todo의 ID
     * @param userId 조회할 사용자의 ID
     * @return 조건에 맞는 Todo 항목, 없으면 null
     */
    fun findByIdAndUserId(id: Long, userId: Long): Todo?
}
