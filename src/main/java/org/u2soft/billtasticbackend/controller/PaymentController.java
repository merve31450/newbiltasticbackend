package org.u2soft.billtasticbackend.controller;

import org.u2soft.billtasticbackend.entity.Payment;
import org.u2soft.billtasticbackend.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.u2soft.billtasticbackend.dto.PaymentRequestDto;
@RestController
@RequestMapping("/api/payments")

public class PaymentController {


    private final PaymentService paymentService;

    @Autowired
    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }


    @GetMapping
    public List<Payment> getAllPayments() {
        return paymentService.getAllPayments();
    }


    @PostMapping
    public Payment createPayment(@RequestBody PaymentRequestDto dto) {
        Payment payment = new Payment();
        payment.setAmount(Double.parseDouble(dto.getAmount()));
        payment.setCurrency(dto.getCurrency());
        payment.setTransactionId(dto.getTransactionId());
        payment.setCardNumber(dto.getCard().getCardNumber());
        payment.setCardHolderName(dto.getCard().getCardHolderName());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/yy");
        String expiryDateStr = dto.getCard().getExpiryDate().format(formatter);
        payment.setExpiryDate(expiryDateStr);

        payment.setCvc(dto.getCard().getCvc());

        payment.setStatus("PENDING");  // <-- Burayı ekledim

        return paymentService.createPayment(payment);
    }




    @PutMapping("/{id}")
    public Payment updatePayment(@PathVariable Long id, @RequestBody Payment payment) {
        return paymentService.updatePayment(id, payment);
    }


    @DeleteMapping("/{id}")
    public void deletePayment(@PathVariable Long id) {
        paymentService.deletePayment(id);
    }
}
