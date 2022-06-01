package com.learningcards.entities;

import javax.persistence.*;

@Entity
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "deck_id")
    private Deck deck;

    private String front;
    private String back;

    public Card() {
    }

    public Card(Deck deck, String front, String back) {
        this.deck = deck;
        this.front = front;
        this.back = back;
    }

    public Long getId() {
        return id;
    }

    public Deck getDeck() {
        return deck;
    }

    public String getFront() {
        return front;
    }

    public String getBack() {
        return back;
    }
}
