package net.enchant.hyper_enchantments.BlockEntities;

import net.enchant.hyper_enchantments.AllBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.Arrays;
import java.util.Optional;

public class SidePedestalBlockEntity extends SingleItemHolderBlockEntity {

    public float Xangle, Yangle, Zangle;

    public float Xrot(){
        Xangle+=0.004f;
        if(Xangle>=360)
            Xangle-=360;
        return Xangle;
    }
    public float Yrot(){
        Yangle+=0.004f;
        if(Yangle>=360)
            Yangle-=360;
        return Yangle;
    }
    public float Zrot(){
        Zangle+=0.004f;
        if(Zangle>=360)
            Zangle-=360;
        return Zangle;
    }

    public double xParticleSpeed = 0, yParticleSpeed = 0, zParticleSpeed = 0;

    public void updatePos(){
        if(mainPedestal.isEmpty())
            return;
        Vec3 mainPos = mainPedestal.get().getCenter();
        Vec3 currentPos = getBlockPos().getCenter();
        currentPos = currentPos.subtract(mainPos);
        xParticleSpeed = currentPos.x;
        yParticleSpeed = currentPos.y;
        zParticleSpeed = currentPos.z;
    }

    public static void renderParticle(Level level, BlockPos pos, BlockState state, SidePedestalBlockEntity entity) {
        if (entity.mainPedestal.isPresent()) {
            BlockPos pos1 = entity.mainPedestal.get();
            double x = (double) pos1.getX() + 0.5, y = (double) pos1.getY() + 3.2, z = (double) pos1.getZ() + 0.5;
            ((ServerLevel) level).sendParticles(ParticleTypes.ENCHANT, x, y, z, 3, 4, 3, 4, 0.4);
        }
    }


    public SidePedestalBlockEntity(BlockPos pos, BlockState blockState) {
        super(AllBlockEntities.SIDE_PEDESTAL.get(), pos, blockState);
    }

    public Optional<BlockPos> mainPedestal = Optional.empty();

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putLong("mainPedestal", mainPedestal.isPresent() ? mainPedestal.get().asLong() : 137438953472L);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        if(tag.contains("mainPedestal"))
            mainPedestal = tag.getLong("mainPedestal") != 137438953472L ?
                    Optional.of(new BlockPos.MutableBlockPos().set(tag.getLong("mainPedestal"))) : Optional.empty();
    }

    public boolean addMainpedestal(BlockPos addPos){
        mainPedestal = Optional.of(addPos);
        return true;
    }

    public boolean removeMainPedestal(BlockPos removePos){
        mainPedestal = Optional.empty();
        return true;
    }

    public void updateMainPedestal(){
        for(BlockPos pos : BlockPos.betweenClosed(getBlockPos().offset(-3,0,-3), getBlockPos().offset(3,0,3))){
            if(level.getBlockEntity(pos) instanceof PedestalBlockEntity entity && !entity.isRemoved()){
                boolean success = entity.addSidePedestal(new BlockPos(getBlockPos()));
                if(success) {
                    mainPedestal = Optional.of(new BlockPos(pos));
                    break;
                }
            }
        }
    }

    public void onBlockEntityRemoved(){
        if(mainPedestal.isEmpty())
            return;
        BlockPos pos = mainPedestal.get();
        if(level.getBlockEntity(pos) instanceof PedestalBlockEntity entity){
            entity.removeSidePedestal(getBlockPos());
            entity.updateSidePedestal();
        }
    }

    @Override
    public void onLoad() {
        updateMainPedestal();
        super.onLoad();
    }
}
