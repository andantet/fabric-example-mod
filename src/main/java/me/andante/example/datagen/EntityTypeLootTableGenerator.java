package me.andante.example.datagen;

import me.andante.example.Example;
import me.andante.example.datagen.impl.generator.loot.AbstractEntityTypeLootTableGenerator;

public class EntityTypeLootTableGenerator extends AbstractEntityTypeLootTableGenerator {
    public EntityTypeLootTableGenerator() {
        super(Example.MOD_ID);
    }

    @Override
    public void generate() {}
}
