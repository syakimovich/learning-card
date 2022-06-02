package com.learningcards.repositories;

import com.learningcards.entities.CardLearningState;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CardLearningStateRepository extends JpaRepository<CardLearningState, Long> {
    @Query("select cls from CardLearningState cls where cls.user.username = :username and cls.card.id = :cardId")
    CardLearningState findByUsernameAndCardId(String username, Long cardId);

    @Query("select cls from CardLearningState cls where cls.user.username = :username and cls.card.deck.id = :deckId")
    List<CardLearningState> findAllByDeckIdAndUsername(Long deckId, String username);

    @Query("select count(cls) from CardLearningState cls where cls.card.deck.id = :deckId " +
            "and cls.user.username = :username and cls.isInLearning = false")
    int countNewToLearn(Long deckId, String username);

    @Query("select cls from CardLearningState cls where cls.card.deck.id = :deckId " +
            "and cls.user.username = :username and cls.isInLearning = false")
    List<CardLearningState> findNextNewToLearn(Long deckId, String username, Pageable pageable);
}
