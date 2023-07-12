package ru.job4j.todo.service;

import lombok.AllArgsConstructor;
import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Service;
import ru.job4j.todo.model.Task;
import ru.job4j.todo.store.TaskRepository;

import java.util.List;
import java.util.Optional;

@ThreadSafe
@Service
@AllArgsConstructor
public class SimpleTaskService implements TaskService {
    private final TaskRepository taskStore;

    @Override
    public List<Task> findAll() {
        return taskStore.findAll();
    }

    @Override
    public List<Task> findTasks(boolean status) {
        return taskStore.findTasks(status);
    }

    @Override
    public Task save(Task task) {
        return taskStore.save(task);
    }

    @Override
    public Optional<Task> findById(int id) {
        return taskStore.findById(id);
    }

    @Override
    public boolean update(Task task) {
        return taskStore.update(task);
    }

    @Override
    public boolean deleteById(int id) {
        return taskStore.deleteById(id);
    }

    @Override
    public boolean changeDone(Task task) {
        return taskStore.changeDone(task);
    }
}