package org.u2soft.billtasticbackend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.u2soft.billtasticbackend.dto.InvoiceRequest;
import org.u2soft.billtasticbackend.entity.Invoice;
import org.u2soft.billtasticbackend.entity.InvoiceItem;
import org.u2soft.billtasticbackend.entity.User;
import org.u2soft.billtasticbackend.repository.InvoiceRepository;
import org.u2soft.billtasticbackend.repository.UserRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final UserRepository userRepository;

    @Autowired
    public InvoiceService(InvoiceRepository invoiceRepository, UserRepository userRepository) {
        this.invoiceRepository = invoiceRepository;
        this.userRepository = userRepository;
    }

    /* =====================================================
       🔐 TOKEN'DAN GİRİŞ YAPAN KULLANICIYI AL
       ===================================================== */
    private User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName(); // Token'dan gelen username (bizde e-posta)
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new RuntimeException("Kullanıcı bulunamadı: " + email);
        }
        return user;
    }

    /* =====================================================
       💎 BENZERSİZ FATURA NUMARASI ÜRET
       ===================================================== */
    private String generateUniqueInvoiceNumber() {
        String invoiceNumber;
        do {
            invoiceNumber = "VL" + (int) (Math.random() * 100_000_000); // örn: VL12345678
        } while (invoiceRepository.findByInvoiceNumber(invoiceNumber).isPresent());
        return invoiceNumber;
    }

    /* =====================================================
       🧾 FATURA OLUŞTUR (Token’lı kullanıcıya ait)
       ===================================================== */
    public Invoice createInvoice(Invoice invoice) {
        Optional<Invoice> existingInvoice = invoiceRepository.findByInvoiceNumber(invoice.getInvoiceNumber());
        if (existingInvoice.isPresent()) {
            throw new IllegalArgumentException("Bu fatura numarası zaten kayıtlı: " + invoice.getInvoiceNumber());
        }

        invoice.setUser(getCurrentUser()); // 🔐 Kullanıcıyı ata
        return invoiceRepository.save(invoice);
    }

    /* =====================================================
       💾 FATURA KAYDET (Frontend'den gelen InvoiceRequest)
       ===================================================== */
    @Transactional
    public Invoice saveInvoiceFromRequest(InvoiceRequest req) {
        Invoice invoice = new Invoice();

        // 💎 1) Fatura numarası benzersiz olsun
        String invoiceNo = req.getInvoiceNo();
        if (invoiceNo == null || invoiceRepository.findByInvoiceNumber(invoiceNo).isPresent()) {
            invoiceNo = generateUniqueInvoiceNumber();
        }
        invoice.setInvoiceNumber(invoiceNo);

        // 💰 2) Diğer alanları doldur
        invoice.setAmount(Double.parseDouble(req.getTotalAmount()));
        invoice.setIssueDate(LocalDate.parse(req.getDate()));
        invoice.setDueDate(LocalDate.now().plusDays(30)); // 30 gün vade
        invoice.setCompanyName(req.getCompanyName());
        invoice.setAddress(req.getAddress());
        invoice.setPhone(req.getPhone());
        invoice.setEmail(req.getEmail());
        invoice.setWebsite(req.getWebsite());
        invoice.setBankAccount(req.getBankAccount());
        invoice.setUser(getCurrentUser()); // 💥 Token'daki kullanıcıyı ata

        // 🧾 3) Kalemleri ekle
        List<InvoiceItem> items = req.getItems().stream().map(itemReq -> {
            InvoiceItem item = new InvoiceItem();
            item.setDescription(itemReq.getDescription());
            item.setUnitPrice(itemReq.getUnitPrice());
            item.setQuantity(itemReq.getQuantity());
            return item;
        }).collect(Collectors.toList());

        invoice.setItems(items);

        // 💾 4) Kaydet
        return invoiceRepository.save(invoice);
    }

    /* =====================================================
       📄 TOKEN’A GÖRE KULLANICIYA AİT TÜM FATURALARI GETİR
       ===================================================== */
    public List<Invoice> getAllInvoices() {
        User currentUser = getCurrentUser();
        return invoiceRepository.findByUserId(currentUser.getId());
    }

    /* =====================================================
       ✏️ FATURA GÜNCELLE
       ===================================================== */
    public Invoice updateInvoice(Long id, Invoice updatedInvoice) {
        User currentUser = getCurrentUser();

        return invoiceRepository.findById(id)
                .map(existingInvoice -> {
                    if (!existingInvoice.getUser().getId().equals(currentUser.getId())) {
                        throw new SecurityException("Bu faturayı düzenleme yetkiniz yok!");
                    }

                    existingInvoice.setInvoiceNumber(updatedInvoice.getInvoiceNumber());
                    existingInvoice.setAmount(updatedInvoice.getAmount());
                    existingInvoice.setIssueDate(updatedInvoice.getIssueDate());
                    existingInvoice.setDueDate(updatedInvoice.getDueDate());
                    existingInvoice.setCompanyName(updatedInvoice.getCompanyName());
                    existingInvoice.setAddress(updatedInvoice.getAddress());
                    existingInvoice.setPhone(updatedInvoice.getPhone());
                    existingInvoice.setEmail(updatedInvoice.getEmail());
                    existingInvoice.setWebsite(updatedInvoice.getWebsite());
                    existingInvoice.setBankAccount(updatedInvoice.getBankAccount());
                    existingInvoice.setItems(updatedInvoice.getItems());

                    return invoiceRepository.save(existingInvoice);
                })
                .orElseThrow(() -> new IllegalArgumentException("Fatura bulunamadı: " + id));
    }

    /* =====================================================
       🗑️ FATURA SİL
       ===================================================== */
    public void deleteInvoice(Long id) {
        User currentUser = getCurrentUser();

        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Fatura bulunamadı: " + id));

        if (!invoice.getUser().getId().equals(currentUser.getId())) {
            throw new SecurityException("Bu faturayı silme yetkiniz yok!");
        }

        invoiceRepository.delete(invoice);
    }
}
