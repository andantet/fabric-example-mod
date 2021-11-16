package me.andante.example.datagen;

import me.andante.example.Example;
import me.andante.example.datagen.impl.generator.model.item.AbstractItemModelGenerator;

public class ItemModelGenerator extends AbstractItemModelGenerator {
    public ItemModelGenerator() {
        super(Example.MOD_ID);
    }

    @Override
    public void generate() {}
}
