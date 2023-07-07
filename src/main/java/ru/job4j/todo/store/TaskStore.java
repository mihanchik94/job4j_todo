package ru.job4j.todo.store;

import lombok.AllArgsConstructor;
import net.jcip.annotations.ThreadSafe;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ThreadSafe
@Repository
@AllArgsConstructor
public class TaskStore implements TaskRepository {
    private static final Logger LOG = LoggerFactory.getLogger(TaskStore.class.getName());
    private final SessionFactory sf;

    @Override
    public List<Task> findAll() {
        Session session = sf.openSession();
        List<Task> result = new ArrayList<>();
        try {
            session.beginTransaction();
            result = session.createQuery("from Task order by id", Task.class).list();
            session.getTransaction().commit();
        } catch (Exception e) {
            LOG.error("Exception when find all tasks ", e);
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
        return result;
    }

    @Override
    public List<Task> findTasks(boolean status) {
        Session session = sf.openSession();
        List<Task> result = new ArrayList<>();
        try {
            session.beginTransaction();
            result = session.createQuery("from Task t where t.done = :taskStatus order by id", Task.class)
                    .setParameter("taskStatus", status)
                    .list();
            session.getTransaction().commit();
        } catch (Exception e) {
            LOG.error("Exception when find tasks ", e);
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
        return result;
    }

    @Override
    public Task save(Task task) {
        Session session = sf.openSession();
        try {
            session.beginTransaction();
            session.save(task);
            session.getTransaction().commit();
        } catch (Exception e) {
            LOG.error("Exception when save task ", e);
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
        return task;
    }

    @Override
    public Optional<Task> findById(int id) {
        Session session = sf.openSession();
        Optional<Task> result = Optional.empty();
        try {
            session.beginTransaction();
            result = session.createQuery("from Task t where t.id = :fId", Task.class)
                    .setParameter("fId", id)
                    .uniqueResultOptional();
            session.getTransaction().commit();
        } catch (Exception e) {
            LOG.error("Exception when find task by id ", e);
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
        return result;
    }

    @Override
    public void update(Task task) {
        Session session = sf.openSession();
        try {
            session.beginTransaction();
            session.update(task);
            session.getTransaction().commit();
        } catch (Exception e) {
            LOG.error("Exception when update task ", e);
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
    }

    @Override
    public boolean deleteById(int id) {
        Session session = sf.openSession();
        boolean result = false;
        try {
            session.beginTransaction();
            result = session.createQuery("delete Task where id = :fId")
                    .setParameter("fId", id)
                    .executeUpdate() > 0;
            session.getTransaction().commit();
        } catch (Exception e) {
            LOG.error("Exception when delete task by id ", e);
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
        return result;
    }

    @Override
    public boolean changeDone(Task task) {
        Session session = sf.openSession();
        boolean result = false;
        try {
            session.beginTransaction();
            result = session.createQuery("update Task set done = true where id = :fId")
                    .setParameter("fId", task.getId())
                    .executeUpdate() > 0;
            session.getTransaction().commit();
        } catch (Exception e) {
            LOG.error("Exception when change status of task ", e);
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
        return result;
    }
 }