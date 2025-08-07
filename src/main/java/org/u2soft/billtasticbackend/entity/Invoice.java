package org.u2soft.billtasticbackend.entity;
import lombok.Data;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "invoices")
@Data
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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
