package ru.job4j.todo.store;

import ru.job4j.todo.model.User;

import java.util.Optional;

public interface UserRepository {
    Optional<User> save(User user);
    Optional<User> findByLoginAndPassword(String login, String password);
}