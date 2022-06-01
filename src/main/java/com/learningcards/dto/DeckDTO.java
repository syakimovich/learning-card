package com.learningcards.dto;

public class DeckDTO {
    private long id;
    private String name;

    public DeckDTO(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public DeckDTO(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
