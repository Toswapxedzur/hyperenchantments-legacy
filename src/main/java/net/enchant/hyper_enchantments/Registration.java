package net.enchant.hyper_enchantments;

import net.neoforged.bus.api.IEventBus;

public class Registration {
    public static void register(IEventBus modEventBus){
        AllBlocks.BLOCKS.register(modEventBus);
        AllItems.ITEMS.register(modEventBus);
        AllBlockEntities.BLOCK_ENTITIES.register(modEventBus);
        AllRecipes.SERIALIZERS.register(modEventBus);
        AllRecipes.RECIPE_TYPES.register(modEventBus);
    }
}
