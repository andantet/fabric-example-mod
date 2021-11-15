package me.andante.example.datagen.provider;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import com.google.gson.JsonElement;
import me.andante.example.datagen.generator.BlockTagGenerator;
import me.andante.example.datagen.generator.EntityTypeTagGenerator;
import me.andante.example.datagen.generator.tag.AbstractTagGenerator;
import net.minecraft.data.DataGenerator;
import net.minecraft.util.Identifier;

import java.util.Map;
import java.util.function.Supplier;

public class TagProvider extends AbstractDataProvider<Supplier<AbstractTagGenerator<?>>> {
    public TagProvider(DataGenerator root) {
        super(root, "tags", ImmutableList.of(BlockTagGenerator::new, EntityTypeTagGenerator::new));
    }

    @Override
    protected Map<Identifier, JsonElement> createFileMap() {
        Map<Identifier, JsonElement> map = Maps.newHashMap();
        for (Supplier<AbstractTagGenerator<?>> generator : this.generators) {
            generator.get().accept((id, factory) -> {
                if (map.put(id, factory.createJson()) != null) {
                    throw new IllegalStateException("Duplicate tag " + id);
                }
            });
        }
        return map;
    }

    @Override
    public String getName() {
        return "Tag";
    }
}
