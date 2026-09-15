package com.fluffy2.simplelantern.items;

import com.fluffy2.simplelantern.SimpleLantern;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class ItemCoalFuel extends Item {

    public ItemCoalFuel() {
        setRegistryName(SimpleLantern.MODID, "coal_fuel");
        setUnlocalizedName(SimpleLantern.MODID + ".coal_fuel");
        setCreativeTab(CreativeTabs.TOOLS);
    }
}
