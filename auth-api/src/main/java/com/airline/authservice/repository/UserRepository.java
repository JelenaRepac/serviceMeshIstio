package com.airline.authservice.repository;

import com.airline.authservice.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByEmailIgnoreCase(String email);
    Optional<User> findByUsername(String username);

    boolean existsByEmail(String email);

}
