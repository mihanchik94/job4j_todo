package ru.job4j.todo.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class Task {
    private int id;
    private String title;
    private String description;
    private final LocalDateTime created = LocalDateTime.now();
    private boolean done;
}