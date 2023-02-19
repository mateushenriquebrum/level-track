package com.king.domain;

public class LoginService {
    private Repository repository;
    private TokenService tokenService;

    public LoginService(Repository repository, TokenService tokenService) {
        this.repository = repository;
        this.tokenService = tokenService;
    }

    public String session(Integer id) {
        return tokenService.generate(id);
    }

    public Boolean verify(String raw)
    {
        return tokenService.valid(raw);
    }
}
