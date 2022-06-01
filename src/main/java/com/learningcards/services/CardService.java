package com.learningcards.services;

import com.learningcards.dto.CardDTO;
import com.learningcards.entities.Card;
import com.learningcards.entities.Deck;
import com.learningcards.repositories.CardRepository;
import com.learningcards.repositories.DeckRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CardService {

    private CardRepository cardRepository;
    private DeckRepository deckRepository;

    public CardService(CardRepository cardRepository, DeckRepository deckRepository) {
        this.cardRepository = cardRepository;
        this.deckRepository = deckRepository;
    }

    public List<CardDTO> findDeckCards(long deckId) {
        return cardRepository.findByDeckId(deckId).stream()
                .map(c -> new CardDTO(c.getId(), c.getFront(), c.getBack())).toList();
    }

    public void addCard(CardDTO cardDTO, long deckId) {
        Deck deck = deckRepository.getReferenceById(deckId);
        cardRepository.save(new Card(deck, cardDTO.getFront(), cardDTO.getBack()));
    }
}
