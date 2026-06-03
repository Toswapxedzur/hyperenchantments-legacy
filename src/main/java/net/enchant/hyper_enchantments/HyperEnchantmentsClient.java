package net.enchant.hyper_enchantments;

import net.enchant.hyper_enchantments.BlockEntities.BlockEntityRenderers.PedestalBlockEntityRenderer;
import net.enchant.hyper_enchantments.BlockEntities.BlockEntityRenderers.SidePedestalBlockEntityRenderer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

@Mod(value = HyperEnchantments.ID, dist = Dist.CLIENT)
@EventBusSubscriber(modid = HyperEnchantments.ID, value = Dist.CLIENT)
public class HyperEnchantmentsClient {
    @SubscribeEvent
    static void onClientSetup(FMLClientSetupEvent event){

    }

    @SubscribeEvent
    public static void registerBlockEntityRenderer(EntityRenderersEvent.RegisterRenderers event){
        event.registerBlockEntityRenderer(AllBlockEntities.PEDESTAL.get(), PedestalBlockEntityRenderer::new);
        event.registerBlockEntityRenderer(AllBlockEntities.SIDE_PEDESTAL.get(), SidePedestalBlockEntityRenderer::new);
    }
}
