package com.example.simplecrudapp.dto.todo;

import com.example.simplecrudapp.jpa.entities.ToDo;

import java.time.LocalDateTime;

public record ToDoRequestDTO(String title, String description, LocalDateTime dueDate, boolean completed) {

    public ToDo toToDo() {
        ToDo toDo = new ToDo();
        toDo.setTitle(this.title);
        toDo.setDescription(this.description);
        toDo.setDueDate(this.dueDate);
        toDo.setCompleted(this.completed);
        return toDo;
    }
}