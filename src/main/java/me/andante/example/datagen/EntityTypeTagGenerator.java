package me.andante.example.datagen;

import me.andante.example.Example;
import me.andante.example.datagen.impl.generator.tag.AbstractEntityTypeTagGenerator;

public class EntityTypeTagGenerator extends AbstractEntityTypeTagGenerator {
    public EntityTypeTagGenerator() {
        super(Example.MOD_ID);
    }

    @Override
    public void generate() {}
}
