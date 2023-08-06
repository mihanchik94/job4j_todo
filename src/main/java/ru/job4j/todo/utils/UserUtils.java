package ru.job4j.todo.utils;

import ru.job4j.todo.model.User;

import javax.servlet.http.HttpSession;

public final class UserUtils {
    private UserUtils() {

    }

    public static User getUserFromSession(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            user = new User();
            user.setName("Гость");
        }
        return user;
    }

}
