package org.u2soft.billtasticbackend.dto;

import lombok.Data;

import java.util.List;

@Data
public class InvoiceRequest {

    private String companyName;
    private String address;
    private String phone;
    private String email;
    private String website;
    private String bankAccount;
    private String invoiceNo;
    private String date;
    private String totalAmount;

    private List<InvoiceItem> items;

    @Data
    public static class InvoiceItem {
        private String description;
        private String unitPrice;
        private String quantity;
    }
}
