package org.u2soft.billtasticbackend.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "mails")
public class Mail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Benzersiz birincil anahtar

    @Column(nullable = false)
    private String sender; // Gönderen

    @Column(nullable = false)
    private String recipient; // Alıcı

    @Column(nullable = false)
    private String subject; // Konu

    @Column(nullable = false, columnDefinition = "TEXT")
    private String body; // E-posta içeriği

    @Column(name = "sent_date")
    private LocalDateTime sentDate; // Gönderilme tarihi

    @Column(name = "status")
    private String status; // Durum (ör. Gönderildi, Başarısız)

    // Getter ve Setter metotları
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public LocalDateTime getSentDate() {
        return sentDate;
    }

    public void setSentDate(LocalDateTime sentDate) {
        this.sentDate = sentDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String to;
}
