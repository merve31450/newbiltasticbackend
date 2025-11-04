package org.u2soft.billtasticbackend.controller;

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

    @PostMapping("/send")
    public ResponseEntity<String> sendMail(@RequestBody MailRequestDto mailRequestDto) {
        if (!mailRequestDto.getPassword().equals(mailRequestDto.getRepeatPassword())) {
            return ResponseEntity.badRequest().body("Şifreler uyuşmuyor.");
        }

        String subject = "Planlı Mail Gönderimi";
        String body = "Bu bir planlı mail gönderimidir.\n" +
                "Tarih: " + mailRequestDto.getDate() + "\nSaat: " + mailRequestDto.getTime();

        mailService.sendEmailWithDelay(mailRequestDto.getEmail(), subject, body, mailRequestDto.getDate(), mailRequestDto.getTime());
        return ResponseEntity.ok("Mail başarıyla planlandı!");
    }

    @PostMapping("/send-attachment")
    public ResponseEntity<String> sendMailWithAttachment(@RequestParam("to") String to,
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
