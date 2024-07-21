package com.myproject.Mail_2_base64_Handler.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;

@Service
public class NotificationService {

    private static final Logger logger = LoggerFactory.getLogger(NotificationService.class);

    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.recipient}")
    private String notificationEmailRecipient;

    @Value("${spring.mail.username}")
    private String notificationEmailSender;

    public void sendEmailNotification(String subject, String errorMessage, String emailBody) {
        try {
            String emailContent = createEmailContent(errorMessage, emailBody);
            sendEmail(notificationEmailRecipient, subject, emailContent);
        } catch (Exception e) {
            logger.error("Failed to send email notification: {}", e.getMessage(), e);
        }
    }

    private String createEmailContent(String errorMessage, String emailBody) {
        StringBuilder emailContent = new StringBuilder();
        emailContent.append("Hi Team,<br><br>");
        emailContent.append("An error occurred during email processing.<br><br>");
        emailContent.append("Error Message: ").append(errorMessage).append("<br><br>");
        emailContent.append("Email Body: ").append(emailBody).append("<br><br>");
        emailContent.append("Please review the details.<br><br>");
        emailContent.append("Regards,<br>Mail base64 Handler Team.");
        return emailContent.toString();
    }

    private void sendEmail(String recipient, String subject, String emailContent) throws MessagingException, UnsupportedEncodingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

        helper.setFrom(notificationEmailSender, "Mail base64 Handler Team");
        helper.setTo(recipient);
        helper.setSubject(subject);
        helper.setText(emailContent, true);

        javaMailSender.send(mimeMessage);
    }

    public void sendErrorNotification(String errorMessage, String emailBody) {
        sendEmailNotification("Error Notification", errorMessage, emailBody);
    }

    public void sendExceptionNotification(String exceptionMessage, String emailBody) {
        sendEmailNotification("Exception Notification", exceptionMessage, emailBody);
    }
}
