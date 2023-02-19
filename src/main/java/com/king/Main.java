package com.king;

import com.king.application.*;
import com.king.application.Selector.*;
import com.king.application.effect.*;
import com.king.domain.*;

import java.io.*;
import java.security.*;
import java.util.*;
import java.util.logging.*;

import static com.king.application.Selector.METHOD.GET;
import static com.king.application.Selector.METHOD.POST;
import static java.util.List.of;

public class Main {

    private static Repository repository = new MemoryRepository();
    private static LoginService login = new LoginService(repository, null);
    private static LevelService level = new LevelService(login, repository);
    private static List<Select> mapping = of(
            new Select(GET, new Template("/<userid>/login"), req -> login.session(req.asInteger("userid").get())),
            new Select(GET, new Template("/<levelid>/highscorelist"), req -> level.scores(req.asString("levelid").get())),
            new Select(POST, new Template("/<levelid>/score"), req -> level.score(req.asString("levelid").get(), "something")) // ?sessionkey=<sessionkey> //body
    );

    public static void main(String[] args) throws IOException, NoSuchAlgorithmException {
        var logger = Logger.getGlobal();
        logger.addHandler(new ConsoleHandler());
        var port = args.length == 1 ? Integer.parseInt(args[0]) : 8080;
        var server = new Server(port, new Dispatcher(), new Selector(mapping));
        server.start();
    }
}