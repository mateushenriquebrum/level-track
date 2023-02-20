package com.king.domain;

import static java.lang.Integer.parseInt;
import static java.time.LocalDateTime.*;
import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME;

public class TokenService {
    private Encryptor encryptor;

    public TokenService(Encryptor encryptor) {
        this.encryptor = encryptor;
    }

    public String generate(Integer id) {
        var expire = now().plusMinutes(10);
        var token = id+"::"+expire.format(ISO_LOCAL_DATE_TIME);
        return encryptor.enc(token);
    }

    public Boolean valid(String token) {
        var raw = encryptor.dec(token).split("::");
        var expire = parse(raw[1], ISO_LOCAL_DATE_TIME);
        return expire.isBefore(now().plusMinutes(10));
    }

    public Integer user(String token) {
        var raw = encryptor.dec(token).split("::");
        return parseInt(raw[0]);
    }
}
