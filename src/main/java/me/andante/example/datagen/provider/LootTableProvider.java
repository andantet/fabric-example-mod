package me.andante.example.datagen.provider;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import com.google.gson.JsonElement;
import com.mojang.datafixers.util.Pair;
import me.andante.example.datagen.generator.BlockLootTableGenerator;
import me.andante.example.datagen.generator.EntityTypeLootTableGenerator;
import me.andante.example.datagen.generator.loot.AbstractLootTableGenerator;
import net.minecraft.data.DataGenerator;
import net.minecraft.loot.LootManager;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.context.LootContextType;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.util.Identifier;

import java.util.Map;
import java.util.function.Supplier;

public class LootTableProvider extends AbstractDataProvider<Pair<Supplier<AbstractLootTableGenerator<?>>, LootContextType>> {
    public LootTableProvider(DataGenerator root) {
        super(root, "loot_tables", ImmutableList.of(
            Pair.of(BlockLootTableGenerator::new, LootContextTypes.BLOCK),
            Pair.of(EntityTypeLootTableGenerator::new, LootContextTypes.ENTITY)
        ));
    }

    @Override
    protected Map<Identifier, JsonElement> createFileMap() {
        Map<Identifier, JsonElement> map = Maps.newHashMap();
        for (Pair<Supplier<AbstractLootTableGenerator<?>>, LootContextType> pair : this.generators) {
            pair.getFirst().get().accept((id, builder) -> {
                LootTable lootTable = builder.type(pair.getSecond()).build();
                if (map.put(id, LootManager.toJson(lootTable)) != null) {
                    throw new IllegalStateException("Duplicate loot table " + id);
                }
            });
        }
        return map;
    }

    @Override
    public String getName() {
        return "Loot Table";
    }
}
