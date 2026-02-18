package be.ucll.se.aaron_abbey_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import be.ucll.se.aaron_abbey_backend.model.Notification;
import java.util.List;

@Repository
public interface NotificationRepo extends JpaRepository<Notification, Long> {
    List<Notification> findByReceiverId(Long receiverId);
}
