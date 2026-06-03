package net.enchant.hyper_enchantments;

import net.enchant.hyper_enchantments.Blocks.PedestalBlock;
import net.enchant.hyper_enchantments.Blocks.SidePedestalBlock;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class AllBlocks {

    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(HyperEnchantments.ID);

    public static final DeferredBlock<PedestalBlock> PEDESTAL = registerBlockItem("pedestal",
            ()->new PedestalBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_MAGENTA).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().lightLevel((blockState) -> 9).strength(5.0F, 1200.0F).noOcclusion()));

    public static final DeferredBlock<SidePedestalBlock> SIDE_PEDESTAL = registerBlockItem("side_pedestal",
            ()->new SidePedestalBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_MAGENTA).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().lightLevel((blockState) -> 7).strength(5.0F, 1200.0F).noOcclusion()));

    public static <T extends Block> DeferredBlock<T> registerBlockItem(String name, Supplier<T> block){
        DeferredBlock<T> t = BLOCKS.register(name, block);
        AllItems.ITEMS.registerItem(name, properties -> new BlockItem(t.get(), properties));
        return t;
    }
}
