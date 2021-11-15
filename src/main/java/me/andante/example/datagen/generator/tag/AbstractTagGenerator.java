package me.andante.example.datagen.generator.tag;

import com.google.common.collect.Maps;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

@SuppressWarnings("unused")
public abstract class AbstractTagGenerator<T> implements Consumer<BiConsumer<Identifier, TagFactory<T>>> {
    protected final String modId;
    protected final Registry<T> registry;
    private final Map<Identifier, TagFactory<T>> factoryMap;

    public AbstractTagGenerator(String modId, Registry<T> registry) {
        this.modId = modId;
        this.registry = registry;
        this.factoryMap = Maps.newHashMap();
    }

    public abstract void generate();

    @Override
    public void accept(BiConsumer<Identifier, TagFactory<T>> biConsumer) {
        this.generate();
        this.factoryMap.forEach(biConsumer);
    }

    @SafeVarargs
    public final TagFactory<T> add(Tag<T> tag, T... objects) {
        return this.factory(tag).add(objects);
    }

    @SafeVarargs
    public final TagFactory<T> add(Tag<T> tag, Tag<T>... tags) {
        return this.factory(tag).add(tags);
    }

    public TagFactory<T> factory(Tag<T> tag) {
        if (tag instanceof Tag.Identified identified) {
            return this.factory(identified.getId());
        } else {
            throw new RuntimeException("Cannot identify tag " + tag);
        }
    }

    public TagFactory<T> factory(Identifier id) {
        return this.factoryMap.computeIfAbsent(this.getId(id), i -> new TagFactory<>(this.registry::getId));
    }

    public Identifier getId(Identifier id) {
        Identifier registryId = this.registry.getKey().getValue();
        return new Identifier(id.getNamespace(), String.format("%ss/%s", registryId.getPath(), id.getPath()));
    }
}
