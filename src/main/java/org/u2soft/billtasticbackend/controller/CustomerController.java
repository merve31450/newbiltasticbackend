package org.u2soft.billtasticbackend.controller;

import lombok.RequiredArgsConstructor;
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
       1️⃣ GİRİŞ YAPAN KULLANICININ TÜM MÜŞTERİLERİ
       ===================================================== */
    @GetMapping
    public ResponseEntity<List<CustomerDto>> getAllForCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName(); // Token'dan email alınıyor 🔥

        List<CustomerDto> customers = customerService.getAllCustomersByUser(email);
        return ResponseEntity.ok(customers);
    }

    /* =====================================================
       2️⃣ TEK MÜŞTERİ GETİR (ID + GİRİŞ YAPAN KULLANICI)
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
       3️⃣ MÜŞTERİ EKLE (OTOMATİK USER İLİŞKİSİ)
       ===================================================== */
    @PostMapping
    public ResponseEntity<CustomerDto> createCustomer(@RequestBody CustomerDto customerDto) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();

        CustomerDto created = customerService.createCustomerForUser(customerDto, email);
        return ResponseEntity.ok(created);
    }

    /* =====================================================
       4️⃣ MÜŞTERİ GÜNCELLE (YALNIZCA SAHİBİ GÜNCELLEYEBİLİR)
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
       5️⃣ MÜŞTERİ SİL (YALNIZCA SAHİBİ SİLEBİLİR)
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

