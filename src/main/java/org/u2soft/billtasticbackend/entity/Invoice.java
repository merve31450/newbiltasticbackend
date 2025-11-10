package org.u2soft.billtasticbackend.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "invoices")
@Data
@NoArgsConstructor
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 🔐 Kullanıcı (JWT ile ilişkili)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // 📄 Fatura bilgileri
    @Column(nullable = false, unique = true)
    private String invoiceNumber;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal amount = BigDecimal.ZERO; // TRY

    @Column(name = "amount_usd", precision = 15, scale = 2)
    private BigDecimal amountUsd = BigDecimal.ZERO;

    @Column(name = "amount_eur", precision = 15, scale = 2)
    private BigDecimal amountEur = BigDecimal.ZERO;

    private LocalDate issueDate;
    private LocalDate dueDate;

    // 🏢 Şirket bilgileri
    private String companyName;
    private String address;
    private String phone;
    private String email;
    private String website;
    private String bankAccount;

    // 🧾 Kalemler
    @OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<InvoiceItem> items = new ArrayList<>();

    /* =========================================================
       🧮 Kayıt öncesi otomatik hesaplama
    ========================================================= */
    @PrePersist
    @PreUpdate
    private void calculateTotals() {
        if (items != null && !items.isEmpty()) {
            BigDecimal totalTry = BigDecimal.ZERO;
            BigDecimal totalUsd = BigDecimal.ZERO;
            BigDecimal totalEur = BigDecimal.ZERO;

            for (InvoiceItem item : items) {
                BigDecimal qty = item.getQuantity() != null ? item.getQuantity() : BigDecimal.ONE;
                totalTry = totalTry.add(
                        (item.getUnitPrice() != null ? item.getUnitPrice() : BigDecimal.ZERO)
                                .multiply(qty)
                );
                totalUsd = totalUsd.add(
                        (item.getUnitPriceUsd() != null ? item.getUnitPriceUsd() : BigDecimal.ZERO)
                                .multiply(qty)
                );
                totalEur = totalEur.add(
                        (item.getUnitPriceEur() != null ? item.getUnitPriceEur() : BigDecimal.ZERO)
                                .multiply(qty)
                );
            }

            this.amount = totalTry;
            this.amountUsd = totalUsd;
            this.amountEur = totalEur;
        }
    }
}
