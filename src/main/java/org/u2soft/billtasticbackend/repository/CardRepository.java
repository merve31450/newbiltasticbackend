package org.u2soft.billtasticbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.u2soft.billtasticbackend.entity.Card;

@Repository
public interface CardRepository extends JpaRepository<Card, Long> {
    // JpaRepository, CRUD işlemleri için gerekli tüm metodları sağlar.
}
