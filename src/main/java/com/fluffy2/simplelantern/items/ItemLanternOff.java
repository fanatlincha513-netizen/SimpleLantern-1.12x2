package com.fluffy2.simplelantern.items;

import com.fluffy2.simplelantern.ModConfig;
import com.fluffy2.simplelantern.SimpleLantern;
import com.fluffy2.simplelantern.init.ModBlocks;
import com.fluffy2.simplelantern.init.ModItems;
import com.fluffy2.simplelantern.init.ModSounds;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

/** Выключенный фонарь: обычный предмет, ставится как блок и зажигается по ПКМ. */
public class ItemLanternOff extends Item {

    public ItemLanternOff() {
        setRegistryName(SimpleLantern.MODID, "lantern_off");
        setUnlocalizedName(SimpleLantern.MODID + ".lantern_off");
        setCreativeTab(CreativeTabs.TOOLS);
        setMaxStackSize(1);
    }

    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
        if (!isInCreativeTab(tab)) {
            return;
        }
        ItemStack empty = new ItemStack(this);
        LanternHelper.setLantern(empty, 0, false);
        items.add(empty);

        ItemStack full = new ItemStack(this);
        LanternHelper.setLantern(full, ModConfig.capacityTicks(), false);
        items.add(full);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        ItemStack stack = player.getHeldItem(hand);

        if (player.isSneaking()) {
            // Переключение режима отражателя
            if (!world.isRemote) {
                LanternHelper.setLantern(stack, LanternHelper.getOilLevel(stack), !LanternHelper.getReflect(stack));
                world.playSound(null, player.getPosition(), ModSounds.CHANGEMODE, SoundCategory.PLAYERS, 1.0F, 1.0F);
            }
            return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, stack);
        }

        if (!LanternHelper.hasFuel(stack) && !player.capabilities.isCreativeMode) {
            if (!world.isRemote) {
                player.sendMessage(new TextComponentTranslation("message.simplelantern.no_fuel"));
            }
            return new ActionResult<ItemStack>(EnumActionResult.FAIL, stack);
        }

        if (!world.isRemote) {
            ItemStack lit = new ItemStack(ModItems.LANTERN_ON);
            LanternHelper.setLantern(lit, LanternHelper.getOilLevel(stack), LanternHelper.getReflect(stack));
            player.setHeldItem(hand, lit);
            world.playSound(null, player.getPosition(), ModSounds.LANTERN_ON, SoundCategory.PLAYERS, 1.0F, 1.0F);
        }
        return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, stack);
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand,
                                      net.minecraft.util.EnumFacing facing, float hitX, float hitY, float hitZ) {
        return LanternPlacement.place(player, world, pos, hand, facing, ModBlocks.LANTERN_OFF);
    }
}
