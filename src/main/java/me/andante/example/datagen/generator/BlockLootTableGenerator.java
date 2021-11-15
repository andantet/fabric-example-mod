package me.andante.example.datagen.generator;

import me.andante.example.Example;
import me.andante.example.datagen.generator.loot.AbstractBlockLootTableGenerator;

public class BlockLootTableGenerator extends AbstractBlockLootTableGenerator {
    public BlockLootTableGenerator() {
        super(Example.MOD_ID);
    }

    @Override
    public void generate() {}
}
