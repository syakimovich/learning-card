package com.learningcards.repositories;

import com.learningcards.entities.Card;
import com.learningcards.entities.CardLearningState;
import com.learningcards.entities.Deck;
import com.learningcards.entities.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Sql(statements = "CREATE SEQUENCE IF NOT EXISTS HIBERNATE_SEQUENCE START WITH 1 INCREMENT BY 1;")
class CardLearningStateRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CardLearningStateRepository cardLearningStateRepository;

    @Test
    void findByUsernameAndCardId() {
        User user = new User("user1", "pass1", "USER", true);
        Deck deck = new Deck(user, "deck1");
        Card card = new Card(deck, "front1", "back1");
        CardLearningState learningState = new CardLearningState(card, user, true, false);
        entityManager.persist(user);
        entityManager.persist(deck);
        entityManager.persist(card);
        entityManager.persist(learningState);
        entityManager.flush();

        CardLearningState foundLearningState = cardLearningStateRepository.findByUsernameAndCardId(user.getUsername(), card.getId());

        assertEquals(learningState.isInLearning(), foundLearningState.isInLearning());
        assertEquals(learningState.getCard().getFront(), card.getFront());
        assertEquals(learningState.getCard().getBack(), card.getBack());
        assertEquals(learningState.getUser().getRole(), user.getRole());
        assertEquals(learningState.getUser().getUsername(), user.getUsername());
    }

    @Test
    void findAllByDeckIdAndUsername() {
        User user = new User("user1", "pass1", "USER", true);
        Deck deck = new Deck(user, "deck1");
        Card card1 = new Card(deck, "front1", "back1");
        Card card2 = new Card(deck, "front2", "back2");
        CardLearningState learningState1 = new CardLearningState(card1, user, true, false);
        CardLearningState learningState2 = new CardLearningState(card2, user, true, false);
        entityManager.persist(user);
        entityManager.persist(deck);
        entityManager.persist(card1);
        entityManager.persist(card2);
        entityManager.persist(learningState1);
        entityManager.persist(learningState2);
        entityManager.flush();

        List<CardLearningState> learningStateList = cardLearningStateRepository.findAllByDeckIdAndUsername(deck.getId(), user.getUsername());
        assertEquals(2, learningStateList.size());
        assertEquals(learningState1.getId(), learningStateList.get(0).getId());
        assertEquals(learningState1.isInLearning(), learningStateList.get(0).isInLearning());
        assertEquals(user.getUsername(), learningStateList.get(0).getUser().getUsername());
        assertEquals(card1.getFront(), learningStateList.get(0).getCard().getFront());
        assertEquals(deck.getName(), learningStateList.get(0).getCard().getDeck().getName());

        assertEquals(learningState2.getId(), learningStateList.get(1).getId());
        assertEquals(learningState2.isInLearning(), learningStateList.get(1).isInLearning());
        assertEquals(user.getUsername(), learningStateList.get(1).getUser().getUsername());
        assertEquals(card2.getFront(), learningStateList.get(1).getCard().getFront());
        assertEquals(deck.getName(), learningStateList.get(1).getCard().getDeck().getName());
    }
}