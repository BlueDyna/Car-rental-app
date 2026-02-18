package be.ucll.se.aaron_abbey_backend.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class NotificationTest {

    private Notification notification;
    private String MESSAGE = "Test notification message";
    private  Long RENT_ID = 1L;
    private  Long RECEIVER_ID = 2L;
    private  Date TIMESTAMP = new Date();
    private  Notification.NotificationType TYPE = Notification.NotificationType.CONFIRMATION;

    @BeforeEach
    void setUp() {
        // Initialize a Notification object before each test
        notification = new Notification(MESSAGE, RENT_ID, RECEIVER_ID, TIMESTAMP, TYPE);
    }

    @Test
    void testConstructor() {
        // Test constructor initialization
        assertNotNull(notification);
        assertEquals(MESSAGE, notification.getMessage());
        assertEquals(RENT_ID, notification.getRentId());
        assertEquals(RECEIVER_ID, notification.getReceiverId());
        assertEquals(TIMESTAMP, notification.getTimestamp());
        assertEquals(TYPE, notification.getType());
    }

    @Test
    void testSettersAndGetters() {
        // Create a new Notification instance
        Notification updatedNotification = new Notification();
        updatedNotification.setMessage("Updated message");
        updatedNotification.setMessage("Updated message");
        updatedNotification.setMessage("Updated message");
    }
}
