package com.fluffy2.simplelantern.blocks;

import com.fluffy2.simplelantern.ModConfig;

import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;

public class TileEntityLantern extends TileEntity implements ITickable {

    private int oil;
    private boolean reflect;
    private int burnCounter;

    public int getOil() {
        return oil;
    }

    public void setOil(int oil) {
        this.oil = oil;
    }

    public boolean getReflect() {
        return reflect;
    }

    public void setReflect(boolean reflect) {
        this.reflect = reflect;
    }

    /** Меняет блок, сохраняя содержимое TileEntity. */
    public void switchTo(Block block) {
        int savedOil = this.oil;
        boolean savedReflect = this.reflect;
        world.setBlockState(pos, block.getDefaultState(), 3);
        TileEntity te = world.getTileEntity(pos);
        if (te instanceof TileEntityLantern) {
            ((TileEntityLantern) te).oil = savedOil;
            ((TileEntityLantern) te).reflect = savedReflect;
            te.markDirty();
        }
    }

    @Override
    public void update() {
        if (world.isRemote || !ModConfig.lanternRequireFuel) {
            return;
        }
        Block block = world.getBlockState(pos).getBlock();
        if (!(block instanceof BlockLanternBase) || !((BlockLanternBase) block).isLit()) {
            return;
        }

        if (oil > 0) {
            oil--;
            if (++burnCounter >= 20) {
                burnCounter = 0;
                markDirty();
            }
        } else {
            switchTo(com.fluffy2.simplelantern.init.ModBlocks.LANTERN_OFF);
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        this.oil = compound.getInteger("Oil");
        this.reflect = compound.getBoolean("Reflect");
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound = super.writeToNBT(compound);
        compound.setInteger("Oil", this.oil);
        compound.setBoolean("Reflect", this.reflect);
        return compound;
    }

    @Override
    public boolean shouldRefresh(net.minecraft.world.World world, net.minecraft.util.math.BlockPos pos,
                                 net.minecraft.block.state.IBlockState oldState,
                                 net.minecraft.block.state.IBlockState newState) {
        // не пересоздавать TE при смене lantern_off <-> lantern_on
        return !(oldState.getBlock() instanceof BlockLanternBase
                && newState.getBlock() instanceof BlockLanternBase);
    }
}
