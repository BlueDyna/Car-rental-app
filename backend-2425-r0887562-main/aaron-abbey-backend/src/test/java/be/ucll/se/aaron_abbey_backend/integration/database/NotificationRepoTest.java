// package be.ucll.se.aaron_abbey_backend.integration.database;

// import static org.junit.Assert.assertEquals;
// import static org.mockito.Mockito.when;
// import static org.assertj.core.api.Assertions.assertThat;

// import java.util.Calendar;
// import java.util.Date;
// import java.util.List;

// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.DisplayName;
// import org.junit.jupiter.api.Test;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
// import org.springframework.test.context.ActiveProfiles;

// import be.ucll.se.aaron_abbey_backend.model.Car;
// import be.ucll.se.aaron_abbey_backend.model.Notification;
// import be.ucll.se.aaron_abbey_backend.model.Rent;
// import be.ucll.se.aaron_abbey_backend.model.Rental;
// import be.ucll.se.aaron_abbey_backend.repository.CarRepo;
// import be.ucll.se.aaron_abbey_backend.repository.NotificationRepo;
// import be.ucll.se.aaron_abbey_backend.repository.RentRepo;
// import be.ucll.se.aaron_abbey_backend.repository.RentalRepo;
// import be.ucll.se.aaron_abbey_backend.util.exception.ServiceException;
// import jakarta.transaction.Transactional;

// @DataJpaTest
// @ActiveProfiles("test")
// @Transactional
// public class NotificationRepoTest {

//     @Autowired
//     private NotificationRepo notificationRepo;

//     @Autowired
//     private RentRepo rentRepo;

//     @Autowired
//     private CarRepo carRepo;

//     @Autowired
//     private RentalRepo rentalRepo;


//     private Rent rent2;
//     private Rental rental;
//     private Car car;

//     @BeforeEach
//     void setUp() throws ServiceException {
//         // Create a Car
//         car = new Car();
//         car.setBrand("Toyota");
//         car.setModel("Corolla");
//         car.setType("Sedan");
//         car.setLicensePlate("1-ABC-123");
//         car.setOwnerEmail("owner@example.com");
//         car.setNumberOfSeats(5);
//         car.setNumberOfChildSeats(2);
//         car.setHaveFoldingRearSeats(true);
//         car.setHasTowBar(false);

//         carRepo.save(car);


//         Calendar calendar = Calendar.getInstance();
//         calendar.add(Calendar.DAY_OF_YEAR, 1); // Add 1 day to today
//         Date startDate = calendar.getTime();

//         // Set the end date to three days after tomorrow
//         calendar.add(Calendar.DAY_OF_YEAR, 3); // Add 3 more days
//         Date endDate = calendar.getTime();

       

//         // Create a Rental
//         rental = new Rental();
//         rental.setStartDate(startDate);
//         rental.setEndDate(endDate);
//         rental.setCity("Brussels");
//         rental.setStreet("Main Street");
//         rental.setPostalCode(1000);
//         rental.setPhoneNumber("0123456789");
//         rental.setEmail("rental@example.com");
//         rental.setCar(car);

//         rentalRepo.save(rental);


//         rent2 = new Rent();
//         rent2.setRenterMail("renter2@example.com");
//         rent2.setRenterPhoneNumber("0456789023");
//         rent2.setNationalRegisterNumber("10987654321");
//         rent2.setBirthDate(new Date(0));
//         rent2.setDrivingLicenseNumber("123456789");
//         rent2.setStatus(Rent.Status.Confirmed);
//         rent2.setRental(rental);

//         // Save Rent objects
//         rentRepo.save(rent2);
//     }


//     @Test
//     void testFindByReceiverId_ValidReceiverId() {

//         when(rentRepo.findById(null))
//         // Arrange
//         Notification notification1 = new Notification("Notification 1", );

//         notificationRepo.save(notification1);

//         Notification notification2 = new Notification();
       
//         notificationRepo.save(notification2);

//         Notification notification3 = new Notification();
//         notificationRepo.save(notification3);

//         // Act
//         List<Notification> notifications = notificationRepo.findByReceiverId(1L);

//         // Assert
//         assertThat(notifications).isNotEmpty();
//         assertThat(notifications).hasSize(2);
//         assertEquals("Notification 1", notifications.get(0).getMessage());
//         assertEquals("Notification 2", notifications.get(1).getMessage());
//     }

//     @Test
//     void testFindByReceiverId_NonExistentReceiverId() {
//         // Act
//         List<Notification> notifications = notificationRepo.findByReceiverId(99L);

//         // Assert
//         assertThat(notifications).isEmpty();
//     }

//     @Test
//     void testSaveAndRetrieveNotification() {
//         // Arrange
//         Notification notification = new Notification();
//         notificationRepo.save(notification);

//         // Act
//         List<Notification> notifications = notificationRepo.findByReceiverId(3L);

//         // Assert
//         assertThat(notifications).isNotEmpty();
//         assertThat(notifications).hasSize(1);
//         assertEquals("Test notification", notifications.get(0).getMessage());
//     }
    
// }
