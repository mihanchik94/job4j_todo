package ru.job4j.todo.store;

import lombok.AllArgsConstructor;
import net.jcip.annotations.ThreadSafe;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.User;

import java.util.Optional;

@ThreadSafe
@Repository
@AllArgsConstructor
public class UserStore implements UserRepository {
    private static final Logger LOG = LoggerFactory.getLogger(UserStore.class.getName());
    private SessionFactory sf;

    @Override
    public Optional<User> save(User user) {
        Optional<User> result = Optional.empty();
        Session session = sf.openSession();
        try {
            session.beginTransaction();
            session.save(user);
            result = Optional.of(user);
            session.getTransaction().commit();
        } catch (Exception e) {
            LOG.error("Exception when save user ", e);
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
        return result;
    }

    @Override
    public Optional<User> findByLoginAndPassword(String login, String password) {
        Optional<User> result = Optional.empty();
        Session session = sf.openSession();
        try {
            session.beginTransaction();
            result = session.createQuery("from User u where u.login = :fLogin and u.password = :fPassword", User.class)
                    .setParameter("fLogin", login)
                    .setParameter("fPassword", password)
                    .uniqueResultOptional();
            session.getTransaction().commit();
        } catch (Exception e) {
            LOG.error("Exception when try to find user ", e);
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
        return result;
    }
}