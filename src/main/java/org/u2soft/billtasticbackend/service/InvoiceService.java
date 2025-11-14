
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

    private User getCurrentUser() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        var email = auth.getName();
        return userRepository.findByEmail(email);
    }

    public List<Invoice> getAllInvoices() {
        return invoiceRepository.findByUserId(getCurrentUser().getId());
    }

    /* --------------------- CREATE --------------------- */
    @Transactional
    public Invoice saveInvoiceFromRequest(InvoiceRequest req) {

        User currentUser = getCurrentUser();
        Invoice invoice = new Invoice();
        invoice.setUser(currentUser);

        mapBasicFields(req, invoice);
        mapItems(req, invoice);

        return invoiceRepository.save(invoice);
    }

    /* ---------------------- UPDATE --------------------- */
    @Transactional
    public Invoice updateInvoiceFromRequest(Long id, InvoiceRequest req) {

        User currentUser = getCurrentUser();

        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invoice not found"));

        if (!invoice.getUser().getId().equals(currentUser.getId())) {
            throw new SecurityException("Bu faturayı güncelleme yetkiniz yok!");
        }

        mapBasicFields(req, invoice);

        invoice.getItems().clear();
        mapItems(req, invoice);

        return invoiceRepository.save(invoice);
    }

    /* ---------------------- DELETE --------------------- */
    @Transactional
    public void deleteInvoice(Long id) {
        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invoice not found"));
        invoiceRepository.delete(invoice);
    }

    /* ---------------------- HELPERS --------------------- */
    private void mapBasicFields(InvoiceRequest req, Invoice invoice) {

        invoice.setInvoiceNumber(req.getInvoiceNo());
        invoice.setIssueDate(parseDate(req.getDate()));
        invoice.setDueDate(LocalDate.now().plusDays(30));

        invoice.setCompanyName(req.getCompanyName());
        invoice.setAddress(req.getAddress());
        invoice.setPhone(req.getPhone());
        invoice.setEmail(req.getEmail());
        invoice.setWebsite(req.getWebsite());
        invoice.setBankAccount(req.getBankAccount());

        invoice.setAmount(req.getTotalAmount());
        invoice.setAmountUsd(req.getTotalAmountUsd());
        invoice.setAmountEur(req.getTotalAmountEur());
    }

    private void mapItems(InvoiceRequest req, Invoice invoice) {
        if (req.getItems() == null) return;

        req.getItems().forEach(dto -> {
            InvoiceItem item = new InvoiceItem();
            item.setInvoice(invoice);
            item.setDescription(dto.getDescription());
            item.setQuantity(dto.getQuantity());
            item.setUnitPrice(dto.getUnitPrice());
            item.setUnitPriceUsd(dto.getUnitPriceUsd());
            item.setUnitPriceEur(dto.getUnitPriceEur());
            invoice.getItems().add(item);
        });
    }

    private LocalDate parseDate(String dateStr) {
        try { return LocalDate.parse(dateStr); }
        catch (Exception e) { return LocalDate.now(); }
    }
    public Invoice getInvoiceById(Long id) {

        User currentUser = getCurrentUser();

        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Fatura bulunamadı"));

        if (!invoice.getUser().getId().equals(currentUser.getId())) {
            throw new SecurityException("Yetki yok!");
        }

        return invoice;
    }

}
