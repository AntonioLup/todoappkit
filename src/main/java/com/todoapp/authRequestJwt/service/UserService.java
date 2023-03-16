package com.todoapp.authRequestJwt.service;

import com.todoapp.authRequestJwt.dao.UserRepo;
import com.todoapp.authRequestJwt.user.UserModel;
import com.todoapp.restTodo.dto.TodoDTO;
import com.todoapp.restTodo.model.Todo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    @Autowired
    private final UserRepo userRepo;

    @Transactional
    public UserModel getUserById(Long id) {
        return userRepo.findById(id).orElse(null);
    }
    @Transactional
    public Todo createTask(Todo task) {
        return userRepo
                .save(task.getUsername())
                .getTasks().stream()
                .filter(t -> t.getTitle().equals(task.getTitle()))
                .findFirst().orElse(null);
    }
    @Transactional
    public UserModel getUserByUsername(String username) {
        return userRepo.findByUsername(username).orElse(null);
    }


}
