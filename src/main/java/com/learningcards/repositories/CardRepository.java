package com.learningcards.repositories;

import com.learningcards.entities.Card;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CardRepository extends JpaRepository<Card, Long> {
    List<Card> findByDeckId(Long DeckId);
}
