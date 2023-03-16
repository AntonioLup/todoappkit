package com.todoapp.restTodo.dto;

public record TodoDTO(
         Long id,
         String title,
         String category,
         String tags,
         Boolean isCompleted
) {
}
