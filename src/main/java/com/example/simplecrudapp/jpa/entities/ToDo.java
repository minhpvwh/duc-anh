package com.example.simplecrudapp.jpa.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
public class ToDo {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "to_do_seq_generator")
    @SequenceGenerator(name = "to_do_seq_generator", sequenceName = "to_do_seq")
    private Long id;

    private String title;
    private String description;
    private LocalDateTime dueDate;
    private boolean completed;
}