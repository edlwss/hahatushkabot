package ru.edel.java.hahatushkabot.server.inter;

import ru.edel.java.hahatushkabot.model.User;
import ru.edel.java.hahatushkabot.model.UserAuthority;

import java.util.Optional;

public interface UserInter {
    void registration(String username, String password);
    Optional<User> getUserById(Long userId);
    void updateUserRoles(Long userId, UserAuthority newAuthority);
}
