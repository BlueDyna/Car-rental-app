package be.ucll.se.aaron_abbey_backend.integration.database;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import be.ucll.se.aaron_abbey_backend.model.Car;
import be.ucll.se.aaron_abbey_backend.repository.CarRepo;
import jakarta.transaction.Transactional;


@DataJpaTest
@ActiveProfiles("test")
@Transactional
public class CarRepoTest {
     
    @Autowired
    private CarRepo carRepo;

    @Test
    void testSaveCar() {
        // Arrange
        Car car = new Car(
                "Toyota", "Corolla", "Sedan",
                "1-ABC-123", 5, 2,
                true, false, "owner@example.com"
        );

        // Act
        Car savedCar = carRepo.save(car);

        // Assert
        assertThat(savedCar).isNotNull();
        assertThat(savedCar.getId()).isNotNull();
        assertThat(savedCar.getBrand()).isEqualTo("Toyota");
        assertThat(savedCar.getLicensePlate()).isEqualTo("1-ABC-123");
    }

    @Test
    void testFindByLicensePlate() {
        // Arrange
        Car car = new Car(
                "Honda", "Civic", "Hatchback",
                "2-DEF-456", 4, 1,
                false, true, "owner2@example.com"
        );
        carRepo.save(car);

        // Act
        Car foundCar = carRepo.findByLicensePlate("2-DEF-456");

        // Assert
        assertThat(foundCar).isNotNull();
        assertThat(foundCar.getModel()).isEqualTo("Civic");
    }

    @Test
    void testFindAllByOwnerEmail() {
        // Arrange
        carRepo.save(new Car("Ford", "Focus", "SUV", "3-GHI-789", 5, 0, true, true, "owner3@example.com"));
        carRepo.save(new Car("BMW", "X5", "SUV", "4-JKL-012", 5, 1, true, false, "owner3@example.com"));

        // Act
        List<Car> cars = carRepo.findAllByOwnerEmail("owner3@example.com");

        // Assert
        assertThat(cars).isNotEmpty();
        assertThat(cars.size()).isEqualTo(2);
    }

    @Test
    void testFindAllByOwnerEmailWithDifferentOwners() {
        // Arrange
        carRepo.save(new Car("Ford", "Focus", "SUV", "3-GHI-789", 5, 0, true, true, "owner3@example.com"));
        carRepo.save(new Car("BMW", "X5", "SUV", "4-JKL-012", 5, 1, true, false, "owner3@example.com"));
        carRepo.save(new Car("Audi", "A4", "Sedan", "5-MNO-345", 5, 0, false, false, "aaron@test.com"));


        // Act
        List<Car> cars = carRepo.findAllByOwnerEmail("owner3@example.com");

        // Assert
        assertThat(cars).isNotEmpty();
        assertThat(cars.size()).isEqualTo(2);
    }

    @Test
    void testFindAllByOwnerEmailWithDifferentOwners2() {
        // Arrange
        carRepo.save(new Car("Ford", "Focus", "SUV", "3-GHI-789", 5, 0, true, true, "owner3@example.com"));
        carRepo.save(new Car("BMW", "X5", "SUV", "4-JKL-012", 5, 1, true, false, "owner3@example.com"));
        carRepo.save(new Car("Audi", "A4", "Sedan", "5-MNO-345", 5, 0, false, false, "aaron@test.com"));


        // Act
        List<Car> cars = carRepo.findAllByOwnerEmail("aaron@test.com");

        // Assert
        assertThat(cars).isNotEmpty();
        assertThat(cars.size()).isEqualTo(1);
    }
    

    @Test
    void testFindById() {
        // Arrange
        Car car = new Car(
                "Tesla", "Model 3", "Electric",
                "5-MNO-345", 5, 0,
                false, false, "owner4@example.com"
        );
        Car savedCar = carRepo.save(car);

        // Act
        Optional<Car> foundCar = carRepo.findById(savedCar.getId());

        // Assert
        assertThat(foundCar).isPresent();
        assertThat(foundCar.get().getBrand()).isEqualTo("Tesla");
    }

    @Test
    void testDeleteCar() {
        // Arrange
        Car car = new Car(
                "Mazda", "CX-5", "SUV",
                "6-PQR-678", 5, 1,
                true, true, "owner5@example.com"
        );
        Car savedCar = carRepo.save(car);

        // Act
        carRepo.deleteById(savedCar.getId());

        // Assert
        Optional<Car> deletedCar = carRepo.findById(savedCar.getId());
        assertThat(deletedCar).isEmpty();
    }
    
}
