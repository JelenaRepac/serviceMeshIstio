package com.airline.authservice.service;

import com.airline.authservice.model.Admin;
import com.airline.authservice.model.User;

import java.util.List;

public interface AdminService {

     List<Admin> getAll();

    void addAdmin(User user);

    void deleteAdmin(Integer id);
}
