package org.u2soft.billtasticbackend.dto;

import jakarta.validation.constraints.Pattern;
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
    @Pattern(
            regexp = "^[A-Za-z0-9]{3}[0-9]{15}$",
            message = "Fatura numarası 3 haneli alfa-nümerik birim kodu ve 15 haneli rakamdan oluşmalıdır (toplam 18 karakter)."
    )
    private String invoiceId;
}
