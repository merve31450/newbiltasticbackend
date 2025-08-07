package org.u2soft.billtasticbackend.entity;

import jakarta.persistence.*;
import java.util.Date;


@Entity
@Table(name = "customers")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "company_name", nullable = false)
    private String companyName;

    @Column(name = "contact_name", nullable = false)
    private String contactName;

    @Column(name = "invoice_number", nullable = false)
    private String invoiceNumber;

    @Column(name = "due_date")
    private Date dueDate;

    @Column(name = "creation_date")
    private Date creationDate;

    @Column(name = "euro_amount")
    private Double euroAmount;

    @Column(name = "dollar_amount")
    private Double dollarAmount;

    @Column(name = "tl_amount")
    private Double tlAmount;

    @Column(name = "priority")
    private String priority;

    @Column(name = "receivable_total")
    private Double receivableTotal;

    @Column(name = "invoice_email", nullable = false, unique = true)
    private String invoiceEmail;

    // Getter / Setter
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }

    public String getContactName() { return contactName; }
    public void setContactName(String contactName) { this.contactName = contactName; }

    public String getInvoiceNumber() { return invoiceNumber; }
    public void setInvoiceNumber(String invoiceNumber) { this.invoiceNumber = invoiceNumber; }

    public Date getDueDate() { return dueDate; }
    public void setDueDate(Date dueDate) { this.dueDate = dueDate; }

    public Date getCreationDate() { return creationDate; }
    public void setCreationDate(Date creationDate) { this.creationDate = creationDate; }

    public Double getEuroAmount() { return euroAmount; }
    public void setEuroAmount(Double euroAmount) { this.euroAmount = euroAmount; }

    public Double getDollarAmount() { return dollarAmount; }
    public void setDollarAmount(Double dollarAmount) { this.dollarAmount = dollarAmount; }

    public Double getTlAmount() { return tlAmount; }
    public void setTlAmount(Double tlAmount) { this.tlAmount = tlAmount; }

    public String getPriority() { return priority; }
    public void setPriority(String priority) { this.priority = priority; }

    public Double getReceivableTotal() { return receivableTotal; }
    public void setReceivableTotal(Double receivableTotal) { this.receivableTotal = receivableTotal; }

    public String getInvoiceEmail() { return invoiceEmail; }
    public void setInvoiceEmail(String invoiceEmail) { this.invoiceEmail = invoiceEmail; }
}
