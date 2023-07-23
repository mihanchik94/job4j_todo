package ru.job4j.todo.store;

import lombok.AllArgsConstructor;
import net.jcip.annotations.ThreadSafe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.User;

import java.util.Map;
import java.util.Optional;

@ThreadSafe
@Repository
@AllArgsConstructor
public class UserStore implements UserRepository {
    private static final Logger LOG = LoggerFactory.getLogger(UserStore.class);
    private final CrudRepository crudRepository;

    @Override
    public Optional<User> save(User user) {
        try {
            crudRepository.run(session -> session.persist(user));
            return Optional.of(user);
        } catch (Exception e) {
            LOG.error("Произошла ошибка при сохранении пользователя " + e);
        }
        return Optional.empty();
    }

    @Override
    public Optional<User> findByLoginAndPassword(String login, String password) {
        return crudRepository.optional("from User u where u.login = :fLogin and u.password = :fPassword", User.class,
                Map.of("fLogin", login,
                        "fPassword", password
                )
        );
    }
}