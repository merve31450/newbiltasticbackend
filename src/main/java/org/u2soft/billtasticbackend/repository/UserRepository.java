package org.u2soft.billtasticbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.u2soft.billtasticbackend.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);  // ❗ Statik değil, gövdesiz!
}
