package com.learningcards.services;

import com.learningcards.dto.CardDTO;
import com.learningcards.entities.Card;
import com.learningcards.entities.CardLearningState;
import com.learningcards.entities.Deck;
import com.learningcards.repositories.CardLearningStateRepository;
import com.learningcards.repositories.CardRepository;
import com.learningcards.repositories.DeckRepository;
import com.learningcards.repositories.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CardService {

    private CardRepository cardRepository;
    private DeckRepository deckRepository;
    private CardLearningStateRepository learningStateRepository;
    private UserRepository userRepository;

    public CardService(CardRepository cardRepository, DeckRepository deckRepository, CardLearningStateRepository learningStateRepository, UserRepository userRepository) {
        this.cardRepository = cardRepository;
        this.deckRepository = deckRepository;
        this.learningStateRepository = learningStateRepository;
        this.userRepository = userRepository;
    }

    public List<CardDTO> findDeckCards(long deckId) {
        return cardRepository.findByDeckId(deckId).stream()
                .map(c -> new CardDTO(c.getId(), c.getFront(), c.getBack())).toList();
    }

    @Transactional
    public void addCard(CardDTO cardDTO, long deckId) {
        Deck deck = deckRepository.findById(deckId).orElseThrow(() -> new RuntimeException("Deck with id %d not found".formatted(deckId)));
        Card newCard = new Card(deck, cardDTO.getFront(), cardDTO.getBack());
        CardLearningState learningState = new CardLearningState(newCard, deck.getUser(), false, false);
        cardRepository.save(newCard);
        learningStateRepository.save(learningState);
    }
}
