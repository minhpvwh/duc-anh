package com.example.simplecrudapp.dto.todo;

import com.example.simplecrudapp.jpa.entities.ToDo;

import java.time.LocalDateTime;

public record ToDoResponseDTO(Long id, String title, String description, LocalDateTime dueDate, boolean completed) {

    public ToDoResponseDTO(ToDo toDo) {
        this(toDo.getId(), toDo.getTitle(), toDo.getDescription(), toDo.getDueDate(), toDo.isCompleted());
    }
}
