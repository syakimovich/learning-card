package com.learningcards.dto;

public class CardDTO {
    private Long id;
    private String front;
    private String back;

    public CardDTO(Long id, String front, String back) {
        this.id = id;
        this.front = front;
        this.back = back;
    }

    public CardDTO(String front, String back) {
        this.front = front;
        this.back = back;
    }

    public Long getId() {
        return id;
    }

    public String getFront() {
        return front;
    }

    public String getBack() {
        return back;
    }
}
