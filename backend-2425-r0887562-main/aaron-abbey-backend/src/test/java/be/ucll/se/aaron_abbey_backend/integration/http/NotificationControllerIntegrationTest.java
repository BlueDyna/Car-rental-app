package be.ucll.se.aaron_abbey_backend.integration.http;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.ActiveProfiles;

import be.ucll.se.aaron_abbey_backend.controller.NotificationController;

@WebMvcTest(NotificationController.class)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
public class NotificationControllerIntegrationTest {
    
}
