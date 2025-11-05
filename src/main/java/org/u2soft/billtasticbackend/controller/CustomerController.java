package org.u2soft.billtasticbackend.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.u2soft.billtasticbackend.dto.CustomerDto;
import org.u2soft.billtasticbackend.service.CustomerService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/customers")
@CrossOrigin(origins = "http://localhost:3000")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    /* =====================================================
       1️ GİRİŞ YAPAN KULLANICININ TÜM MÜŞTERİLERİ
       ===================================================== */
    @GetMapping
    public ResponseEntity<List<CustomerDto>> getAllForCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName(); // Token'dan email alınıyor

        List<CustomerDto> customers = customerService.getAllCustomersByUser(email);
        return ResponseEntity.ok(customers);
    }

    /* =====================================================
       2️TEK MÜŞTERİ GETİR (ID + GİRİŞ YAPAN KULLANICI)
       ===================================================== */
    @GetMapping("/{id}")
    public ResponseEntity<CustomerDto> getCustomerById(@PathVariable Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();

        Optional<CustomerDto> customer = customerService.findByIdAndUser(id, email);
        return customer.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /* =====================================================
       3️MÜŞTERİ EKLE (OTOMATİK USER İLİŞKİSİ)
       ===================================================== */
    @PostMapping
    public ResponseEntity<CustomerDto> createCustomer(
            @Valid @RequestBody CustomerDto customerDto) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();

        CustomerDto created = customerService.createCustomerForUser(customerDto, email);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    /* =====================================================
       4️ MÜŞTERİ GÜNCELLE (YALNIZCA SAHİBİ GÜNCELLEYEBİLİR)
       ===================================================== */
    @PutMapping("/{id}")
    public ResponseEntity<CustomerDto> updateCustomer(@PathVariable Long id,
                                                      @RequestBody CustomerDto customerDto) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();

        customerDto.setId(id);
        Optional<CustomerDto> updated = customerService.updateCustomerForUser(customerDto, email);
        return updated.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /* =====================================================
       5️ MÜŞTERİ SİL (YALNIZCA SAHİBİ SİLEBİLİR)
       ===================================================== */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();

        boolean deleted = customerService.deleteCustomerForUser(id, email);
        return deleted ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }
}
