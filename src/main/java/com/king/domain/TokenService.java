package com.king.domain;

import static java.lang.Integer.parseInt;
import static java.time.LocalDateTime.*;
import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME;

/**
 * Generate and validate token for a user, it is something like JWT but simpler
 */
public class TokenService {
    private Encryptor encryptor;

    public TokenService(Encryptor encryptor) {
        this.encryptor = encryptor;
    }

    /**
     * Generate an encrypted token
     * @param id User id
     * @return Enc token with id and expiration
     */
    public String generate(Integer id) {
        var expire = now().plusMinutes(10);
        var token = id+"::"+expire.format(ISO_LOCAL_DATE_TIME);
        return encryptor.enc(token);
    }

    /**
     * Validate token, verifying the current date time against token expiration
     * @param token The generated token
     * @return Valid?
     */
    public Boolean valid(String token) {
        var raw = encryptor.dec(token).split("::");
        var expire = parse(raw[1], ISO_LOCAL_DATE_TIME);
        return expire.isBefore(now().plusMinutes(10));
    }

    /**
     * Extract the user from a token, regardless it is expired or not
     * @param token The generated token
     * @return User id
     */
    public Integer user(String token) {
        var raw = encryptor.dec(token).split("::");
        return parseInt(raw[0]);
    }
}
