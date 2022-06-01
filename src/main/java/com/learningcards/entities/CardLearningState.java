package com.learningcards.entities;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
public class CardLearningState {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "card_id")
    private Card card;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private boolean isInLearning;

    private Timestamp toRepeat;

    public CardLearningState() {
    }

    public CardLearningState(Card card, User user, boolean isInLearning) {
        this.card = card;
        this.user = user;
        this.isInLearning = isInLearning;
    }

    public Long getId() {
        return id;
    }

    public Card getCard() {
        return card;
    }

    public User getUser() {
        return user;
    }

    public boolean isInLearning() {
        return isInLearning;
    }

    public void setInLearning(boolean inLearning) {
        isInLearning = inLearning;
    }

    public Timestamp getToRepeat() {
        return toRepeat;
    }

    public void setToRepeat(Timestamp toRepeat) {
        this.toRepeat = toRepeat;
    }
}
