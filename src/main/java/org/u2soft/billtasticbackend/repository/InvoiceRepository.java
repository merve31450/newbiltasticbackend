package org.u2soft.billtasticbackend.repository;

import org.u2soft.billtasticbackend.entity.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.time.LocalDate;
@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {


    // Fatura tarihi ile sorgulama yapılabilir
    List<Invoice> findByIssueDateBetween(LocalDate startDate, LocalDate endDate);
}
