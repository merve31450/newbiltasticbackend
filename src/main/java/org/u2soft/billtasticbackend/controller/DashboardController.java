package org.u2soft.billtasticbackend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.u2soft.billtasticbackend.repository.CustomerRepository;
import org.u2soft.billtasticbackend.repository.TaskRepository;
import org.u2soft.billtasticbackend.repository.InvoiceRepository;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final CustomerRepository customerRepository;
    private final TaskRepository taskRepository;
    private final InvoiceRepository invoiceRepository;

    @GetMapping("/summary")
    public Map<String, Object> getDashboardSummary() {
        Map<String, Object> summary = new HashMap<>();

        summary.put("totalCustomers", customerRepository.count());
        summary.put("totalTasks", taskRepository.count());
        summary.put("completedTasks", taskRepository.countByCompletedTrue()); // ✅ Düzelttik
        summary.put("totalInvoices", invoiceRepository.count());

        return summary;
    }
}
