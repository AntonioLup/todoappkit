package com.todoapp.restTodo.service;

import com.todoapp.authRequestJwt.dao.UserRepo;
import com.todoapp.authRequestJwt.user.UserModel;
import com.todoapp.restTodo.dao.TodoRepo;
import com.todoapp.restTodo.exceptions.UserNotFoundException;
import com.todoapp.restTodo.model.Todo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TodoService {
    @Autowired
    private final TodoRepo todoRepo;
    @Autowired
    private final UserRepo userRepo;
    @Transactional
    public Todo createTaskForUser(String username, Todo task) {
        UserModel user = userRepo.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));
        task.setUsername(user);
        return todoRepo.save(task);
    }
    @Transactional
    public List<Todo> getAllTasksForUser(String username) {
        List<Todo> todos = todoRepo.findByUsername(username);
        if (todos.isEmpty()) {
            throw new UserNotFoundException();
        }
        return todos;
    }
    @Transactional
    public Todo getTaskById(Long taskId) {
        Optional<Todo> optionalTask = todoRepo.findById(taskId);
        return optionalTask.orElse(null);
    }
    @Transactional
    public Todo updateTask(Todo task) {
        return todoRepo.save(task);
    }
    @Transactional
    public void deleteTaskById(Long taskId) {
        todoRepo.deleteById(taskId);
    }

    public void deleteAllTaskById(UserModel user) {
        List<Todo> todos = user.getTasks();
        todoRepo.deleteAll(todos);
    }

    public List<Todo> getCompletedTasksByUser(UserModel user) {
        List<Todo> todos = user.getTasks();
        List<Todo> completedTasks = todos.stream()
                .filter(todo -> todo.getIsCompleted())
                .collect(Collectors.toList());
        return completedTasks;
    }

    public void updateTaskCompletion(Long taskId, boolean isCompleted) {
        Optional<Todo> task = todoRepo.findById(taskId);
        if (task.isPresent()) {
            Todo updatedTask = task.get();
            updatedTask.setIsCompleted(isCompleted);
            todoRepo.save(updatedTask);
        } else {
            throw new UserNotFoundException("Task not found with ID: " + taskId);
        }
    }

    public void completeTask(Long taskId, boolean isCompleted) {
        Optional<Todo> taskOptional = todoRepo.findById(taskId);
        if (taskOptional.isPresent()) {
            Todo task = taskOptional.get();
            task.setIsCompleted(isCompleted);
            todoRepo.save(task);
        } else {
            throw new UserNotFoundException("Task not found with ID: " + taskId);
        }
    }
}
