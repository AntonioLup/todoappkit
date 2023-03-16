package com.todoapp.restTodo.controller;

import com.todoapp.authRequestJwt.dao.UserRepo;
import com.todoapp.authRequestJwt.service.UserService;
import com.todoapp.authRequestJwt.user.UserModel;
import com.todoapp.restTodo.dao.TodoRepo;
import com.todoapp.restTodo.dto.TodoDTO;
import com.todoapp.restTodo.model.Todo;
import com.todoapp.restTodo.service.TodoService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/")
@RequiredArgsConstructor
@CrossOrigin("http://localhost:8080")
public class TodoController {

    @Autowired
    private final TodoService todoService;

    @Autowired
    private final UserService userService;


    @GetMapping("{username}/todos")
    public ResponseEntity<List<Todo>>  getAllTodos(
            @PathVariable String username) {
        UserModel user = userService.getUserByUsername(username);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        List<Todo> todos = user.getTasks();
        return new ResponseEntity<>(todos, HttpStatus.OK);
    }


    @PostMapping("/{username}/todos")
    public ResponseEntity<Todo> createTaskForUser(
            @PathVariable String username,
            @RequestBody TodoDTO taskdto
    ) {
        UserModel user = userService.getUserByUsername(username);
        List<Todo> todos = user.getTasks();

        if(user == null){
            return ResponseEntity.notFound().build();
        }

        Todo task = Todo.builder()
                .taskId(null)
                .title(taskdto.title())
                .tags(taskdto.tags())
                .category(taskdto.category())
                .createdOn(LocalDate.now())
                .isCompleted(false)
                .build();

        task.setUsername(user);
        todos.add(task);
        Todo createdTask = userService.createTask(task);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{taskId}").buildAndExpand(createdTask.getTaskId()).toUri();
        return ResponseEntity.created(uri).body(createdTask);
    }

    @PutMapping("/{username}/todos/{taskId}")
    public ResponseEntity<Todo> updateTaskForUser(
            @PathVariable String username,
            @PathVariable Long taskId,
            @RequestBody TodoDTO taskdto
    ) {
        UserModel user = userService.getUserByUsername(username);


        if(user == null){
            return ResponseEntity.notFound().build();
        }

        Todo task = todoService.getTaskById(taskId);

        if (task == null) {
            return ResponseEntity.notFound().build();
        }


        task.setTitle(taskdto.title());
        task.setTags(taskdto.tags());
        task.setCategory(taskdto.category());
        task.setIsCompleted(taskdto.isCompleted());


        Todo updateTask = todoService.updateTask(task);

        return ResponseEntity.ok(updateTask);
    }

    @DeleteMapping("/{username}/todos/{taskId}")
    public ResponseEntity<Void> deleteTaskForUser(
            @PathVariable String username,
            @PathVariable Long taskId
    ) {
        UserModel user = userService.getUserByUsername(username);


        if(user == null){
            return ResponseEntity.notFound().build();
        }

        Todo task = todoService.getTaskById(taskId);

        if (task == null) {
            return ResponseEntity.notFound().build();
        }

        todoService.deleteTaskById(taskId);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{username}/todos")
    public ResponseEntity<Void> deleteAllTaskForUser(
            @PathVariable String username

    ) {
        UserModel user = userService.getUserByUsername(username);


        if(user == null){
            return ResponseEntity.notFound().build();
        }


        todoService.deleteAllTaskById(user);

        return ResponseEntity.noContent().build();
    }


}
