package com.king.application.effect;

import com.king.domain.*;

public class MemoryRepository implements Repository {

    @Override
    public boolean exists(int id) {
        return false;
    }
}
