package com.todoapp.restTodo.dao;

import com.todoapp.authRequestJwt.user.UserModel;
import com.todoapp.restTodo.model.Todo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TodoRepo extends JpaRepository<Todo, Long> {

    List<Todo> findByUsername(String username);
}
