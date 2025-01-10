package com.airline.flightservice.runner;

import com.airline.flightservice.model.Admin;
import com.airline.flightservice.repository.AdminRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Profile({"default"})
@Component
public class DataRunner implements CommandLineRunner {

    private AdminRepository adminRepository;

    public DataRunner(AdminRepository adminRepository){
        this.adminRepository=adminRepository;
    }

    @Override
    public void run(String... args) throws Exception {


        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encodedPassword = encoder.encode("Jeca0");

        Admin admin = Admin.builder().username("repac01jelena@gmail.com").password(encodedPassword).build();

        adminRepository.save(admin);


    }
}
