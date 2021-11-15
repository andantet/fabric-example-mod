package me.andante.example.datagen.provider;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import com.google.gson.JsonElement;
import me.andante.example.datagen.generator.AdvancementGenerator;
import me.andante.example.datagen.generator.advancement.AbstractAdvancementGenerator;
import net.minecraft.data.DataGenerator;
import net.minecraft.util.Identifier;

import java.util.Map;
import java.util.function.Supplier;

public class AdvancementProvider extends AbstractDataProvider<Supplier<AbstractAdvancementGenerator>> {
    public AdvancementProvider(DataGenerator root) {
        super(root, "advancements", ImmutableList.of(AdvancementGenerator::new));
    }

    @Override
    protected Map<Identifier, JsonElement> createFileMap() {
        Map<Identifier, JsonElement> map = Maps.newHashMap();
        for (Supplier<AbstractAdvancementGenerator> generator : this.generators) {
            generator.get().accept((id, task) -> {
                if (map.put(id, task.toJson()) != null) {
                    throw new IllegalStateException("Duplicate advancement " + id);
                }
            });
        }
        return map;
    }

    @Override
    public String getName() {
        return "Advancement";
    }
}
