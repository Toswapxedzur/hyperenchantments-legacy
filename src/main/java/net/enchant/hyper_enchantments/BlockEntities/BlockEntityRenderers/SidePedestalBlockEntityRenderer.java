package net.enchant.hyper_enchantments.BlockEntities.BlockEntityRenderers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.enchant.hyper_enchantments.BlockEntities.PedestalBlockEntity;
import net.enchant.hyper_enchantments.BlockEntities.SidePedestalBlockEntity;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.EnchantingTableBlock;

public class SidePedestalBlockEntityRenderer implements BlockEntityRenderer<SidePedestalBlockEntity> {
    public final ItemRenderer itemRenderer;

    public SidePedestalBlockEntityRenderer(BlockEntityRendererProvider.Context context){
        this.itemRenderer = context.getItemRenderer();
    }

    @Override
    public void render(SidePedestalBlockEntity blockEntity, float v, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, int i1) {
        ItemStack renderStack = blockEntity.getInventory().getStackInSlot(0);
        if(!renderStack.isEmpty()){
            poseStack.pushPose();
            poseStack.translate(0.5, 1.2, 0.5);
            poseStack.scale(0.6f,0.6f,0.6f);
            poseStack.mulPose( Axis.YP.rotation(blockEntity.Yrot()) );
            poseStack.mulPose( Axis.XP.rotation(blockEntity.Xrot()) );
            poseStack.mulPose( Axis.ZP.rotation(blockEntity.Zrot()) );
            int lightLevel = LevelRenderer.getLightColor(blockEntity.getLevel(), blockEntity.getBlockState(), blockEntity.getBlockPos().relative(Direction.UP)) + 12;
            itemRenderer.renderStatic(renderStack, ItemDisplayContext.FIXED, lightLevel, OverlayTexture.NO_OVERLAY, poseStack, multiBufferSource, blockEntity.getLevel(), 1);
            poseStack.popPose();
        }
    }
}
