package me.andante.example.datagen;

import me.andante.example.Example;
import me.andante.example.datagen.impl.generator.model.StateGen;
import me.andante.example.datagen.impl.generator.model.block.AbstractStateModelGenerator;
import net.minecraft.block.Block;

import java.util.function.Function;

public class StateModelGenerator extends AbstractStateModelGenerator {
    public StateModelGenerator() {
        super(Example.MOD_ID);
    }

    @Override
    public void generate() {}

    public void add(Block block, Function<Block, StateGen> factory) {
        add(block, factory.apply(block));
    }
}
