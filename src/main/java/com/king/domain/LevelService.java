package com.king.domain;

import static java.lang.String.join;

/**
 * Service responsible to deal with level, actually it is a more CRUD
 */
public class LevelService {
    private Repository repository;

    public LevelService(Repository repository) {
        this.repository = repository;
    }

    /**
     *
     * @param level create csv/text format
     * @return csv
     */
    public String scores(Integer level) {
        var csv = repository
                .scoresOf(level)
                .stream()
                .map(us -> us.user()+"="+us.score())
                .toList();
        return join(",", csv);
    }

    public String score(Integer level, UserScore userScore) {
        repository.scoreTo(level, userScore);
        return "";
    }
}
