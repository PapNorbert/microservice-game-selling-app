package edu.ubb.consolegamesales.backend.repository.setup;

import edu.ubb.consolegamesales.backend.model.Role;
import edu.ubb.consolegamesales.backend.model.User;
import edu.ubb.consolegamesales.backend.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Date;

@AllArgsConstructor
@Component
public class AdminUserChecker implements CommandLineRunner {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (userRepository.findByUsername("admin").isEmpty()) {
            // Create an admin user
            User adminUser = new User();
            adminUser.setUsername("admin");
            adminUser.setPassword(passwordEncoder.encode("admin"));
            adminUser.setFirstName("admin");
            adminUser.setLastName("admin");
            adminUser.setAddress("admin");
            adminUser.setRegistrationDate(new Date());
            adminUser.setRole(Role.ADMIN);

            userRepository.saveAndFlush(adminUser);
        }
    }
}
