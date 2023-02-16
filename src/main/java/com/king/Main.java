package com.king;

public class Main {
    public static void main(String[] args) {
        var port = args.length == 1 ? Integer.parseInt(args[0]) : 8080;
    }
}