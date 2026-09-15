package com.fluffy2.simplelantern.items;

import com.fluffy2.simplelantern.ModConfig;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

/**
 * Работа с NBT фонаря. В 1.7.10 это лежало прямо в классах предметов,
 * здесь вынесено отдельно, чтобы блок, предмет и рецепт использовали одно и то же.
 */
public final class LanternHelper {

    public static final String TAG_OIL = "SimpleLantern.Oil";
    public static final String TAG_REFLECT = "SimpleLantern.Reflect";

    private LanternHelper() {}

    /** Остаток топлива в тиках. */
    public static int getOilLevel(ItemStack stack) {
        NBTTagCompound tag = stack.getTagCompound();
        return tag == null ? 0 : tag.getInteger(TAG_OIL);
    }

    /** Режим отражателя (узкий/широкий свет) — переключается шифт+ПКМ. */
    public static boolean getReflect(ItemStack stack) {
        NBTTagCompound tag = stack.getTagCompound();
        return tag != null && tag.getBoolean(TAG_REFLECT);
    }

    public static void setLantern(ItemStack stack, int oil, boolean reflect) {
        NBTTagCompound tag = stack.getTagCompound();
        if (tag == null) {
            tag = new NBTTagCompound();
            stack.setTagCompound(tag);
        }
        tag.setInteger(TAG_OIL, Math.max(0, Math.min(oil, ModConfig.capacityTicks())));
        tag.setBoolean(TAG_REFLECT, reflect);
    }

    public static boolean hasFuel(ItemStack stack) {
        return !ModConfig.lanternRequireFuel || getOilLevel(stack) > 0;
    }
}
