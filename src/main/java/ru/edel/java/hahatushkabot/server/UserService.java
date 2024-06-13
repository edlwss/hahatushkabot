package ru.edel.java.hahatushkabot.server;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.edel.java.hahatushkabot.model.User;
import ru.edel.java.hahatushkabot.model.UserAuthority;
import ru.edel.java.hahatushkabot.model.UserRole;
import ru.edel.java.hahatushkabot.repository.UserRepository;
import ru.edel.java.hahatushkabot.repository.UserRoleRepository;
import ru.edel.java.hahatushkabot.server.inter.UserInter;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService implements UserInter, UserDetailsService {

    private final UserRoleRepository userRolesRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(username));
    }

    @Transactional
    @Override
    public void registration(String username, String password) {
        if (userRepository.findByUsername(username).isEmpty()) {
            User user = userRepository.save(
                    new User()
                            .setId(null)
                            .setUsername(username)
                            .setPassword(passwordEncoder.encode(password))
                            .setLocked(false)
                            .setExpired(false)
                            .setEnabled(true)
            );
            userRolesRepository.save(new UserRole(null, UserAuthority.USER, user));}
    }

    @Override
    public Optional<User> getUserById(Long userId) {
        return userRepository.findById(userId);
    }

    @Transactional
    @Override
    public void updateUserRoles(Long userId, UserAuthority newAuthority) {
        Optional<UserRole> userRoleOptional = userRolesRepository.findByUserId(userId);
        userRoleOptional.ifPresent(userRole -> {
            userRole.setUserAuthority(newAuthority);
            userRolesRepository.save(userRole);
        });
    }
}
