// src/main/java/org/u2soft/billtasticbackend/controller/InvoiceController.java
package org.u2soft.billtasticbackend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.u2soft.billtasticbackend.dto.InvoicePdfGenerator;
import org.u2soft.billtasticbackend.dto.InvoiceRequest;
import org.u2soft.billtasticbackend.entity.Invoice;
import org.u2soft.billtasticbackend.service.InvoiceService;

import java.io.ByteArrayOutputStream;
import java.util.List;

/**
 * Tüm fatura uç-noktaları tek yerde.
 *   • /api/invoices                 → CRUD
 *   • /api/invoices/save            → InvoiceRequest’i DB’ye kaydet
 *
 *   • /api/invoices/save-and-pdf    → Kaydet + PDF (byte[])
 */
@RestController
@RequestMapping("/api/invoices")
@CrossOrigin(origins = {
        "http://localhost:3000",
        "http://127.0.0.1:3000",
        "http://192.168.56.1:3000"
})
@PreAuthorize("permitAll()")          // Gerektiğinde ROLE bazlı daralt
@RequiredArgsConstructor
public class InvoiceController {

    private final InvoiceService invoiceService;

    /* =========================================================
       1) KLASİK CRUD (Invoice entity)
       ========================================================= */

    @GetMapping
    public List<Invoice> getAllInvoices() {
        return invoiceService.getAllInvoices();
    }

    @PostMapping
    public ResponseEntity<Invoice> createInvoice(@RequestBody Invoice invoice) {
        try {
            Invoice created = invoiceService.createInvoice(invoice);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (IllegalArgumentException ex) {     // fatura numarası duplicate vb.
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Invoice> updateInvoice(@PathVariable Long id,
                                                 @RequestBody Invoice invoice) {
        try {
            Invoice updated = invoiceService.updateInvoice(id, invoice);
            return ResponseEntity.ok(updated);
        } catch (SecurityException ex) {            // başka kullanıcının faturası
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInvoice(@PathVariable Long id) {
        try {
            invoiceService.deleteInvoice(id);
            return ResponseEntity.noContent().build();
        } catch (SecurityException ex) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /* =========================================================
       2) InvoiceRequest → DB’ye kaydet (/save)
       ========================================================= */
    @PostMapping("/save")
    public ResponseEntity<Void> saveInvoice(@RequestBody InvoiceRequest req) {
        invoiceService.saveInvoiceFromRequest(req);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /* =========================================================
       3) Kaydet + PDF döndür (/save-and-pdf)
       ========================================================= */
    @PostMapping("/save-and-pdf")
    public ResponseEntity<byte[]> saveAndCreatePdf(@RequestBody InvoiceRequest req) {
        try {
            invoiceService.saveInvoiceFromRequest(req);                 // DB kaydı
            ByteArrayOutputStream pdf = InvoicePdfGenerator.createInvoicePdf(req);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=fatura.pdf")
                    .body(pdf.toByteArray());

        } catch (Exception ex) {   // PDF veya IO hatası
            ex.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
