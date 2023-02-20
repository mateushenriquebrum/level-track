package com.king.application;

import com.king.domain.*;

public class Controllers {

    private Repository repository;
    private TokenService token;
    private LevelService level;

    public Controllers(Repository repository, TokenService token, LevelService level) {
        this.repository = repository;
        this.token = token;
        this.level = level;
    }

    public String login(Params params) {
        var id = params.asInteger("userid").get();
        return token.generate(id);
    }

    public String scores(Params params) {
        var id = params.asInteger("levelid").get();
        return level.scores(id);
    }

    public String score(Params params) {
        var levelId = params.asInteger("levelid").get();
        var key = params.asString("sessionkey").get();
        var score = params.asInteger("body").get();
        if (!token.valid(key)) throw new RuntimeException("Login expired");
        var id = token.user(key);
        return level.score(levelId, new UserScore(id, score));
    }
}
