package ru.edel.java.hahatushkabot.repository;
import org.springframework.data.repository.CrudRepository;
import java.util.Optional;
import ru.edel.java.hahatushkabot.model.User;
public interface UserRepository extends CrudRepository<User, Long> {
    Optional<User> findByUsername(String username);
}