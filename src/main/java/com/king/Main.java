package com.king;

import com.king.application.*;
import com.king.application.Router.*;
import com.king.application.effect.*;

import java.io.*;
import java.util.*;
import java.util.logging.*;

import static com.king.application.Router.METHOD.GET;
import static com.king.application.Router.METHOD.POST;
import static java.util.List.of;

public class Main {

    private static Controllers controllers = ControllersFactory.create();
    private static List<Route> routes = of(
            new Route(GET, new Template("/<userid>/login"), req -> controllers.login(req)),
            new Route(GET, new Template("/<levelid>/highscorelist"), req -> controllers.scores(req)),
            new Route(POST, new Template("/<levelid>/score"), req -> controllers.score(req))
    );

    public static void main(String[] args) throws IOException {
        var logger = Logger.getGlobal();
        logger.addHandler(new ConsoleHandler());
        var port = args.length == 1 ? Integer.parseInt(args[0]) : 8080;
        var server = new Server(port, new Dispatcher(), new Router(routes));
        server.start();
    }
}