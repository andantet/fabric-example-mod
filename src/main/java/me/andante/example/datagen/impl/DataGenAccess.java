package me.andante.example.datagen.impl;

import java.io.IOException;
import java.util.function.Consumer;

public interface DataGenAccess {
    void run(Consumer<DataCacheAccess> configure) throws IOException;
}
