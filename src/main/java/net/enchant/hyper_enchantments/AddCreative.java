package net.enchant.hyper_enchantments;

import net.minecraft.world.item.CreativeModeTabs;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;

public class AddCreative {
    public static void addCreativeTab(BuildCreativeModeTabContentsEvent event){
        if(event.getTabKey().equals(CreativeModeTabs.FUNCTIONAL_BLOCKS)){
            event.accept(AllBlocks.PEDESTAL);
            event.accept(AllBlocks.SIDE_PEDESTAL);
        }
    }
}
