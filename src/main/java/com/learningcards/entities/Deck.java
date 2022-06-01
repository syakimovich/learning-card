package com.learningcards.entities;

import javax.persistence.*;

@Entity
public class Deck {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String name;

    public Deck() {
    }

    public Deck(User user, String name) {
        this.user = user;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public String getName() {
        return name;
    }
}
