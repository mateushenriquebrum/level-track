package com.king;

import com.king.application.*;
import com.king.application.Resolver.*;
import com.king.application.effect.*;
import com.king.domain.*;

import java.io.*;
import java.util.*;

import static com.king.application.Resolver.METHOD.GET;
import static com.king.application.Resolver.METHOD.POST;
import static java.util.List.of;

public class Main {

    private static Repository repository = new MemoryRepository();
    private static LoginService login = new LoginService(repository);
    private static LevelService level = new LevelService(repository);
    private static List<Resolve> mapping = of(
            new Resolve(GET, "/<userid>/login", req -> login.session(req.get("userid"))),
            new Resolve(GET, "/<levelid>/highscorelist", req -> level.scores(req.get("levelid"))),
            new Resolve(POST, "/<levelid>/score", req -> level.score(req.get("levelid"), "something")) // ?sessionkey=<sessionkey> //body
    );

    public static void main(String[] args) throws IOException {
        var port = args.length == 1 ? Integer.parseInt(args[0]) : 8080;
        var server = new Server(port, new Dispatcher(new Resolver(mapping)));
        server.start();
    }
}