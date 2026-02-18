package be.ucll.se.aaron_abbey_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import be.ucll.se.aaron_abbey_backend.model.Rental;
@Repository
public interface RentalRepo extends JpaRepository<Rental, Long> {

   
} 