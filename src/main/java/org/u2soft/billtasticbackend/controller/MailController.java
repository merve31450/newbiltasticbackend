package org.u2soft.billtasticbackend.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.u2soft.billtasticbackend.dto.MailRequestDto;
import org.u2soft.billtasticbackend.service.MailService;

@CrossOrigin(
        origins = {
                "http://localhost:3000",
                "http://127.0.0.1:3000",
                "http://192.168.56.1:3000"
        },
        allowCredentials = "true"
)
@RestController
@RequestMapping("/api/mail")
public class MailController {

    @Autowired
    private MailService mailService;

    /* =====================================================
       1️E-POSTA GÖNDER (GEÇERLİLİK KONTROLÜ DAHİL)
       ===================================================== */
    @PostMapping("/send")
    public ResponseEntity<String> sendMail(@Valid @RequestBody MailRequestDto mailRequestDto) {

        // 1. Şifre kontrolü
        if (!mailRequestDto.getPassword().equals(mailRequestDto.getRepeatPassword())) {
            return ResponseEntity.badRequest().body("Şifreler uyuşmuyor.");
        }

        // 2. Mail içeriği
        String subject = "Planlı Mail Gönderimi";
        String body = "Bu bir planlı mail gönderimidir.\n" +
                "Tarih: " + mailRequestDto.getDate() + "\nSaat: " + mailRequestDto.getTime();

        // 3. Mail servisine gönder
        mailService.sendEmailWithDelay(
                mailRequestDto.getEmail(),
                subject,
                body,
                mailRequestDto.getDate(),
                mailRequestDto.getTime()
        );

        return ResponseEntity.ok("Mail başarıyla planlandı!");
    }

    /* =====================================================
       2️ DOSYA EKLİ MAIL GÖNDER (PLANLI)
       ===================================================== */
    @PostMapping("/send-attachment")
    public ResponseEntity<String> sendMailWithAttachment(
            @RequestParam("to") String to,
            @RequestParam("subject") String subject,
            @RequestParam("body") String body,
            @RequestParam("file") MultipartFile file,
            @RequestParam("date") String date,
            @RequestParam("time") String time) {
        try {
            mailService.sendEmailWithAttachmentDelay(to, subject, body, file, date, time);
            return ResponseEntity.ok("Dosya ekli mail zamanlı olarak planlandı!");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
}
