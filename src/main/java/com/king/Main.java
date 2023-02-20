package com.king;

import com.king.application.*;
import com.king.application.Router.*;
import com.king.application.effect.*;
import com.king.domain.*;

import java.io.*;
import java.util.*;
import java.util.logging.*;

import static com.king.application.Router.METHOD.GET;
import static com.king.application.Router.METHOD.POST;
import static java.util.List.of;

public class Main {

    private static Repository repository = new MemoryRepository();
    private static TokenService token = new TokenService(new AESEncryptor());
    private static LevelService level = new LevelService(repository);
    private static List<Route> routes = of(
            new Route(GET, new Template("/<userid>/login"), req -> {
                var id = req.asInteger("userid").get();
                return token.generate(id);
            }),
            new Route(GET, new Template("/<levelid>/highscorelist"), req -> {
                var id = req.asInteger("levelid").get();
                return level.scores(id);
            }),
            new Route(POST, new Template("/<levelid>/score"), req -> {
                var levelId = req.asInteger("levelid").get();
                var key = req.asString("sessionkey").get();
                var score = req.asInteger("body").get();
                if (!token.valid(key)) throw new RuntimeException("Login expired");
                var id = token.user(key);
                return level.score(levelId, new UserScore(id, score));
            })
    );

    public static void main(String[] args) throws IOException {
        var logger = Logger.getGlobal();
        logger.addHandler(new ConsoleHandler());
        var port = args.length == 1 ? Integer.parseInt(args[0]) : 8080;
        var server = new Server(port, new Dispatcher(), new Router(routes));
        server.start();
    }
}