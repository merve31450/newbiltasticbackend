package org.u2soft.billtasticbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.u2soft.billtasticbackend.entity.Invoice;

import java.util.List;
import java.util.Optional;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {


    // 🔹 Fatura numarasına göre arama
    Optional<Invoice> findByInvoiceNumber(String invoiceNumber);

    // 🔹 Kullanıcı ID’sine göre faturaları getir
    List<Invoice> findByUserId(Long userId);
}
