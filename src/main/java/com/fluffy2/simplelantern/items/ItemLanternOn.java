package com.fluffy2.simplelantern.items;

import com.fluffy2.simplelantern.SimpleLantern;
import com.fluffy2.simplelantern.init.ModBlocks;
import com.fluffy2.simplelantern.init.ModItems;
import com.fluffy2.simplelantern.init.ModSounds;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Зажжённый фонарь. Как и в оригинале — ItemArmor на слот ног,
 * чтобы его можно было носить на поясе и он светил, пока надет.
 */
public class ItemLanternOn extends ItemArmor {

    public ItemLanternOn() {
        super(SimpleLantern.LANTERN_MATERIAL, 0, EntityEquipmentSlot.LEGS);
        setRegistryName(SimpleLantern.MODID, "lantern_on");
        setUnlocalizedName(SimpleLantern.MODID + ".lantern_on");
        setCreativeTab(null); // выдаётся только зажиганием выключенного фонаря
        setMaxStackSize(1);
        setMaxDamage(0);
    }

    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EntityEquipmentSlot slot, String type) {
        return SimpleLantern.MODID + ":textures/models/armor/lantern_layer_1.png";
    }

    @Override
    public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
        return false;
    }

    @Override
    public boolean isDamageable() {
        return false;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        ItemStack stack = player.getHeldItem(hand);

        if (player.isSneaking()) {
            if (!world.isRemote) {
                LanternHelper.setLantern(stack, LanternHelper.getOilLevel(stack), !LanternHelper.getReflect(stack));
                world.playSound(null, player.getPosition(), ModSounds.CHANGEMODE, SoundCategory.PLAYERS, 1.0F, 1.0F);
            }
            return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, stack);
        }

        if (!world.isRemote) {
            player.setHeldItem(hand, extinguish(stack));
            world.playSound(null, player.getPosition(), ModSounds.LANTERN_OFF, SoundCategory.PLAYERS, 1.0F, 1.0F);
        }
        return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, stack);
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand,
                                      EnumFacing facing, float hitX, float hitY, float hitZ) {
        return LanternPlacement.place(player, world, pos, hand, facing, ModBlocks.LANTERN_ON);
    }

    /** Превращает зажжённый фонарь в выключенный, сохраняя NBT. */
    public static ItemStack extinguish(ItemStack lit) {
        ItemStack off = new ItemStack(ModItems.LANTERN_OFF);
        LanternHelper.setLantern(off, LanternHelper.getOilLevel(lit), LanternHelper.getReflect(lit));
        return off;
    }

    @Override
    public void onArmorTick(World world, EntityPlayer player, ItemStack stack) {
        // расход топлива для надетого фонаря обрабатывается в LanternTickHandler
    }

    @Override
    public boolean isValidArmor(ItemStack stack, EntityEquipmentSlot slot, Entity entity) {
        return slot == EntityEquipmentSlot.LEGS;
    }

    @Override
    public EntityEquipmentSlot getEquipmentSlot(ItemStack stack) {
        return EntityEquipmentSlot.LEGS;
    }

    @Override
    public boolean canEquip(ItemStack stack, EntityEquipmentSlot armorType, Entity entity) {
        return armorType == EntityEquipmentSlot.LEGS && entity instanceof EntityLivingBase;
    }
}
