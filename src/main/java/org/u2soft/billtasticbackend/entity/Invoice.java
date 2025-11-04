package org.u2soft.billtasticbackend.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "invoices")
@Data
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 🔐 Fatura hangi kullanıcıya ait
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, unique = true)
    private String invoiceNumber;

    @Column(nullable = false)
    private Double amount;

    private LocalDate issueDate;
    private LocalDate dueDate;

    // Şirket bilgileri
    private String companyName;
    private String address;
    private String phone;
    private String email;
    private String website;
    private String bankAccount;

    // Ürünler
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "invoice_id")
    private List<InvoiceItem> items;
}
