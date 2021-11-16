package me.andante.example.datagen.impl.generator.tag;

import net.minecraft.block.Block;
import net.minecraft.util.registry.Registry;

public abstract class AbstractBlockTagGenerator extends AbstractTagGenerator<Block> {
    public AbstractBlockTagGenerator(String modId) {
        super(modId, Registry.BLOCK);
    }
}
