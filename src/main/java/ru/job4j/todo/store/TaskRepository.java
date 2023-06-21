package ru.job4j.todo.store;

import ru.job4j.todo.model.Task;

import java.util.List;
import java.util.Optional;

public interface TaskRepository {
    List<Task> findAll();
    List<Task> findNewTasks();
    List<Task> findDoneTasks();
    Task save(Task task);
    Optional<Task> findById(int id);
    boolean update(Task task);
    boolean deleteById(int id);
    boolean changeDone(Task task);
}