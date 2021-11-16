package me.andante.example.datagen;

import me.andante.example.Example;
import me.andante.example.datagen.impl.generator.recipe.AbstractRecipeGenerator;

public class RecipeGenerator extends AbstractRecipeGenerator {
    public RecipeGenerator() {
        super(Example.MOD_ID);
    }

    @Override
    public void generate() {}
}
