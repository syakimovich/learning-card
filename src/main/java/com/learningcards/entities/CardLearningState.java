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

    private boolean isLearned;

    private Timestamp toReview;

    public CardLearningState() {
    }

    public CardLearningState(Card card, User user, boolean isInLearning, boolean isLearned) {
        this.card = card;
        this.user = user;
        this.isInLearning = isInLearning;
        this.isLearned = isLearned;
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

    public boolean isLearned() {
        return isLearned;
    }

    public void setLearned(boolean learned) {
        isLearned = learned;
    }

    public Timestamp getToReview() {
        return toReview;
    }

    public void setToReview(Timestamp toReview) {
        this.toReview = toReview;
    }
}
