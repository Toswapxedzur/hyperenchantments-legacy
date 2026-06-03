package net.enchant.hyper_enchantments.BlockEntities;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.CampfireBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.items.ItemStackHandler;

public abstract class SingleItemHolderBlockEntity extends BlockEntity {
    public SingleItemHolderBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.put("inventory", inventory.serializeNBT(registries));
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        inventory.deserializeNBT(registries, tag.getCompound("inventory"));
    }

    private final ItemStackHandler inventory = new ItemStackHandler(1){
        @Override
        protected int getStackLimit(int slot, ItemStack stack) {
            return 1;
        }

        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
            if(!level.isClientSide)
                level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
        }
    };

    public ItemStackHandler getInventory(){
        return this.inventory;
    }

    public void invalidate(){
        this.setRemoved();
    }

    public void interactContent(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult){
        if(inventory.getStackInSlot(0).isEmpty()){
            if(!stack.isEmpty()){
                inventory.setStackInSlot(0, new ItemStack(stack.getItem(), 1));
                stack.shrink(1);
            }
        }else{
            ItemStack extract = inventory.extractItem(0, 1, false);
            if(stack.isEmpty()){
                player.setItemInHand(hand, extract);
            }else if(stack.getCount()==1){
                inventory.setStackInSlot(0, stack);
                player.setItemInHand(hand, extract);
            }else{
                ItemEntity entity = new ItemEntity(level, pos.getX()+0.5, pos.getY()+1, pos.getZ()+0.5, extract);
                level.addFreshEntity(entity);
                inventory.setStackInSlot(0, new ItemStack(stack.getItem(), 1));
                stack.shrink(1);
            }
        }
    }

    public void dropContent(){
        if(inventory.extractItem(0, 1, true).isEmpty())
            return;
        ItemStack stack = inventory.extractItem(0, 1, false);
        ItemEntity entity = new ItemEntity(level, getBlockPos().getX()+0.5, getBlockPos().getY()+1.5, getBlockPos().getZ()+0.5, stack);
        entity.setPickUpDelay(20);
        level.addFreshEntity(entity);
    }

}
