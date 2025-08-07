package org.u2soft.billtasticbackend.service;

import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Service
public class MailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private ThreadPoolTaskScheduler taskScheduler;

    public void sendEmailWithDelay(String to, String subject, String body, String date, String time) {
        LocalDateTime scheduledTime = LocalDateTime.parse(date + "T" + time);
        LocalDateTime now = LocalDateTime.now();

        if (scheduledTime.isBefore(now)) {
            sendEmailImmediately(to, subject, body);
        } else {
            Date executionTime = Date.from(scheduledTime.atZone(ZoneId.systemDefault()).toInstant());

            taskScheduler.schedule(() -> sendEmailImmediately(to, subject, body), executionTime);

            System.out.println("Görev planlandı: " + scheduledTime);
        }
    }

    public void sendEmailWithAttachmentDelay(String to, String subject, String body, MultipartFile file, String date, String time) {
        try {

            String tempDir = System.getProperty("java.io.tmpdir");
            Path savedPath = Paths.get(tempDir, file.getOriginalFilename());
            file.transferTo(savedPath);

            LocalDateTime scheduledTime = LocalDateTime.parse(date + "T" + time);
            LocalDateTime now = LocalDateTime.now();

            if (scheduledTime.isBefore(now)) {
                sendEmailWithAttachmentFromPath(to, subject, body, savedPath.toString());
            } else {
                Date executionTime = Date.from(scheduledTime.atZone(ZoneId.systemDefault()).toInstant());

                taskScheduler.schedule(() -> {
                    try {
                        sendEmailWithAttachmentFromPath(to, subject, body, savedPath.toString());
                        Files.deleteIfExists(savedPath); // Gönderim sonrası dosyayı sil
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }, executionTime);

                System.out.println("Dosya ekli görev planlandı: " + scheduledTime);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendEmailImmediately(String to, String subject, String body) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body, true);

            mailSender.send(message);
            System.out.println("Mail hemen gönderildi: " + to);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendEmailWithAttachmentFromPath(String to, String subject, String body, String filePath) throws Exception {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(body, true);

        File file = new File(filePath);
        helper.addAttachment(file.getName(), file);

        mailSender.send(message);
        System.out.println("Dosya ekli mail gönderildi: " + to);
    }
}
