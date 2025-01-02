package org.u2soft.billtasticbackend.controller;

import org.u2soft.billtasticbackend.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/mails")
public class MailController {

    private final MailService mailService;

    @Autowired
    public MailController(MailService mailService) {
        this.mailService = mailService;
    }

    // E-posta gönderme
    @PostMapping("/send")
    public String sendEmail(@RequestParam String to,
                            @RequestParam String subject,
                            @RequestParam String body) {
        try {
            mailService.sendEmail(to, subject, body);
            return "E-posta başarıyla gönderildi.";
        } catch (Exception e) {
            return "E-posta gönderilemedi: " + e.getMessage();
        }
    }
}
