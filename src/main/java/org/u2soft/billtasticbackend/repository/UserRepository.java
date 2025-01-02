package org.u2soft.billtasticbackend.repository;

import org.u2soft.billtasticbackend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // Kullanıcıyı e-posta adresine göre bulmak için örnek bir method
    User findByEmail(String email);
}
