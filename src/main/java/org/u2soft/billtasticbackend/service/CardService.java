package org.u2soft.billtasticbackend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.u2soft.billtasticbackend.dto.CardRequestDto;
import org.u2soft.billtasticbackend.entity.Card;
import org.u2soft.billtasticbackend.repository.CardRepository;

import java.util.List;
import java.util.Optional;

@Service
public class CardService {

    private final CardRepository cardRepository;

    @Autowired
    public CardService(CardRepository cardRepository) {
        this.cardRepository = cardRepository;
    }

    // Tüm kartları listele
    public List<Card> getAllCards() {
        return cardRepository.findAll();
    }

    // Yeni kart oluştur
    public Card createCard(CardRequestDto cardRequestDto) {
        Card card = new Card();
        card.setCardNumber(cardRequestDto.getCardNumber());
        card.setCardHolderName(cardRequestDto.getCardHolderName());
        card.setExpirationDate(cardRequestDto.getExpirationDate());
        card.setCvc(cardRequestDto.getCvc());
        return cardRepository.save(card);
    }

    // Kart bilgilerini güncelle
    public Card updateCard(Long id, CardRequestDto cardRequestDto) {
        Optional<Card> existingCard = cardRepository.findById(id);
        if (existingCard.isPresent()) {
            Card card = existingCard.get();
            card.setCardNumber(cardRequestDto.getCardNumber());
            card.setCardHolderName(cardRequestDto.getCardHolderName());
            card.setExpirationDate(cardRequestDto.getExpirationDate());
            card.setCvc(cardRequestDto.getCvc());
            return cardRepository.save(card);
        } else {
            throw new RuntimeException("Card not found with id " + id);
        }
    }

    // Kartı sil
    public void deleteCard(Long id) {
        if (cardRepository.existsById(id)) {
            cardRepository.deleteById(id);
        } else {
            throw new RuntimeException("Card not found with id " + id);
        }
    }
}
