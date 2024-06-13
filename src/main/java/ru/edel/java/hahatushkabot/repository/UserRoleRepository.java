package ru.edel.java.hahatushkabot.repository;

import org.springframework.data.repository.CrudRepository;
import ru.edel.java.hahatushkabot.model.UserRole;

import java.util.Optional;

public interface UserRoleRepository extends CrudRepository<UserRole, Long> {

    Optional<UserRole> findByUserId(Long userId);
}
