package be.ucll.se.aaron_abbey_backend.mail;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import be.ucll.se.aaron_abbey_backend.model.Notification.NotificationType;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class MailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String sender;

    public enum EmailType {
        OWNER_NOTIFICATION, RENTER_NOTIFICATION
    }

    private String sendEmail(MailTemplate mailTemplate, String recipient, NotificationType notificationType)
            throws MessagingException {

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        String subject;

        if (notificationType == NotificationType.CONFIRMATION) {
            subject = "New rental confirmation!";
        } else if (notificationType == NotificationType.CANCELATION) {
            subject = "New rental cancellation!";
        } else  {
            subject = "New rental request!";
        } 

        helper.setFrom(sender);
        helper.setTo(recipient);
        helper.setSubject(subject);
        helper.setText(mailTemplate.getHtml(), true);
        mailSender.send(message);

        return mailTemplate.getText();
    }

    public String sendNotificationMail(String reciptient, NotificationType notificationType, String ownerName,
            String renterName,
            String car, String licensePlate, String startDate, String endDate, EmailType emailType)
            throws Exception {

        MailTemplate mailTemplate = new MailTemplate();

        if (emailType == EmailType.OWNER_NOTIFICATION) {
            mailTemplate = MailTemplate.mailToOwner(notificationType, ownerName, renterName, car, licensePlate,
                    startDate, endDate);

            sendEmail(mailTemplate, reciptient, notificationType);
            return mailTemplate.getText();
        } else if (emailType == EmailType.RENTER_NOTIFICATION) {
            mailTemplate = MailTemplate.mailToRenter(notificationType, renterName, car, licensePlate,
                    startDate, endDate);

            sendEmail(mailTemplate, reciptient, notificationType);
            mailTemplate.getText();
            return mailTemplate.getText();
        } else {
            throw new Exception();
        }
    }

}
