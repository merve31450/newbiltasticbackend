package org.u2soft.billtasticbackend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.u2soft.billtasticbackend.dto.CardRequestDto;
import org.u2soft.billtasticbackend.entity.Card;
import org.u2soft.billtasticbackend.service.CardService;

import java.util.List;

@RestController
@RequestMapping("/api/cards")
public class CardController {

    private final CardService cardService;

    @Autowired
    public CardController(CardService cardService) {
        this.cardService = cardService;
    }

    // Tüm kartları listele
    @GetMapping
    public List<Card> getAllCards() {
        return cardService.getAllCards();
    }

    // Yeni kart oluştur
    @PostMapping
    public Card createCard(@RequestBody CardRequestDto cardRequestDto) {
        return cardService.createCard(cardRequestDto);
    }

    // Kart bilgilerini güncelle
    @PutMapping("/{id}")
    public Card updateCard(@PathVariable Long id, @RequestBody CardRequestDto cardRequestDto) {
        return cardService.updateCard(id, cardRequestDto);
    }

    // Kartı sil
    @DeleteMapping("/{id}")
    public void deleteCard(@PathVariable Long id) {
        cardService.deleteCard(id);
    }
}
