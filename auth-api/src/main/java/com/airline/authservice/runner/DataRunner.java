package com.airline.authservice.runner;

import com.airline.authservice.model.Admin;
import com.airline.authservice.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Profile({"default"})
@Component
public class DataRunner implements CommandLineRunner {

  @Autowired
  private AdminRepository adminRepository;

    @Override
    public void run(String... args) throws Exception {


        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encodedPassword = encoder.encode("Jeca0");

        Admin admin = Admin.builder().username("repacjelena@gmail.com").password(encodedPassword).build();

        adminRepository.save(admin);


    }
}
