package me.andante.example.datagen.generator.loot;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import me.andante.example.datagen.ObjectLootTableAccess;
import net.minecraft.block.Block;
import net.minecraft.item.ItemConvertible;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.LootTables;
import net.minecraft.loot.condition.BlockStatePropertyLootCondition;
import net.minecraft.loot.condition.LootCondition;
import net.minecraft.loot.condition.RandomChanceLootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.entry.LootPoolEntry;
import net.minecraft.loot.function.ConditionalLootFunction;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.loot.operator.BoundedIntUnaryOperator;
import net.minecraft.loot.provider.number.BinomialLootNumberProvider;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.loot.provider.number.LootNumberProvider;
import net.minecraft.loot.provider.number.UniformLootNumberProvider;
import net.minecraft.predicate.StatePredicate;
import net.minecraft.state.property.Property;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

@SuppressWarnings("unused")
public abstract class AbstractLootTableGenerator<T> implements Consumer<BiConsumer<Identifier, LootTable.Builder>> {
    protected final String modId;
    protected final Registry<T> registry;
    private final Map<Identifier, LootTable.Builder> lootTables;

    public AbstractLootTableGenerator(String modId, Registry<T> registry) {
        this.modId = modId;
        this.registry = registry;
        this.lootTables = Maps.newHashMap();

        if (!(this.registry.get(0) instanceof ObjectLootTableAccess)) {
            throw new IllegalArgumentException(String.format("Object provided for %s was not instanceof ObjectLootTableAccess", this.getClass()));
        }
    }

    public abstract void generate();
    public void testObject(Identifier id, T obj) {}

    @SuppressWarnings("ConstantConditions")
    @Override
    public void accept(BiConsumer<Identifier, LootTable.Builder> biConsumer) {
        this.generate();

        Set<Identifier> set = Sets.newHashSet();
        Iterable<T> objects = this.registry
            .stream()
            .filter(obj -> this.registry.getId(obj).getNamespace().equals(this.modId))
            ::iterator;

        for (T obj : objects) {
            Identifier id = ((ObjectLootTableAccess) obj).access_getLootTableId();
            this.testObject(id, obj);
            if (id != LootTables.EMPTY && set.add(id)) {
                LootTable.Builder builder = this.lootTables.remove(id);
                if (builder == null) {
                    throw new IllegalStateException(
                        String.format(
                            "Missing loottable '%s' for '%s'", id,
                            this.registry.getId(obj)
                        )
                    );
                }

                biConsumer.accept(id, builder);
            }
        }

        if (!this.lootTables.isEmpty()) {
            throw new IllegalStateException(String.format("Created loot tables for non-%s: %s", this.registry.getKey().getValue().getPath(), this.lootTables.keySet()));
        }
    }

    public final void add(T obj, LootTable.Builder lootTable) {
        this.lootTables.put(((ObjectLootTableAccess) obj).access_getLootTableId(), lootTable);
    }

    public final void add(T obj, Function<T, LootTable.Builder> function) {
        this.add(obj, function.apply(obj));
    }

    public LootTable.Builder dropsNothing() {
        return LootTable.builder();
    }

    public LootTable.Builder dropsConditionally(ItemConvertible drop, LootCondition.Builder condition, LootPoolEntry.Builder<?> orElse) {
        return LootTable.builder().pool(
            pool().with(ItemEntry.builder(drop).conditionally(condition).alternatively(orElse))
        );
    }

    public static ConditionalLootFunction.Builder<?> setCount(LootNumberProvider range) {
        return SetCountLootFunction.builder(range);
    }

    public static LootPool.Builder pool(LootNumberProvider rolls) {
        return LootPool.builder().rolls(rolls);
    }

    public static LootPool.Builder pool() {
        return pool(count(1));
    }

    public static LootNumberProvider count(int count) {
        return ConstantLootNumberProvider.create(count);
    }

    public static LootNumberProvider countRandom(float min, float max) {
        return UniformLootNumberProvider.create(min, max);
    }

    public static LootNumberProvider countBiased(int n, float p) {
        return BinomialLootNumberProvider.create(n, p);
    }

    public static <T extends Comparable<T>> StatePredicate.Builder stateProp(Property<T> prop, T val) {
        return StatePredicate.Builder.create().exactMatch(prop, prop.name(val));
    }

    public static <T extends Comparable<T>> BlockStatePropertyLootCondition.Builder stateCond(Block block, Property<T> prop, T val) {
        return BlockStatePropertyLootCondition.builder(block).properties(stateProp(prop, val));
    }

    public static BoundedIntUnaryOperator atLeast(int min) {
        return BoundedIntUnaryOperator.createMin(min);
    }

    public static BoundedIntUnaryOperator atMost(int max) {
        return BoundedIntUnaryOperator.createMax(max);
    }

    public static BoundedIntUnaryOperator minMax(int min, int max) {
        return BoundedIntUnaryOperator.create(min, max);
    }

    public static RandomChanceLootCondition.Builder chance(float chance) {
        return RandomChanceLootCondition.builder(chance);
    }
}
