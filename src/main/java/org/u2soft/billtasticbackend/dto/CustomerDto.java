package org.u2soft.billtasticbackend.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDate;

public class CustomerDto {

    private Long id;
    private String companyName;
    private String contactName;
    private String invoiceEmail;

    @NotBlank(message = "Fatura numarası boş olamaz")
    @Pattern(
            regexp = "^[A-Za-z0-9]{3}[0-9]{15}$",
            message = "Fatura numarası 3 haneli alfa-nümerik birim kodu ve 15 haneli rakamdan oluşmalıdır (toplam 18 karakter)."
    )
    private String invoiceNumber;

    private Double euroAmount;
    private Double dollarAmount;
    private Double tlAmount;
    private String priority;
    private Double receivableTotal;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate creationDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate dueDate;

    // ---------- Getters / Setters ----------
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }

    public String getContactName() { return contactName; }
    public void setContactName(String contactName) { this.contactName = contactName; }

    public String getInvoiceEmail() { return invoiceEmail; }
    public void setInvoiceEmail(String invoiceEmail) { this.invoiceEmail = invoiceEmail; }

    public String getInvoiceNumber() { return invoiceNumber; }
    public void setInvoiceNumber(String invoiceNumber) { this.invoiceNumber = invoiceNumber; }

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

    public LocalDate getCreationDate() { return creationDate; }
    public void setCreationDate(LocalDate creationDate) { this.creationDate = creationDate; }

    public LocalDate getDueDate() { return dueDate; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }

    @Override
    public String toString() {
        return "CustomerDto{" +
                "id=" + id +
                ", companyName='" + companyName + '\'' +
                ", contactName='" + contactName + '\'' +
                ", invoiceEmail='" + invoiceEmail + '\'' +
                ", invoiceNumber='" + invoiceNumber + '\'' +
                ", euroAmount=" + euroAmount +
                ", dollarAmount=" + dollarAmount +
                ", tlAmount=" + tlAmount +
                ", priority='" + priority + '\'' +
                ", receivableTotal=" + receivableTotal +
                ", creationDate=" + creationDate +
                ", dueDate=" + dueDate +
                '}';
    }
}
