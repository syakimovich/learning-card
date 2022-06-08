package com.learningcards.dto;

public class ReviewTimeDTO {
    private long time;
    private String toShow;

    public ReviewTimeDTO(long time, String toShow) {
        this.time = time;
        this.toShow = toShow;
    }

    public long getTime() {
        return time;
    }

    public String getToShow() {
        return toShow;
    }
}
