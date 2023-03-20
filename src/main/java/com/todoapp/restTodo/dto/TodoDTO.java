package com.todoapp.restTodo.dto;

import java.time.LocalDateTime;

public record TodoDTO(
         Long id,
         String title,
         String category,
         String tags,
         Boolean isCompleted,
         LocalDateTime datepick
) {
}
