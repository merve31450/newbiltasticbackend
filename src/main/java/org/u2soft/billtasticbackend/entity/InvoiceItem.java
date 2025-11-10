package org.u2soft.billtasticbackend.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "invoice_items")
@Data
@NoArgsConstructor
public class InvoiceItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;

    // 💰 Para birimleri (BigDecimal olarak)
    @Column(precision = 15, scale = 2)
    private BigDecimal quantity = BigDecimal.ONE;

    @Column(precision = 15, scale = 2)
    private BigDecimal unitPrice = BigDecimal.ZERO;

    @Column(name = "unit_price_usd", precision = 15, scale = 2)
    private BigDecimal unitPriceUsd = BigDecimal.ZERO;

    @Column(name = "unit_price_eur", precision = 15, scale = 2)
    private BigDecimal unitPriceEur = BigDecimal.ZERO;

    // 🔄 Fatura ilişkisi (JSON recursion fix)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "invoice_id")
    @JsonBackReference
    private Invoice invoice;
}
