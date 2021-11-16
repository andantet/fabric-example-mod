package me.andante.example.datagen;

import me.andante.example.Example;
import me.andante.example.datagen.impl.generator.advancement.AbstractAdvancementGenerator;

public class AdvancementGenerator extends AbstractAdvancementGenerator {
    public AdvancementGenerator() {
        super(Example.MOD_ID);
    }

    @Override
    public void generate() {}
}
