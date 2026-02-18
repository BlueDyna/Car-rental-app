package be.ucll.se.aaron_abbey_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import be.ucll.se.aaron_abbey_backend.model.Rent;
import java.util.List;
import java.util.Optional;

@Repository
public interface RentRepo extends JpaRepository<Rent, Long> {
    Optional<Rent> findById(Long id); 
    
    List<Rent> findAllByRenterMail(String email);

    List<Rent> findAllByRentalId(long rentalId);
}
