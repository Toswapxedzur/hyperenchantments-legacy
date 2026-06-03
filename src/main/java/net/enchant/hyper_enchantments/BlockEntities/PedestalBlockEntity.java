package net.enchant.hyper_enchantments.BlockEntities;

import net.enchant.hyper_enchantments.AllBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.CampfireBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

public class PedestalBlockEntity extends SingleItemHolderBlockEntity {

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

    public int time;
    public float flip;
    public float oFlip;
    public float flipT;
    public float flipA;
    public float open;
    public float oOpen;
    public float rot;
    public float oRot;
    public float tRot;
    private static final RandomSource RANDOM = RandomSource.create();

    public static void bookAnimationTick(Level level, BlockPos pos, BlockState state, PedestalBlockEntity pedestal) {
        pedestal.oOpen = pedestal.open;
        pedestal.oRot = pedestal.rot;
        Player player = level.getNearestPlayer((double)pos.getX() + (double)0.5F, (double)pos.getY() + (double)0.5F, (double)pos.getZ() + (double)0.5F, (double)3.0F, false);
        if (player != null) {
            double d0 = player.getX() - ((double)pos.getX() + (double)0.5F);
            double d1 = player.getZ() - ((double)pos.getZ() + (double)0.5F);
            pedestal.tRot = (float) Mth.atan2(d1, d0);
            pedestal.open += 0.1F;
            if (pedestal.open < 0.5F || RANDOM.nextInt(40) == 0) {
                float f1 = pedestal.flipT;

                do {
                    pedestal.flipT += (float)(RANDOM.nextInt(4) - RANDOM.nextInt(4));
                } while(f1 == pedestal.flipT);
            }
        } else {
            pedestal.tRot += 0.02F;
            pedestal.open -= 0.1F;
        }

        while(pedestal.rot >= (float)Math.PI) {
            pedestal.rot -= ((float)Math.PI * 2F);
        }

        while(pedestal.rot < -(float)Math.PI) {
            pedestal.rot += ((float)Math.PI * 2F);
        }

        while(pedestal.tRot >= (float)Math.PI) {
            pedestal.tRot -= ((float)Math.PI * 2F);
        }

        while(pedestal.tRot < -(float)Math.PI) {
            pedestal.tRot += ((float)Math.PI * 2F);
        }

        float f2;
        for(f2 = pedestal.tRot - pedestal.rot; f2 >= (float)Math.PI; f2 -= ((float)Math.PI * 2F)) {
        }

        while(f2 < -(float)Math.PI) {
            f2 += ((float)Math.PI * 2F);
        }

        pedestal.rot += f2 * 0.4F;
        pedestal.open = Mth.clamp(pedestal.open, 0.0F, 1.0F);
        ++pedestal.time;
        pedestal.oFlip = pedestal.flip;
        float f = (pedestal.flipT - pedestal.flip) * 0.4F;
        float f3 = 0.2F;
        f = Mth.clamp(f, -0.2F, 0.2F);
        pedestal.flipA += (f - pedestal.flipA) * 0.9F;
        pedestal.flip += pedestal.flipA;
    }


    public PedestalBlockEntity(BlockPos pos, BlockState blockState) {
        super(AllBlockEntities.PEDESTAL.get(), pos, blockState);
        this.sidePedestal = new Optional[8];
        Arrays.fill(sidePedestal, Optional.empty());
    }

    @Override
    public void interactContent(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if(getInventory().getStackInSlot(0).isEmpty() && stack.isEmpty()) {
            this.dropContent();
        }else {
            super.interactContent(stack, state, level, pos, player, hand, hitResult);
        }
    }

    @Override
    public void dropContent() {
        for(Optional<BlockPos> i : sidePedestal)
            if(i.isPresent() && level.getBlockEntity(i.get()) instanceof SidePedestalBlockEntity entity)
                if(!entity.isRemoved())
                    entity.dropContent();
        super.dropContent();
    }

    public Optional<BlockPos>[] sidePedestal;

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        long iArray[] = Arrays.stream(sidePedestal).mapToLong(optional -> optional.isPresent() ? optional.get().asLong() : 137438953472L).toArray();

        tag.putLongArray("sidePedestal", iArray);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        if(tag.contains("sidePedestal")) {
            Optional[] oArray = Arrays.stream(tag.getLongArray("sidePedestal"))
                    .mapToObj(num -> num == 137438953472L ? Optional.empty() : new BlockPos.MutableBlockPos().set(num)).toArray(Optional[]::new);
            System.arraycopy(oArray, 0, sidePedestal, 0, 8);
        }
    }

    public boolean addSidePedestal(BlockPos addedPos){
        for(int i=0;i<8;i++){
            if(sidePedestal[i].isEmpty() || level.getBlockEntity(sidePedestal[i].get()) == null || level.getBlockEntity(sidePedestal[i].get()).isRemoved()){
                sidePedestal[i] = Optional.of(new BlockPos(addedPos));
                return true;
            }
        }
        return false;
    }

    public boolean removeSidePedestal(BlockPos removePos){
        for(int i=0;i<8;i++){
            if(sidePedestal[i].isEmpty() || level.getBlockEntity(sidePedestal[i].get()) == null || level.getBlockEntity(sidePedestal[i].get()).isRemoved())
                continue;
            if(sidePedestal[i].get().equals(removePos)){
                sidePedestal[i] = Optional.empty();
                return true;
            }
        }
        return false;
    }

    public void updateSidePedestal(){
        for(Optional<BlockPos> pos : sidePedestal){
            if(pos.isPresent() && level.getBlockEntity(pos.get()) instanceof SidePedestalBlockEntity entity){
                if(!isValidConnection(entity)){
                    removeSidePedestal(entity.getBlockPos());
                }
            }
        }

        for(BlockPos pos : BlockPos.betweenClosed(getBlockPos().offset(-3,0,-3), getBlockPos().offset(3,0,3))){
            if(level.getBlockEntity(pos) instanceof SidePedestalBlockEntity entity && !entity.isRemoved()){
                if(entity.mainPedestal.isEmpty()){
                    addSidePedestal(new BlockPos(pos));
                    entity.addMainpedestal(new BlockPos(getBlockPos()));
                }
            }
        }
    }

    private boolean isValidConnection(SidePedestalBlockEntity entity) {
        boolean predicate = Math.abs(entity.getBlockPos().getX() - getBlockPos().getX()) <= 3;
        predicate = predicate && Math.abs(entity.getBlockPos().getZ() - getBlockPos().getZ()) <= 3;
        return predicate;
    }

    public void onBlockEntityRemoved(){
        for(int i=0;i<8;i++){
            if(sidePedestal[i].isEmpty())
                continue;
            BlockPos pos = sidePedestal[i].get();
            BlockEntity entity = level.getBlockEntity(pos);
            if(!(entity instanceof SidePedestalBlockEntity sidePedestalEntity))
                continue;
            if(sidePedestalEntity.mainPedestal.isPresent() && sidePedestalEntity.mainPedestal.get().equals(getBlockPos())){
                sidePedestalEntity.removeMainPedestal(getBlockPos());
                sidePedestalEntity.updateMainPedestal();
            }
        }
    }

    @Override
    public void onLoad() {
        updateSidePedestal();
        super.onLoad();
    }

}
