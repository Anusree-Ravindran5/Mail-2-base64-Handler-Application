package com.myproject.Mail_2_base64_Handler.Service;

import com.myproject.Mail_2_base64_Handler.Entity.Image;
import com.myproject.Mail_2_base64_Handler.Repository.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.mail.*;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.search.FlagTerm;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import java.util.Properties;

@Service
public class MailService {

    @Autowired
    private ImageRepository imageRepository;

    public void readUnreadMails() throws MessagingException, IOException {
        Properties props = new Properties();
        props.setProperty("mail.store.protocol", "imaps"); // Use "imaps" for secure IMAP
        props.setProperty("mail.imap.host", "imap.gmail.com"); // Replace with actual IMAP server hostname
        props.setProperty("mail.imap.port", "993"); // IMAPS port
        props.setProperty("mail.imap.auth", "true");
        props.setProperty("mail.imap.ssl.enable", "true");
        props.setProperty("mail.imap.connectiontimeout", "10000"); // 10 seconds timeout

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("Your Email", "Your Password");
            }
        });

        Store store = session.getStore("imaps");
        store.connect("imap.gmail.com", "Your Email", "Your Password");
        Folder inbox = store.getFolder("INBOX");
        inbox.open(Folder.READ_WRITE); // Changed to READ_WRITE to mark emails as read

        Message[] messages = inbox.search(new FlagTerm(new Flags(Flags.Flag.SEEN), false));
        for (Message message : messages) {
            if (message instanceof MimeMessage) {
                processMimeMessage((MimeMessage) message);
                message.setFlag(Flags.Flag.SEEN, true);
                moveToProcessedArchiveFolder(inbox, message);
            }
        }

        inbox.close(false);
        store.close();
    }

    private void processMimeMessage(MimeMessage message) throws MessagingException, IOException {
        Object content = message.getContent();
        String subject = message.getSubject();
        if (content instanceof Multipart) {
            Multipart multipart = (Multipart) content;
            for (int i = 0; i < multipart.getCount(); i++) {
                BodyPart bodyPart = multipart.getBodyPart(i);
                if (bodyPart.isMimeType("image/*")) {
                    saveImage(bodyPart, subject);
                }
            }
        }
    }

    private void saveImage(BodyPart bodyPart, String subject) throws IOException, MessagingException {
        InputStream inputStream = bodyPart.getInputStream();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }
        byte[] imageBytes = outputStream.toByteArray();
        String base64Image = Base64.getEncoder().encodeToString(imageBytes);

        Image image = new Image();
        image.setData(imageBytes);
        image.setName(subject);
        imageRepository.save(image);

        outputStream.close();
        inputStream.close();
    }
    private void moveToProcessedArchiveFolder(Folder inbox, Message message) throws MessagingException {
        Folder processedArchiveFolder = inbox.getStore().getFolder("Processed_Archive");
        if (!processedArchiveFolder.exists()) {
            processedArchiveFolder.create(Folder.HOLDS_MESSAGES);
        }
        processedArchiveFolder.open(Folder.READ_WRITE);
        inbox.copyMessages(new Message[]{message}, processedArchiveFolder);
        message.setFlag(Flags.Flag.DELETED, true);
        inbox.expunge();
        processedArchiveFolder.close(false);
    }
}

