package com.example.simplecrudapp.services;

import com.example.simplecrudapp.exceptions.ToDoNotFoundException;
import com.example.simplecrudapp.jpa.entities.ToDo;
import com.example.simplecrudapp.jpa.respository.ToDoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.List;

@Service
public class ToDoService {
    private final ToDoRepository toDoRepository;

    @Autowired
    public ToDoService(ToDoRepository toDoRepository) {
        this.toDoRepository = toDoRepository;
    }

    public ToDo createToDo(ToDo toDo) {
        // You can add any additional business logic before saving
        return toDoRepository.save(toDo);
    }

    public List<ToDo> getAllToDos() {
        return toDoRepository.findAll();
    }

    public ToDo getToDoById(Long id) {
        var toDoOpt = toDoRepository.findById(id);
        return toDoOpt.orElseThrow(() ->
                new ToDoNotFoundException(MessageFormat.format("To do with id={0} not found", id)));
    }

    public ToDo updateToDo(Long id, ToDo updatedToDo) {
        if (toDoRepository.existsById(id)) {
            updatedToDo.setId(id);
            return toDoRepository.save(updatedToDo);
        } else {
            // Handle the case where ToDo with the given id is not found
            throw new ToDoNotFoundException(MessageFormat.format("To do with id={0} not found", id));
        }
    }

    public void deleteToDo(Long id) {
        // You can add additional logic before deleting
        toDoRepository.deleteById(id);
    }
}
