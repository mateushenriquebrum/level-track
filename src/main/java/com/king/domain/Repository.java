package com.king.domain;

import java.util.*;

public interface Repository {
    void scoreTo(Integer level, UserScore userScore);

    List<UserScore> scoresOf(Integer level);
}
