package me.andante.example.datagen.provider;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import net.minecraft.data.DataCache;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

public abstract class AbstractDataProvider<T> implements DataProvider {
    protected static final Logger LOGGER = LogManager.getLogger();
    protected static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    protected final DataGenerator root;
    protected final String type;
    protected final List<T> generators;

    public AbstractDataProvider(DataGenerator root, String type, List<T> generators) {
        this.root = root;
        this.type = type;
        this.generators = generators;
    }

    @Override
    public void run(DataCache cache) {
        this.write(cache, this.createFileMap());
    }

    public void write(DataCache cache, Map<Identifier, JsonElement> map, BiFunction<Path, Identifier, Path> pathCreator) {
        Path path = this.root.getOutput();
        map.forEach((id, json) -> {
            Path output = pathCreator.apply(path, id);
            try {
                DataProvider.writeToPath(GSON, cache, json, output);
            } catch (IOException e) {
                LOGGER.error("Couldn't save {} {}", this.type, output, e);
            }
        });
    }

    public void write(DataCache cache, Map<Identifier, JsonElement> map) {
        this.write(cache, map, this::getOutput);
    }

    protected Path getOutput(Path rootOutput, Identifier id) {
        return rootOutput.resolve(String.format("data/%s/%s/%s.json", id.getNamespace(), this.type, id.getPath()));
    }

    protected abstract Map<Identifier, JsonElement> createFileMap();

    public abstract String getName();
}
