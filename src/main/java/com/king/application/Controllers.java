package com.king.application;

import com.king.domain.*;

import java.util.logging.*;

/**
 * The controller that still relly on infrastructure components but there are zero side effect, so can be tested safely
 */
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
        logger.info("Executing login with params "+params);
        var id = params.asInteger("userid").get();
        return token.generate(id);
    }

    public String scores(Params params) {
        logger.info("Executing scores with params "+params);
        var id = params.asInteger("levelid").get();
        return level.scores(id);
    }

    public String score(Params params) {
        logger.info("Executing score with params "+params);
        var levelId = params.asInteger("levelid").get();
        var key = params.asString("sessionkey").get();
        var score = params.asInteger("body").get();
        if (!token.valid(key)) throw new RuntimeException("Login expired");
        var id = token.user(key);
        return level.score(levelId, new UserScore(id, score));
    }

    private static Logger logger = Logger.getLogger(Controllers.class.getPackageName());
}
