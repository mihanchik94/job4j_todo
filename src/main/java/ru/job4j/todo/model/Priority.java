package ru.job4j.todo.model;

import lombok.*;
import lombok.EqualsAndHashCode.Include;

import javax.persistence.*;

@Data
@Entity
@Getter
@Setter
@Table(name = "priorities")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Priority {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Include
    private int id;
    private String name;
    private int position;
}