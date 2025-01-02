package org.u2soft.billtasticbackend.repository;

import org.u2soft.billtasticbackend.entity.Todo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TodoRepository extends JpaRepository<Todo, Long> {
    // Örnek bir custom query method: Başlıkla göre Todo arama
    List<Todo> findByTitle(String title);
}
