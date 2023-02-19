package com.king.domain;

public class LoginService {
    private Repository repository;
    private Token token;

    public LoginService(Repository repository, Token token) {
        this.repository = repository;
        this.token = token;
    }

    public String session(Integer id) {
        return token.generate(id);
    }

    public Boolean verify(String raw)
    {
        return token.valid(raw);
    }
}
