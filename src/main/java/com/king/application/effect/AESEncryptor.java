package com.king.application.effect;

import com.king.domain.*;

import javax.crypto.*;
import static java.nio.charset.StandardCharsets.*;
import static java.util.Base64.getDecoder;
import static java.util.Base64.getEncoder;
import static javax.crypto.Cipher.DECRYPT_MODE;
import static javax.crypto.Cipher.ENCRYPT_MODE;
import static javax.crypto.KeyGenerator.getInstance;

public class AESEncryptor implements Encryptor {

    private SecretKey key;

    public AESEncryptor() {
        try {
            var gen = getInstance("AES");
            gen.init(128);
            key = gen.generateKey();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String enc(String raw) {
        try {
            var cipher = Cipher.getInstance("AES");
            cipher.init(ENCRYPT_MODE, key);
            var enc = getEncoder().encode(cipher.doFinal(raw.getBytes(US_ASCII)));
            return new String(enc, US_ASCII);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String dec(String sec) {
        try {
            var cipher = Cipher.getInstance("AES");
            cipher.init(DECRYPT_MODE, key);
            var enc = cipher.doFinal(getDecoder().decode(sec));
            return new String(enc, US_ASCII);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
