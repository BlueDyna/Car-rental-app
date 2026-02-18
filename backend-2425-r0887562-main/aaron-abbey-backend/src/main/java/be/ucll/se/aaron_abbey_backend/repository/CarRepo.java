package be.ucll.se.aaron_abbey_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import be.ucll.se.aaron_abbey_backend.model.Car;
import java.util.List;
import java.util.Optional;

@Repository
public interface CarRepo extends JpaRepository<Car, Long> {
    
    Optional<Car> findById(Long id);

    Car findByLicensePlate(String licensePlate);

    List<Car> findAllByOwnerEmail(String email);
       
    
}
