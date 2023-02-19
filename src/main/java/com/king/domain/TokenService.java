package com.king.domain;

import static java.time.LocalDateTime.*;
import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME;

public class TokenService {
    private Encryptor enc;
    private Repository repository;

    public TokenService(Encryptor enc, Repository repository) {
        this.enc = enc;
        this.repository = repository;
    }

    public String generate(Integer id) {
        var expire = now().plusMinutes(10);
        var token = id+"::"+expire.format(ISO_LOCAL_DATE_TIME);
        return enc.enc(token);
    }

    public Boolean valid(String token) {
        var raw = enc.dec(token).split("::");
        var expire = parse(raw[1], ISO_LOCAL_DATE_TIME);
        var id = Integer.parseInt(raw[0]);
        return expire.isBefore(now().plusMinutes(10)) && repository.exists(id);
    }
}
