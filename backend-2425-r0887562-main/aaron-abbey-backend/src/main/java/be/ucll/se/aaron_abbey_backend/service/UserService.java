package be.ucll.se.aaron_abbey_backend.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import be.ucll.se.aaron_abbey_backend.DTO.UserRequest;
import be.ucll.se.aaron_abbey_backend.authentication.JWT.JWTservice;
import be.ucll.se.aaron_abbey_backend.model.User;
import be.ucll.se.aaron_abbey_backend.model.User.Role;
import be.ucll.se.aaron_abbey_backend.repository.UserRepo;
import be.ucll.se.aaron_abbey_backend.util.exception.ServiceException;

@Service
public class UserService {

    @Autowired
    private UserRepo userRepository;

    private final JWTservice jwtService;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserService(UserRepo userRepository, JWTservice jwtService, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
    }

    

    public List<User> getUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public User createUser(User user) {
        return userRepository.save(user);
    }

    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User deleteUser(Long id) throws ServiceException {
        User user = userRepository.findById(id).orElse(null);
        if (user == null) {
            throw new ServiceException("User", "User with id " + id + " does not exist");
        }
        userRepository.deleteById(id);
        return user;
    }

    public User addUser(UserRequest userRequest) throws ServiceException {
        try {
            if (userRepository.findByEmail(userRequest.getEmail()) != null) {
                throw new ServiceException("add", "email.already.exists");
            }

            Role userRole = Role.valueOf(userRequest.getRole());
            boolean roleIsValid = false;

            for (Role role : Role.values()) {
                if (role.equals(userRole)) {
                    roleIsValid = true;
                }
            }

            if (!roleIsValid) {
                throw new ServiceException("Role", "Given user role is invalid.");
            }

            User user = new User(userRequest.getUsername(), userRequest.getPassword(), userRequest.getEmail(),
                    userRole);

            user.setPassword(encodePassword(user.getPassword()));

            return userRepository.save(user);

        } catch (Exception e) {
            throw new ServiceException("User", "Error adding user: " + e.getMessage());
        }
    }

    public String authenticateUser(User user) throws ServiceException {
        User u = userRepository.findByEmail(user.getEmail());

        if (u == null) {
            throw new ServiceException("message", "User not found");
        }

        if (!passwordEncoder.matches(user.getPassword(), u.getPassword())) {
            throw new ServiceException("message", "Invalid credentials");
        }

        return jwtService.createToken(user.getEmail(), u.getRole().toString(), u.getId());
    }

    private String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }

    

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({ MethodArgumentNotValidException.class })
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getFieldErrors().forEach((error) -> {
            String fieldName = error.getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({ ServiceException.class })
    public Map<String, String> handleValidationExceptions(ServiceException ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put(ex.getField(), ex.getMessage());
        return errors;
    }
    
}
