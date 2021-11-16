package me.andante.example.datagen;

import me.andante.example.Example;
import me.andante.example.datagen.impl.generator.loot.AbstractBlockLootTableGenerator;

public class BlockLootTableGenerator extends AbstractBlockLootTableGenerator {
    public BlockLootTableGenerator() {
        super(Example.MOD_ID);
    }

    @Override
    public void generate() {}
}
