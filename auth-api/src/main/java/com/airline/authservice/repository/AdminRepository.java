package com.airline.authservice.repository;

import com.airline.authservice.model.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRepository extends JpaRepository<Admin,Integer> {
    Admin findAdminByUsername(String username);
    boolean existsByUsername(String username);
}