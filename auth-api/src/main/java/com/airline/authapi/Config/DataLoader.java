package com.airline.authapi.Config;

import org.springframework.context.annotation.Configuration;

@Configuration
public class DataLoader {
//
//    @Bean(name = "authDataLoader")
//    public CommandLineRunner dataLoader(UserRepository userRepository) {
//        return args -> {
//            if (userRepository.count() == 0) {
//                for (int i = 1; i <= 10; i++) {
//                    User user = User.builder()
//                            .email(i+"@gmail.com")
//                            .firstname("Jelena"+i)
//                            .lastname("Repac")
//                            .rank("SILVER")
//                            .passportNumber("55ds6dsd6s").build();
//
//                    userRepository.save(user);
//                }
//                System.out.println("10 users inserted.");
//            } else {
//                System.out.println("Users already exist.");
//            }
//        };
//    }
}