package com.king.domain;


public record UserScore(Integer user, Integer score) implements Comparable<UserScore> {
    @Override
    public int compareTo(UserScore o) {
        return o.score.compareTo(score);
    }
}