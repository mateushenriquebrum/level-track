package com.king.application;

import com.king.application.effect.*;
import com.king.domain.*;
import org.junit.jupiter.api.*;

import static java.util.Map.of;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ControllersTest {
    @Test
    void shouldGetHighScoreOnHappyPath() {
        var controllers = controllers();

        var john = controllers.login(new Params(of("userid", "1")));
        controllers.score(new Params(of("levelid", "999", "sessionkey", john, "body", "1020")));
        controllers.score(new Params(of("levelid", "888", "sessionkey", john, "body", "2020")));

        var barbara = controllers.login(new Params(of("userid", "2")));
        controllers.score(new Params(of("levelid", "888", "sessionkey", barbara, "body", "1020")));
        controllers.score(new Params(of("levelid", "999", "sessionkey", barbara, "body", "2020")));

        var paul = controllers.login(new Params(of("userid", "3")));
        controllers.score(new Params(of("levelid", "888", "sessionkey", paul, "body", "100")));
        controllers.score(new Params(of("levelid", "999", "sessionkey", paul, "body", "100")));

        var highestNines = controllers.scores(new Params(of("levelid", "999")));
        var highestEights = controllers.scores(new Params(of("levelid", "888")));
        assertEquals("2=2020,1=1020,3=100", highestNines);
        assertEquals("1=2020,2=1020,3=100", highestEights);
    }

    private Controllers controllers() {
        var repository = new MemoryRepository();
        return new Controllers(repository, new TokenService(new AESEncryptor()), new LevelService(repository));
    }
}
