package com.king.application;

import com.king.application.effect.*;
import com.king.domain.*;

public class ControllersFactory {
    private static Repository repository = new MemoryRepository();
    private static TokenService token = new TokenService(new AESEncryptor());
    private static LevelService level = new LevelService(repository);

    public static Controllers create() {
        return new Controllers(repository, token, level);
    }
}
