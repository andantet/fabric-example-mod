package me.andante.example.datagen.generator.advancement;

import com.google.common.collect.Maps;
import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.criterion.EnterBlockCriterion;
import net.minecraft.advancement.criterion.InventoryChangedCriterion;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.predicate.NumberRange;
import net.minecraft.predicate.StatePredicate;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.predicate.item.ItemPredicate;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;

import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

@SuppressWarnings({ "unused", "UnusedReturnValue" })
public abstract class AbstractAdvancementGenerator implements Consumer<BiConsumer<Identifier, Advancement.Task>> {
    protected final String modId;
    private final Map<Identifier, Advancement.Task> advancementMap;

    public AbstractAdvancementGenerator(String modId) {
        this.modId = modId;
        this.advancementMap = Maps.newHashMap();
    }

    public abstract void generate();

    @Override
    public void accept(BiConsumer<Identifier, Advancement.Task> biConsumer) {
        this.generate();
        this.advancementMap.forEach(biConsumer);
    }

    public AbstractAdvancementGenerator add(String id, Advancement.Task factory) {
        return this.add(getId(id), factory);
    }

    public AbstractAdvancementGenerator add(Identifier id, Advancement.Task factory) {
        this.advancementMap.put(id, factory);
        return this;
    }

    public Identifier getId(String id) {
        return new Identifier(this.modId, id);
    }

    protected static EnterBlockCriterion.Conditions inFluid(Block block) {
        return new EnterBlockCriterion.Conditions(EntityPredicate.Extended.EMPTY, block, StatePredicate.ANY);
    }

    public InventoryChangedCriterion.Conditions hasItem(ItemConvertible itemConvertible) {
        return this.hasItems(ItemPredicate.Builder.create().items(itemConvertible).build());
    }

    public InventoryChangedCriterion.Conditions hasItems(Tag<Item> tag) {
        return this.hasItems(ItemPredicate.Builder.create().tag(tag).build());
    }

    public InventoryChangedCriterion.Conditions hasItems(ItemPredicate... predicates) {
        return new InventoryChangedCriterion.Conditions(EntityPredicate.Extended.EMPTY, NumberRange.IntRange.ANY, NumberRange.IntRange.ANY, NumberRange.IntRange.ANY, predicates);
    }
}
