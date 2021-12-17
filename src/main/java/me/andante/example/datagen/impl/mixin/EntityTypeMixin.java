package me.andante.example.datagen.impl.mixin;

import me.andante.example.datagen.impl.ObjectLootTableAccess;
import net.minecraft.entity.EntityType;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(EntityType.class)
public abstract class EntityTypeMixin implements ObjectLootTableAccess {
    @Shadow public abstract Identifier getLootTableId();

    @Override
    public Identifier access_getLootTableId() {
        return this.getLootTableId();
    }
}
