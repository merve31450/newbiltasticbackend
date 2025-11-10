
package org.u2soft.billtasticbackend.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.u2soft.billtasticbackend.dto.InvoiceRequest;
import org.u2soft.billtasticbackend.entity.Invoice;
import org.u2soft.billtasticbackend.entity.InvoiceItem;
import org.u2soft.billtasticbackend.entity.User;
import org.u2soft.billtasticbackend.repository.InvoiceRepository;
import org.u2soft.billtasticbackend.repository.UserRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final UserRepository userRepository;

    /* =========================================================
       1️⃣ Aktif kullanıcıyı getir (token'dan)
       ========================================================= */
    private User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new RuntimeException("Kullanıcı bulunamadı: " + email);
        }
        return user;
    }

    /* =========================================================
       2️⃣ Tüm faturaları getir (kullanıcıya göre)
       ========================================================= */
    public List<Invoice> getAllInvoices() {
        return invoiceRepository.findByUserId(getCurrentUser().getId());
    }

    /* =========================================================
       3️⃣ Yeni fatura oluştur (USD/EUR destekli)
       ========================================================= */
    @Transactional
    public Invoice saveInvoiceFromRequest(InvoiceRequest req) {
        User currentUser = getCurrentUser();

        Invoice invoice = new Invoice();
        invoice.setUser(currentUser);

        // Fatura numarası
        invoice.setInvoiceNumber(
                Optional.ofNullable(req.getInvoiceNo()).orElse(generateUniqueInvoiceNumber())
        );

        invoice.setIssueDate(parseDate(req.getDate()));
        invoice.setDueDate(LocalDate.now().plusDays(30));

        // Şirket bilgileri
        invoice.setCompanyName(req.getCompanyName());
        invoice.setAddress(req.getAddress());
        invoice.setPhone(req.getPhone());
        invoice.setEmail(req.getEmail());
        invoice.setWebsite(req.getWebsite());
        invoice.setBankAccount(req.getBankAccount());

        // 💸 Para birimleri
        invoice.setAmount(req.getTotalAmount() != null ? req.getTotalAmount() : BigDecimal.ZERO);
        invoice.setAmountUsd(req.getTotalAmountUsd() != null ? req.getTotalAmountUsd() : BigDecimal.ZERO);
        invoice.setAmountEur(req.getTotalAmountEur() != null ? req.getTotalAmountEur() : BigDecimal.ZERO);

        // 📦 Kalemleri dönüştür
        if (req.getItems() != null) {
            for (InvoiceRequest.InvoiceItem dtoItem : req.getItems()) {
                InvoiceItem item = new InvoiceItem();
                item.setDescription(dtoItem.getDescription());
                item.setQuantity(dtoItem.getQuantity() != null ? dtoItem.getQuantity() : BigDecimal.ONE);
                item.setUnitPrice(dtoItem.getUnitPrice() != null ? dtoItem.getUnitPrice() : BigDecimal.ZERO);
                item.setUnitPriceUsd(dtoItem.getUnitPriceUsd() != null ? dtoItem.getUnitPriceUsd() : BigDecimal.ZERO);
                item.setUnitPriceEur(dtoItem.getUnitPriceEur() != null ? dtoItem.getUnitPriceEur() : BigDecimal.ZERO);
                item.setInvoice(invoice);
                invoice.getItems().add(item);
            }
        }

        return invoiceRepository.save(invoice);
    }

    /* =========================================================
       4️⃣ Fatura Güncelle (orphanRemoval fix’li)
       ========================================================= */
    @Transactional
    public Invoice updateInvoice(Long id, Invoice updatedInvoice) {
        User currentUser = getCurrentUser();

        Invoice existing = invoiceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Fatura bulunamadı: " + id));

        if (!existing.getUser().getId().equals(currentUser.getId())) {
            throw new SecurityException("Bu faturayı güncelleme yetkiniz yok!");
        }

        existing.setCompanyName(updatedInvoice.getCompanyName());
        existing.setAddress(updatedInvoice.getAddress());
        existing.setPhone(updatedInvoice.getPhone());
        existing.setEmail(updatedInvoice.getEmail());
        existing.setWebsite(updatedInvoice.getWebsite());
        existing.setBankAccount(updatedInvoice.getBankAccount());
        existing.setInvoiceNumber(updatedInvoice.getInvoiceNumber());
        existing.setIssueDate(updatedInvoice.getIssueDate());
        existing.setDueDate(updatedInvoice.getDueDate());
        existing.setAmount(updatedInvoice.getAmount());
        existing.setAmountUsd(updatedInvoice.getAmountUsd());
        existing.setAmountEur(updatedInvoice.getAmountEur());

        // ✅ orphanRemoval fix: eski kalemleri temizle, yenilerini ekle
        existing.getItems().clear();
        if (updatedInvoice.getItems() != null) {
            for (InvoiceItem item : updatedInvoice.getItems()) {
                item.setId(null);
                item.setInvoice(existing);
                existing.getItems().add(item);
            }
        }

        return invoiceRepository.save(existing);
    }

    /* =========================================================
       5️⃣ Fatura Sil
       ========================================================= */
    @Transactional
    public void deleteInvoice(Long id) {
        User currentUser = getCurrentUser();

        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Fatura bulunamadı: " + id));

        if (!invoice.getUser().getId().equals(currentUser.getId())) {
            throw new SecurityException("Bu faturayı silme yetkiniz yok!");
        }

        invoiceRepository.delete(invoice);
    }

    /* =========================================================
       Yardımcı Fonksiyonlar
       ========================================================= */
    private LocalDate parseDate(String dateStr) {
        try {
            return LocalDate.parse(dateStr);
        } catch (Exception e) {
            return LocalDate.now();
        }
    }

    private String generateUniqueInvoiceNumber() {
        String invoiceNumber;
        do {
            invoiceNumber = "VL" + (int) (Math.random() * 1_000_000_000);
        } while (invoiceRepository.findByInvoiceNumber(invoiceNumber).isPresent());
        return invoiceNumber;
    }
}
