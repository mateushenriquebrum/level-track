package com.king.domain;

public class LoginService {
    private Repository repository;

    public LoginService(Repository repository) {
        this.repository = repository;
    }

    public String session(String id) {
        return null;
    }

    public Boolean verify(String token)
    {
        return null;
    }
}
