package org.u2soft.billtasticbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.u2soft.billtasticbackend.entity.Customer;
import org.u2soft.billtasticbackend.entity.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {


    List<Customer> findByUser(User user);


    Optional<Customer> findByIdAndUser(Long id, User user);
}
