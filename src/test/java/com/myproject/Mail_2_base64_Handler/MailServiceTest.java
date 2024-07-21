package com.myproject.Mail_2_base64_Handler;

import com.myproject.Mail_2_base64_Handler.Entity.Image;
import com.myproject.Mail_2_base64_Handler.Repository.ImageRepository;
import com.myproject.Mail_2_base64_Handler.Service.MailService;
import jakarta.mail.*;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import jakarta.mail.search.FlagTerm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class MailServiceTest {

    @Mock
    private ImageRepository imageRepository;

    @InjectMocks
    private MailService mailService;

    @Mock
    private Store store;

    @Mock
    private Folder inbox;

    @Mock
    private Folder processedArchiveFolder;

    @BeforeEach
    void setUp() throws MessagingException {
        MockitoAnnotations.openMocks(this);

        // Mock the connection to the store and inbox
        when(store.getFolder("INBOX")).thenReturn(inbox);
        when(store.getFolder("Processed_Archive")).thenReturn(processedArchiveFolder);
    }

    @Test
    void readUnreadMails_success() throws MessagingException, IOException {
        Properties props = new Properties();
        Session session = Session.getInstance(props);

        MimeMessage message = new MimeMessage(session);
        message.setSubject("Test Subject");
        message.setContent("Test Content", "text/plain");

        when(inbox.search(any(FlagTerm.class))).thenReturn(new Message[]{message});
        when(inbox.getStore()).thenReturn(store);

        mailService.readUnreadMails();

        verify(inbox, times(1)).open(Folder.READ_WRITE);
        verify(inbox, times(1)).close(false);
        verify(store, times(1)).close();
    }

    @Test
    void readUnreadMails_failure() throws MessagingException, IOException {
        when(inbox.search(any(FlagTerm.class))).thenThrow(new MessagingException("Failed to read emails"));

        MessagingException exception = assertThrows(MessagingException.class, () -> mailService.readUnreadMails());
        assertEquals("Failed to read emails", exception.getMessage());
    }

    @Test
    void processMimeMessage_imageAttachment() throws MessagingException, IOException {
        Properties props = new Properties();
        Session session = Session.getInstance(props);

        MimeMessage message = new MimeMessage(session);
        message.setSubject("Test Image");
        Multipart multipart = new MimeMultipart();
        MimeBodyPart bodyPart = new MimeBodyPart();
        bodyPart.setContent("image content", "image/png");
        multipart.addBodyPart(bodyPart);
        message.setContent(multipart);

        ReflectionTestUtils.invokeMethod(mailService, "processMimeMessage", message);

        verify(imageRepository, times(1)).save(any(Image.class));
    }

    @Test
    void saveImage_success() throws MessagingException, IOException {
        String subject = "Test Image";
        String imageContent = "image content";
        byte[] imageBytes = imageContent.getBytes();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(imageBytes);

        BodyPart bodyPart = mock(BodyPart.class);
        when(bodyPart.getInputStream()).thenReturn(inputStream);
        when(bodyPart.getContentType()).thenReturn("image/png");

        ReflectionTestUtils.invokeMethod(mailService, "saveImage", bodyPart, subject);

        ArgumentCaptor<Image> imageCaptor = ArgumentCaptor.forClass(Image.class);
        verify(imageRepository, times(1)).save(imageCaptor.capture());
        Image savedImage = imageCaptor.getValue();
        assertEquals(subject, savedImage.getName());
        assertEquals(Base64.getEncoder().encodeToString(imageBytes), Base64.getEncoder().encodeToString(savedImage.getData()));
    }

    @Test
    void moveToProcessedArchiveFolder_success() throws MessagingException {
        Message message = mock(Message.class);
        when(inbox.getStore()).thenReturn(store);
        when(store.getFolder("Processed_Archive")).thenReturn(processedArchiveFolder);
        when(processedArchiveFolder.exists()).thenReturn(false);

        ReflectionTestUtils.invokeMethod(mailService, "moveToProcessedArchiveFolder", inbox, message);

        verify(processedArchiveFolder, times(1)).create(Folder.HOLDS_MESSAGES);
        verify(processedArchiveFolder, times(1)).open(Folder.READ_WRITE);
        verify(inbox, times(1)).copyMessages(new Message[]{message}, processedArchiveFolder);
        verify(message, times(1)).setFlag(Flags.Flag.DELETED, true);
        verify(inbox, times(1)).expunge();
        verify(processedArchiveFolder, times(1)).close(false);
    }
}
