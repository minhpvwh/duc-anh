package com.example.simplecrudapp.jpa.respository;

import com.example.simplecrudapp.jpa.entities.ToDo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ToDoRepository extends JpaRepository<ToDo, Long> {
}