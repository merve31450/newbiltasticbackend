package org.u2soft.billtasticbackend.repository;

import org.u2soft.billtasticbackend.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    // Örnek bir custom query method: Kullanıcıya göre ödeme arama
}
