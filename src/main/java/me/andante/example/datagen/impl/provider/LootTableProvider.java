package me.andante.example.datagen.impl.provider;

import com.google.common.collect.Maps;
import com.google.gson.JsonElement;
import com.mojang.datafixers.util.Pair;
import me.andante.example.datagen.impl.DataType;
import me.andante.example.datagen.impl.generator.loot.AbstractLootTableGenerator;
import net.minecraft.data.DataGenerator;
import net.minecraft.loot.LootManager;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.context.LootContextType;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class LootTableProvider extends AbstractDataProvider<Pair<Supplier<AbstractLootTableGenerator<?>>, LootContextType>> {
    public LootTableProvider(DataGenerator root) {
        super(root);
    }

    @Override
    public String getName() {
        return "Loot Table";
    }

    @Override
    public String getFolder() {
        return "loot_tables";
    }

    @Override
    public DataType getDataType() {
        return DataType.DATA;
    }

    @Override
    public List<Pair<Supplier<AbstractLootTableGenerator<?>>, LootContextType>> getGenerators() {
        return List.of();
    }

    @Override
    public Map<Identifier, JsonElement> createFileMap() {
        Map<Identifier, JsonElement> map = Maps.newHashMap();
        for (Pair<Supplier<AbstractLootTableGenerator<?>>, LootContextType> pair : this.getGenerators()) {
            pair.getFirst().get().accept((id, builder) -> {
                LootTable lootTable = builder.type(pair.getSecond()).build();
                if (map.put(id, LootManager.toJson(lootTable)) != null) {
                    throw new IllegalStateException("Duplicate loot table " + id);
                }
            });
        }
        return map;
    }
}