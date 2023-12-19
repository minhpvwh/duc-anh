package com.example.simplecrudapp.controllers;

import com.example.simplecrudapp.dto.todo.ToDoRequestDTO;
import com.example.simplecrudapp.dto.todo.ToDoResponseDTO;
import com.example.simplecrudapp.jpa.entities.ToDo;
import com.example.simplecrudapp.services.ToDoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/to-do")
@RequiredArgsConstructor
public class ToDoController {

    private final ToDoService toDoService;

    // Create a new ToDo
    @PostMapping
    public ResponseEntity<ToDoResponseDTO> createToDo(@RequestBody ToDoRequestDTO toDoRequestDTO) {
        ToDo toDo = toDoRequestDTO.toToDo(); // Using method for mapping
        ToDo createdToDo = toDoService.createToDo(toDo);
        ToDoResponseDTO responseDTO = new ToDoResponseDTO(createdToDo);
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    // Get all ToDos
    @GetMapping
    public ResponseEntity<List<ToDoResponseDTO>> getAllToDos() {
        List<ToDo> toDos = toDoService.getAllToDos();
        List<ToDoResponseDTO> responseDTOs = toDos.stream().map(ToDoResponseDTO::new).collect(Collectors.toList());
        return new ResponseEntity<>(responseDTOs, HttpStatus.OK);
    }

    // Get a ToDo by ID
    @GetMapping("/{id}")
    public ResponseEntity<ToDoResponseDTO> getToDoById(@PathVariable Long id) {
        ToDo toDo = toDoService.getToDoById(id);
        ToDoResponseDTO responseDTO = new ToDoResponseDTO(toDo);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    // Update a ToDo by ID
    @PutMapping("/{id}")
    public ResponseEntity<ToDoResponseDTO> updateToDo(@PathVariable Long id, @RequestBody ToDoRequestDTO toDoRequestDTO) {
        ToDo toDo = toDoRequestDTO.toToDo(); // Using method for mapping
        ToDo updatedToDo = toDoService.updateToDo(id, toDo);
        ToDoResponseDTO responseDTO = new ToDoResponseDTO(updatedToDo);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    // Delete a ToDo by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteToDo(@PathVariable Long id) {
        toDoService.deleteToDo(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}