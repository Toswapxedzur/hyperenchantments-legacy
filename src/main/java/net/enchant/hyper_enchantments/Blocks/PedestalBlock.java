package net.enchant.hyper_enchantments.Blocks;

import com.mojang.serialization.MapCodec;
import net.enchant.hyper_enchantments.AllBlockEntities;
import net.enchant.hyper_enchantments.BlockEntities.SidePedestalBlockEntity;
import net.enchant.hyper_enchantments.BlockEntities.SingleItemHolderBlockEntity;
import net.enchant.hyper_enchantments.BlockEntities.PedestalBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class PedestalBlock extends BaseEntityBlock{
    public static final VoxelShape SHAPE = Shapes.or(box(1,0,1,15,4,15), box(4,4,4,12,10,12), box(0,10,0,16,16,16));

    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    protected boolean propagatesSkylightDown(BlockState state, BlockGetter level, BlockPos pos) {
        return true;
    }


    public PedestalBlock(Properties properties) {
        super(properties);
    }


    public static final MapCodec<PedestalBlock> CODEC = simpleCodec(PedestalBlock::new);

    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new PedestalBlockEntity(blockPos, blockState);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        return level.isClientSide ? createTickerHelper(blockEntityType, AllBlockEntities.PEDESTAL.get(), PedestalBlockEntity::bookAnimationTick) : null;
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if(level.getBlockEntity(pos) instanceof SingleItemHolderBlockEntity blockEntity){
            blockEntity.interactContent(stack,state,level,pos,player,hand,hitResult);
            return ItemInteractionResult.SUCCESS;
        }
        return ItemInteractionResult.CONSUME;
    }

    @Override
    protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        if(!state.getBlock().equals(newState.getBlock())){
            if(level.getBlockEntity(pos) instanceof PedestalBlockEntity blockEntity){
                blockEntity.dropContent();
                blockEntity.onBlockEntityRemoved();
                if(!level.isClientSide) {
                    level.updateNeighbourForOutputSignal(pos, this);
                    blockEntity.invalidate();
                }
            }
        }
    }
}
