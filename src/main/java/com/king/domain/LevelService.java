package com.king.domain;

public class LevelService {
    private LoginService login;
    private Repository repository;

    public LevelService(LoginService login, Repository repository) {
        this.login = login;
        this.repository = repository;
    }

    public String scores(String level) {
        return null;
    }

    public String score(String level, String score) {
        return null;
    }
}
