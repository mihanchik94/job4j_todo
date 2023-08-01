package ru.job4j.todo.store;

import lombok.AllArgsConstructor;
import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.Task;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@ThreadSafe
@Repository
@AllArgsConstructor
public class TaskStore implements TaskRepository {
    private final CrudRepository crudRepository;


    @Override
    public List<Task> findAll() {
        return crudRepository.query("from Task t join fetch t.priority order by t.id", Task.class);
    }

    @Override
    public List<Task> findTasks(boolean status) {
        return crudRepository.query("from Task t join fetch t.priority where t.done = :taskStatus order by t.id", Task.class,
                Map.of("taskStatus", status));
    }

    @Override
    public Task save(Task task) {
        crudRepository.run(session -> session.save(task));
        return task;
    }

    @Override
    public Optional<Task> findById(int id) {
        return crudRepository.optional("from Task t join fetch t.priority where t.id = :fId", Task.class,
                Map.of("fId", id));
    }

    @Override
    public boolean update(Task task) {
        return crudRepository.query("update Task t set t.title = :fTitle, t.description = :fDescription, t.done = :fDone where t.id = :fId",
                Map.of("fTitle", task.getTitle(),
                        "fDescription", task.getDescription(),
                        "fDone", task.isDone(),
                        "fId", task.getId()
                )
        );
    }

    @Override
    public boolean deleteById(int id) {
        return crudRepository.query("delete Task where id = :fId",
                Map.of("fId", id));
    }

    @Override
    public boolean changeDone(Task task) {
        return crudRepository.query("update Task set done = true where id = :fId",
                Map.of("fId", task.getId()));
    }
 }