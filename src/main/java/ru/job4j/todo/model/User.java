package ru.job4j.todo.model;

import lombok.*;

import javax.persistence.*;
import java.util.TimeZone;

@Entity
@Table(name = "users")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private int id;
    private String name;
    private String login;
    private String password;
    private String timezone = TimeZone.getDefault().getID();;
}