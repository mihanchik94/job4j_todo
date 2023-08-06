package ru.job4j.todo.service;

import lombok.AllArgsConstructor;
import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Service;
import ru.job4j.todo.model.Task;
import ru.job4j.todo.store.TaskRepository;

import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.TimeZone;

@ThreadSafe
@Service
@AllArgsConstructor
public class SimpleTaskService implements TaskService {
    private final TaskRepository taskStore;

    @Override
    public List<Task> findAll(String zoneId) {
        List<Task> result = taskStore.findAll();
        shiftTimeZone(result, zoneId);
        return result;
    }

    @Override
    public List<Task> findTasks(boolean status, String zoneId) {
        List<Task> result = taskStore.findTasks(status);
        shiftTimeZone(result, zoneId);
        return result;
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

    private void shiftTimeZone(List<Task> tasks, String zoneId) {
        tasks.forEach(task -> task.setCreated(
                task.getCreated()
                        .atZone(TimeZone.getDefault().toZoneId())
                        .withZoneSameInstant(ZoneId.of(zoneId))
                        .toLocalDateTime()
        ));
    }
}