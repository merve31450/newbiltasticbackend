package org.u2soft.billtasticbackend.dto;

public class CustomerDto {

    private Long id;
    private String companyName;
    private String contactName;
    private String invoiceEmail;
    private String invoiceNumber; // Fatura numarası
    private Double euroAmount;
    private Double dollarAmount;
    private Double tlAmount;
    private String priority;
    private Double receivableTotal;

    // Getter ve Setter metodları
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getInvoiceEmail() {
        return invoiceEmail;
    }

    public void setInvoiceEmail(String invoiceEmail) {
        this.invoiceEmail = invoiceEmail;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public Double getEuroAmount() {
        return euroAmount;
    }

    public void setEuroAmount(Double euroAmount) {
        this.euroAmount = euroAmount;
    }

    public Double getDollarAmount() {
        return dollarAmount;
    }

    public void setDollarAmount(Double dollarAmount) {
        this.dollarAmount = dollarAmount;
    }

    public Double getTlAmount() {
        return tlAmount;
    }

    public void setTlAmount(Double tlAmount) {
        this.tlAmount = tlAmount;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public Double getReceivableTotal() {
        return receivableTotal;
    }

    public void setReceivableTotal(Double receivableTotal) {
        this.receivableTotal = receivableTotal;
    }
}
