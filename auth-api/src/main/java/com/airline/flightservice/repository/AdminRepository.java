package com.airline.flightservice.repository;

import com.airline.flightservice.model.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRepository extends JpaRepository<Admin,String> {
    Admin findAdminByUsername(String username);
    boolean existsByUsername(String username);
}