package com.myproject.Mail_2_base64_Handler;

import com.myproject.Mail_2_base64_Handler.Controller.MailController;
import com.myproject.Mail_2_base64_Handler.Service.MailService;
import jakarta.mail.MessagingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

class MailControllerTest {

    @Mock
    private MailService mailService;

    @InjectMocks
    private MailController mailController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void readUnreadEmails_success() throws MessagingException, IOException {
        ResponseEntity<String> response = mailController.readUnreadEmails();

        verify(mailService).readUnreadMails();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Successfully read and processed unread emails.", response.getBody());
    }

    @Test
    void readUnreadEmails_failure() throws MessagingException, IOException {
        doThrow(new MessagingException("Failed to read emails")).when(mailService).readUnreadMails();

        ResponseEntity<String> response = mailController.readUnreadEmails();

        verify(mailService).readUnreadMails();
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Failed to read and process unread emails: Failed to read emails", response.getBody());
    }

    @Test
    void readUnreadEmails_ioException() throws MessagingException, IOException {
        doThrow(new IOException("IO Exception")).when(mailService).readUnreadMails();

        ResponseEntity<String> response = mailController.readUnreadEmails();

        verify(mailService).readUnreadMails();
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Failed to read and process unread emails: IO Exception", response.getBody());
    }
}
