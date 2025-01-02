package org.u2soft.billtasticbackend.dto;

public class PaymentRequestDto {
    private String amount;
    private String currency;
    private String transactionId;
    private CardRequestDto card;  // Kart bilgilerini almak için CardRequestDto kullanıyoruz

    // Getter ve Setter metotları
    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public CardRequestDto getCard() {
        return card;
    }

    public void setCard(CardRequestDto card) {
        this.card = card;
    }
}
