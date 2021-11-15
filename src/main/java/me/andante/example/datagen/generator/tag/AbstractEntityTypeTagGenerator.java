package me.andante.example.datagen.generator.tag;

import net.minecraft.entity.EntityType;
import net.minecraft.util.registry.Registry;

public abstract class AbstractEntityTypeTagGenerator extends AbstractTagGenerator<EntityType<?>> {
    public AbstractEntityTypeTagGenerator(String modId) {
        super(modId, Registry.ENTITY_TYPE);
    }
}
