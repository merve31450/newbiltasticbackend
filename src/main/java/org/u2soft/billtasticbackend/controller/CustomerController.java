package org.u2soft.billtasticbackend.controller;

import org.u2soft.billtasticbackend.dto.CustomerDto;
import org.u2soft.billtasticbackend.entity.Customer;
import org.u2soft.billtasticbackend.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private final CustomerService customerService;

    @Autowired
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    // Tüm müşterileri listeleme
    @GetMapping
    public List<CustomerDto> getAllCustomers() {
        return customerService.getAllCustomers().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // Yeni müşteri oluşturma
    @PostMapping
    public CustomerDto createCustomer(@RequestBody CustomerDto customerDto) {
        Customer customer = convertToEntity(customerDto);
        Customer createdCustomer = customerService.createCustomer(customer);
        return convertToDto(createdCustomer);
    }

    // Müşteriyi güncelleme
    @PutMapping("/{id}")
    public CustomerDto updateCustomer(@PathVariable Long id, @RequestBody CustomerDto customerDto) {
        Customer customer = convertToEntity(customerDto);
        Customer updatedCustomer = customerService.updateCustomer(id, customer);
        return convertToDto(updatedCustomer);
    }

    // Müşteriyi silme
    @DeleteMapping("/{id}")
    public void deleteCustomer(@PathVariable Long id) {
        customerService.deleteCustomer(id);
    }

    // DTO'dan Entity'ye dönüşüm
    private Customer convertToEntity(CustomerDto customerDto) {
        Customer customer = new Customer();
        customer.setId(customerDto.getId());
        customer.setCompanyName(customerDto.getCompanyName());
        customer.setContactName(customerDto.getContactName());
        customer.setInvoiceEmail(customerDto.getInvoiceEmail());
        customer.setInvoiceNumber(customerDto.getInvoiceNumber()); // Invoice numarası eklendi
        customer.setEuroAmount(customerDto.getEuroAmount());
        customer.setDollarAmount(customerDto.getDollarAmount());
        customer.setTlAmount(customerDto.getTlAmount());
        customer.setPriority(customerDto.getPriority());
        customer.setReceivableTotal(customerDto.getReceivableTotal());
        return customer;
    }

    // Entity'den DTO'ya dönüşüm
    private CustomerDto convertToDto(Customer customer) {
        CustomerDto customerDto = new CustomerDto();
        customerDto.setId(customer.getId());
        customerDto.setCompanyName(customer.getCompanyName());
        customerDto.setContactName(customer.getContactName());
        customerDto.setInvoiceEmail(customer.getInvoiceEmail());
        customerDto.setInvoiceNumber(customer.getInvoiceNumber()); // Invoice numarası eklendi
        customerDto.setEuroAmount(customer.getEuroAmount());
        customerDto.setDollarAmount(customer.getDollarAmount());
        customerDto.setTlAmount(customer.getTlAmount());
        customerDto.setPriority(customer.getPriority());
        customerDto.setReceivableTotal(customer.getReceivableTotal());
        return customerDto;
    }
}