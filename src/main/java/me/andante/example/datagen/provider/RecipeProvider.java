package me.andante.example.datagen.provider;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import me.andante.example.datagen.generator.RecipeGenerator;
import me.andante.example.datagen.generator.recipe.AbstractRecipeGenerator;
import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.criterion.ImpossibleCriterion;
import net.minecraft.data.DataCache;
import net.minecraft.data.DataGenerator;
import net.minecraft.util.Identifier;

import java.nio.file.Path;
import java.util.Map;
import java.util.function.Supplier;

public class RecipeProvider extends AbstractDataProvider<Supplier<AbstractRecipeGenerator>> {
    public RecipeProvider(DataGenerator root) {
        super(root, "recipes", ImmutableList.of(RecipeGenerator::new));
    }

    @Override
    public void run(DataCache cache) {
        super.run(cache);
        this.write(cache, this.createFileMapAdvancements(), this::getOutputAdvancements);
    }

    @Override
    protected Map<Identifier, JsonElement> createFileMap() {
        Map<Identifier, JsonElement> map = Maps.newHashMap();
        for (Supplier<AbstractRecipeGenerator> generator : this.generators) {
            generator.get().accept((id, factory) -> {
                factory.offerTo(provider -> {
                    if (map.put(id, provider.toJson()) != null) {
                        throw new IllegalStateException("Duplicate recipe " + id);
                    }
                }, id);
            });
        }
        return map;
    }

    protected Map<Identifier, JsonElement> createFileMapAdvancements() {
        Map<Identifier, JsonElement> map = Maps.newHashMap();
        for (Supplier<AbstractRecipeGenerator> generator : this.generators) {
            AbstractRecipeGenerator gen = generator.get();
            Identifier rootId = gen.getId("root");
            gen.accept((id, factory) -> {
                factory.offerTo(provider -> {
                    JsonObject json = provider.toAdvancementJson();
                    if (json != null) {
                        if (map.put(id, json) != null) {
                            throw new IllegalStateException("Duplicate recipe advancement " + id);
                        } else {
                            if (!map.containsKey(rootId)) {
                                map.put(rootId, Advancement.Task.create()
                                                                .criterion("impossible", new ImpossibleCriterion.Conditions())
                                                                .toJson()
                                );
                            }
                        }
                    }
                }, id);
            });
        }
        return map;
    }

    protected Path getOutputAdvancements(Path rootOutput, Identifier id) {
        return rootOutput.resolve(String.format("data/%s/advancements/%s/%s.json", id.getNamespace(), this.type, id.getPath()));
    }

    @Override
    public String getName() {
        return "Recipe";
    }
}
