package com.fluffy2.simplelantern.crafting;

import com.fluffy2.simplelantern.ModConfig;
import com.fluffy2.simplelantern.init.ModItems;
import com.fluffy2.simplelantern.items.LanternHelper;

import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.registries.IForgeRegistryEntry;

/**
 * Заправка фонаря: фонарь + бутылки топлива в любой раскладке.
 * Пустые бутылки возвращаются игроку через getRemainingItems.
 */
public class RecipeRefuelLantern extends IForgeRegistryEntry.Impl<IRecipe> implements IRecipe {

    @Override
    public boolean matches(InventoryCrafting inv, World world) {
        int lanterns = 0;
        int fuel = 0;

        for (int i = 0; i < inv.getSizeInventory(); i++) {
            ItemStack stack = inv.getStackInSlot(i);
            if (stack.isEmpty()) {
                continue;
            }
            if (stack.getItem() == ModItems.LANTERN_OFF || stack.getItem() == ModItems.LANTERN_ON) {
                lanterns++;
            } else if (stack.getItem() == ModItems.COAL_FUEL) {
                fuel++;
            } else {
                return false;
            }
        }
        return lanterns == 1 && fuel >= 1;
    }

    @Override
    public ItemStack getCraftingResult(InventoryCrafting inv) {
        ItemStack lantern = ItemStack.EMPTY;
        int fuel = 0;

        for (int i = 0; i < inv.getSizeInventory(); i++) {
            ItemStack stack = inv.getStackInSlot(i);
            if (stack.isEmpty()) {
                continue;
            }
            if (stack.getItem() == ModItems.LANTERN_OFF || stack.getItem() == ModItems.LANTERN_ON) {
                lantern = stack;
            } else if (stack.getItem() == ModItems.COAL_FUEL) {
                fuel++;
            }
        }

        if (lantern.isEmpty()) {
            return ItemStack.EMPTY;
        }

        ItemStack result = new ItemStack(lantern.getItem());
        int newOil = Math.min(ModConfig.capacityTicks(),
                LanternHelper.getOilLevel(lantern) + fuel * ModConfig.bottleTicks());
        LanternHelper.setLantern(result, newOil, LanternHelper.getReflect(lantern));
        return result;
    }

    @Override
    public NonNullList<ItemStack> getRemainingItems(InventoryCrafting inv) {
        NonNullList<ItemStack> remaining = NonNullList.withSize(inv.getSizeInventory(), ItemStack.EMPTY);
        for (int i = 0; i < remaining.size(); i++) {
            ItemStack stack = inv.getStackInSlot(i);
            if (!stack.isEmpty() && stack.getItem() == ModItems.COAL_FUEL) {
                remaining.set(i, new ItemStack(Items.GLASS_BOTTLE));
            }
        }
        return remaining;
    }

    @Override
    public boolean canFit(int width, int height) {
        return width * height >= 2;
    }

    @Override
    public ItemStack getRecipeOutput() {
        return new ItemStack(ModItems.LANTERN_OFF);
    }

    @Override
    public boolean isDynamic() {
        return true;
    }
}
