package be.ucll.se.aaron_abbey_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import be.ucll.se.aaron_abbey_backend.model.User;


@Repository
public interface UserRepo extends JpaRepository<User, Long> {

    User findByEmail(String email);
    
}
