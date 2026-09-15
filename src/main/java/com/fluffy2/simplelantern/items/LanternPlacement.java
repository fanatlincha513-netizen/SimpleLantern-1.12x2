package com.fluffy2.simplelantern.items;

import com.fluffy2.simplelantern.blocks.TileEntityLantern;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/** Общая логика установки фонаря как блока с переносом топлива в TileEntity. */
public final class LanternPlacement {

    private LanternPlacement() {}

    public static EnumActionResult place(EntityPlayer player, World world, BlockPos pos, EnumHand hand,
                                         EnumFacing facing, Block block) {
        ItemStack stack = player.getHeldItem(hand);
        BlockPos target = pos;
        IBlockState hit = world.getBlockState(pos);

        if (!hit.getBlock().isReplaceable(world, pos)) {
            target = pos.offset(facing);
        }

        if (!player.canPlayerEdit(target, facing, stack)) {
            return EnumActionResult.FAIL;
        }
        if (!world.getBlockState(target).getBlock().isReplaceable(world, target)) {
            return EnumActionResult.FAIL;
        }
        if (!world.mayPlace(block, target, false, facing, player)) {
            return EnumActionResult.FAIL;
        }

        if (!world.isRemote) {
            world.setBlockState(target, block.getDefaultState(), 3);
            TileEntity te = world.getTileEntity(target);
            if (te instanceof TileEntityLantern) {
                ((TileEntityLantern) te).setOil(LanternHelper.getOilLevel(stack));
                ((TileEntityLantern) te).setReflect(LanternHelper.getReflect(stack));
                te.markDirty();
            }
            if (!player.capabilities.isCreativeMode) {
                stack.shrink(1);
            }
        }
        return EnumActionResult.SUCCESS;
    }
}
