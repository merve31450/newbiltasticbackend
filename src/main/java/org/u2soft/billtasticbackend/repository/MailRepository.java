package org.u2soft.billtasticbackend.repository;

import org.u2soft.billtasticbackend.entity.Mail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
@Repository
public interface MailRepository extends JpaRepository<Mail, Long> {
    // E-posta gönderim geçmişi aramaları için örnek bir method
    List<Mail> findByTo(String email);
}
