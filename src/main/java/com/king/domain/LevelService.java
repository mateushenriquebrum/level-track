package com.king.domain;

public class LevelService {
    private Repository repository;

    public LevelService(Repository repository) {
        this.repository = repository;
    }

    public String scores(Integer level) {
        var csv = repository
                .scoresOf(level)
                .stream()
                .map(us -> us.user()+"="+us.score())
                .toList();
        return String.join(",", csv);
    }

    public String score(Integer level, UserScore userScore) {
        repository.scoreTo(level, userScore);
        return "";
    }
}
