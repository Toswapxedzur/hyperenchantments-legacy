package net.enchant.hyper_enchantments;

import net.enchant.hyper_enchantments.BlockEntities.PedestalBlockEntity;
import net.enchant.hyper_enchantments.BlockEntities.SidePedestalBlockEntity;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class AllBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, HyperEnchantments.ID);

    public static final Supplier<BlockEntityType<PedestalBlockEntity>> PEDESTAL = BLOCK_ENTITIES.register("pedestal", ()->BlockEntityType.Builder.of(PedestalBlockEntity::new, AllBlocks.PEDESTAL.get()).build(null));

    public static final Supplier<BlockEntityType<SidePedestalBlockEntity>> SIDE_PEDESTAL = BLOCK_ENTITIES.register("side_pedestal", ()->BlockEntityType.Builder.of(SidePedestalBlockEntity::new, AllBlocks.SIDE_PEDESTAL.get()).build(null));
}
