package org.u2soft.billtasticbackend.controller;
import org.springframework.http.ResponseEntity;

import org.u2soft.billtasticbackend.dto.CustomerDto;
import org.u2soft.billtasticbackend.entity.Customer;
import org.u2soft.billtasticbackend.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/customers")
@CrossOrigin(origins = "http://localhost:3000")
public class CustomerController {

    private final CustomerService customerService;

    @Autowired
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping
    public List<CustomerDto> getAllCustomers() {
        return customerService.getAllCustomers().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @PostMapping
    public CustomerDto createCustomer(@RequestBody CustomerDto customerDto) {
        System.out.println(">>> GELEN DTO: " + customerDto);
        try {
            Customer customer = convertToEntity(customerDto);
            Customer createdCustomer = customerService.createCustomer(customer);
            return convertToDto(createdCustomer);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @PutMapping("/{id}")
    public CustomerDto updateCustomer(@PathVariable Long id, @RequestBody CustomerDto customerDto) {
        Customer customer = convertToEntity(customerDto);
        Customer updatedCustomer = customerService.updateCustomer(id, customer);
        return convertToDto(updatedCustomer);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Long id) {

        System.out.println(">>> SİLİYORUZ: ID = " + id);
        customerService.deleteCustomer(id);
        return ResponseEntity.noContent().build();
    }



    private Customer convertToEntity(CustomerDto dto) {
        Customer entity = new Customer();
        entity.setId(dto.getId());
        entity.setCompanyName(dto.getCompanyName());
        entity.setContactName(dto.getContactName());
        entity.setInvoiceEmail(dto.getInvoiceEmail());
        entity.setInvoiceNumber(dto.getInvoiceNumber());
        entity.setEuroAmount(dto.getEuroAmount());
        entity.setDollarAmount(dto.getDollarAmount());
        entity.setTlAmount(dto.getTlAmount());
        entity.setPriority(dto.getPriority());
        entity.setReceivableTotal(dto.getReceivableTotal());

        if (dto.getCreationDate() != null) {
            entity.setCreationDate(java.sql.Date.valueOf(dto.getCreationDate()));
        }
        if (dto.getDueDate() != null) {
            entity.setDueDate(java.sql.Date.valueOf(dto.getDueDate()));
        }

        return entity;
    }

    private CustomerDto convertToDto(Customer entity) {
        CustomerDto dto = new CustomerDto();
        dto.setId(entity.getId());
        dto.setCompanyName(entity.getCompanyName());
        dto.setContactName(entity.getContactName());
        dto.setInvoiceEmail(entity.getInvoiceEmail());
        dto.setInvoiceNumber(entity.getInvoiceNumber());
        dto.setEuroAmount(entity.getEuroAmount());
        dto.setDollarAmount(entity.getDollarAmount());
        dto.setTlAmount(entity.getTlAmount());
        dto.setPriority(entity.getPriority());
        dto.setReceivableTotal(entity.getReceivableTotal());

        if (entity.getCreationDate() != null) {
            dto.setCreationDate(new java.sql.Date(entity.getCreationDate().getTime()).toLocalDate());
        }
        if (entity.getDueDate() != null) {
            dto.setDueDate(new java.sql.Date(entity.getDueDate().getTime()).toLocalDate());
        }

        return dto;
    }
}

