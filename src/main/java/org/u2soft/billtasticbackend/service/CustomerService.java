package org.u2soft.billtasticbackend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.u2soft.billtasticbackend.dto.CustomerDto;
import org.u2soft.billtasticbackend.entity.Customer;
import org.u2soft.billtasticbackend.entity.User;
import org.u2soft.billtasticbackend.mapper.CustomerMapper;
import org.u2soft.billtasticbackend.repository.CustomerRepository;
import org.u2soft.billtasticbackend.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final UserRepository userRepository;

    /* =====================================================
       1️⃣ ADMIN TÜM MÜŞTERİLERİ GÖRÜR
       ===================================================== */
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    public Optional<Customer> findById(Long id) {
        return customerRepository.findById(id);
    }

    public boolean exists(Long id) {
        return customerRepository.existsById(id);
    }

    public void delete(Long id) {
        customerRepository.deleteById(id);
    }

    /* =====================================================
       2️⃣ SADECE GİRİŞ YAPAN KULLANICIYA GÖRE MÜŞTERİLER
       ===================================================== */
    public List<CustomerDto> getAllCustomersByUser(String email) {
        User user = Optional.ofNullable(userRepository.findByEmail(email))
                .orElseThrow(() -> new RuntimeException("User not found"));

        return customerRepository.findByUser(user)
                .stream()
                .map(CustomerMapper::toDto)
                .toList();
    }

    public Optional<CustomerDto> findByIdAndUser(Long id, String email) {
        User user = Optional.ofNullable(userRepository.findByEmail(email))
                .orElseThrow(() -> new RuntimeException("User not found"));

        return customerRepository.findByIdAndUser(id, user)
                .map(CustomerMapper::toDto);
    }

    public CustomerDto createCustomerForUser(CustomerDto dto, String email) {
        User user = Optional.ofNullable(userRepository.findByEmail(email))
                .orElseThrow(() -> new RuntimeException("User not found"));

        Customer entity = CustomerMapper.toEntity(dto);
        entity.setUser(user);
        Customer saved = customerRepository.save(entity);

        return CustomerMapper.toDto(saved);
    }

    public Optional<CustomerDto> updateCustomerForUser(CustomerDto dto, String email) {
        User user = Optional.ofNullable(userRepository.findByEmail(email))
                .orElseThrow(() -> new RuntimeException("User not found"));

        return customerRepository.findByIdAndUser(dto.getId(), user)
                .map(existing -> {
                    existing.setCompanyName(dto.getCompanyName());
                    existing.setContactName(dto.getContactName());
                    existing.setInvoiceEmail(dto.getInvoiceEmail());
                    existing.setInvoiceNumber(dto.getInvoiceNumber());
                    existing.setEuroAmount(dto.getEuroAmount());
                    existing.setDollarAmount(dto.getDollarAmount());
                    existing.setTlAmount(dto.getTlAmount());
                    existing.setPriority(dto.getPriority());
                    existing.setReceivableTotal(dto.getReceivableTotal());

                    return CustomerMapper.toDto(customerRepository.save(existing));
                });
    }

    public boolean deleteCustomerForUser(Long id, String email) {
        User user = Optional.ofNullable(userRepository.findByEmail(email))
                .orElseThrow(() -> new RuntimeException("User not found"));

        return customerRepository.findByIdAndUser(id, user)
                .map(customer -> {
                    customerRepository.delete(customer);
                    return true;
                })
                .orElse(false);
    }
}
