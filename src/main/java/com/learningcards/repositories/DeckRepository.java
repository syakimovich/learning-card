package com.learningcards.repositories;

import com.learningcards.entities.Deck;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DeckRepository extends JpaRepository<Deck, Long> {
    @Query("select d from Deck d where d.user.username = :username")
    List<Deck> findByUsername(String username);
}
