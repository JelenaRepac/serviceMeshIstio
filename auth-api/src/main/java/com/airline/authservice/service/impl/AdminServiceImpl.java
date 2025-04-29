package com.airline.authservice.service.impl;

import com.airline.authservice.model.Admin;
import com.airline.authservice.model.User;
import com.airline.authservice.repository.AdminRepository;
import com.airline.authservice.service.AdminService;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class AdminServiceImpl implements AdminService {

    private final AdminRepository adminRepository;

    public AdminServiceImpl(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }

    @Override
    public List<Admin> getAll() {
        return adminRepository.findAll();
    }

    @Override
    public void addAdmin(User user) {
            adminRepository.save(Admin.builder().username(user.getEmail()).password(user.getPassword()).build());

    }

    @Override
    public void deleteAdmin(Integer id) {
        adminRepository.deleteById(id);

    }
}
