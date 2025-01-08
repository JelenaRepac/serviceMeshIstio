package com.airline.authapi.Repository;

import com.airline.authapi.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    User findByEmailIgnoreCase(String email);
    boolean existsByEmail(String email);
}
