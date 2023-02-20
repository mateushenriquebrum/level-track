package com.king.application.effect;

import com.king.domain.*;

import java.util.*;

public class MemoryRepository implements Repository {

    private Map<Integer, Set<UserScore>> userScoreByLevel = new HashMap<>();

    @Override
    public void scoreTo(Integer level, UserScore userScore) {
        userScoreByLevel.putIfAbsent(level, new TreeSet<>());
        userScoreByLevel.get(level).add(userScore);
    }

    @Override
    public List<UserScore> scoresOf(Integer level) {
        return userScoreByLevel
                .get(level)
                .stream()
                .map(UserDistinction::new)
                .distinct()
                .map(UserDistinction::unwrap)
                .limit(15)
                .toList();
    }

    record UserDistinction(UserScore us) {
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof UserDistinction userDistinction)) return false;
            return Objects.equals(us.user(), userDistinction.us.user());
        }

        @Override
        public int hashCode() {
            return Objects.hash(us.user());
        }

        public UserScore unwrap() {
            return us;
        }
    }
}
