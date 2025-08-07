package org.u2soft.billtasticbackend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.u2soft.billtasticbackend.dto.InvoiceRequest;
import org.u2soft.billtasticbackend.entity.Invoice;
import org.u2soft.billtasticbackend.entity.InvoiceItem;
import org.u2soft.billtasticbackend.repository.InvoiceRepository;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class InvoiceService {

    private final InvoiceRepository invoiceRepository;

    @Autowired
    public InvoiceService(InvoiceRepository invoiceRepository) {
        this.invoiceRepository = invoiceRepository;
    }


    public List<Invoice> getAllInvoices() {
        return invoiceRepository.findAll();
    }


    public Invoice createInvoice(Invoice invoice) {
        Optional<Invoice> existingInvoice = invoiceRepository.findByInvoiceNumber(invoice.getInvoiceNumber());
        if (existingInvoice.isPresent()) {
            throw new IllegalArgumentException("Bu fatura numarası zaten kayıtlı: " + invoice.getInvoiceNumber());
        }
        return invoiceRepository.save(invoice);
    }
    @Transactional

    public Invoice saveInvoiceFromRequest(InvoiceRequest req) {
        Invoice invoice = new Invoice();
        invoice.setInvoiceNumber(req.getInvoiceNo());
        invoice.setAmount(Double.parseDouble(req.getTotalAmount()));
        invoice.setIssueDate(LocalDate.parse(req.getDate()));
        invoice.setDueDate(LocalDate.now().plusDays(30)); // Ödeme tarihi 30 gün sonrası


        invoice.setCompanyName(req.getCompanyName());
        invoice.setAddress(req.getAddress());
        invoice.setPhone(req.getPhone());
        invoice.setEmail(req.getEmail());
        invoice.setWebsite(req.getWebsite());
        invoice.setBankAccount(req.getBankAccount());


        List<InvoiceItem> items = req.getItems().stream().map(itemReq -> {
            InvoiceItem item = new InvoiceItem();
            item.setDescription(itemReq.getDescription());
            item.setUnitPrice(itemReq.getUnitPrice());
            item.setQuantity(itemReq.getQuantity());
            return item;
        }).collect(Collectors.toList());

        invoice.setItems(items);

        return invoiceRepository.save(invoice);
    }


    public Invoice updateInvoice(Long id, Invoice updatedInvoice) {
        return invoiceRepository.findById(id).map(existingInvoice -> {
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
        }).orElseThrow(() -> new IllegalArgumentException("Fatura bulunamadı: " + id));
    }


    public void deleteInvoice(Long id) {
        invoiceRepository.deleteById(id);
    }
}

