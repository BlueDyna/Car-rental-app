package be.ucll.se.aaron_abbey_backend.util.initializer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import be.ucll.se.aaron_abbey_backend.authentication.config.PasswordEncoderConfig;
import be.ucll.se.aaron_abbey_backend.model.User;
import be.ucll.se.aaron_abbey_backend.repository.UserRepo;


@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepo userRepository;

    @Autowired
    private PasswordEncoderConfig passwordEncoder;

    @Override
    // @Transactional
    public void run(String... args) {
        String adminEmail = "aaron.abbey02@gmail.com";
        
        // Check if admin user already exists
        if (userRepository.findByEmail(adminEmail) == null) {
            User admin = new User("Admin", "admin123", adminEmail, User.Role.Admin);
            admin.setPassword(passwordEncoder.passwordEncoder().encode(admin.getPassword()));
            userRepository.save(admin);
            System.out.println("Admin user created successfully.");
        } else {
            System.out.println("Admin user already exists.");
        }
    }
}