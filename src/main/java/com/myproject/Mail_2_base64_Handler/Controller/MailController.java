package com.myproject.Mail_2_base64_Handler.Controller;

import com.myproject.Mail_2_base64_Handler.Service.MailService;
import com.myproject.Mail_2_base64_Handler.Service.NotificationService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/emails")
public class MailController {

    private final MailService emailService;
    private final NotificationService notificationService;

    @Autowired
    public MailController(MailService emailService, NotificationService notificationService) {
        this.emailService = emailService;
        this.notificationService = notificationService;
    }

    @GetMapping("/processUnread")
    public ResponseEntity<String> readUnreadEmails() {
        try {
            emailService.readUnreadMails();
            return ResponseEntity.ok("Successfully read and processed unread emails.");
        } catch (MessagingException | IOException e) {
            notificationService.sendErrorNotification(e.getMessage(), "");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to read and process unread emails: " + e.getMessage());
        }
    }
}
