package com.fluffy2.simplelantern.init;

import com.fluffy2.simplelantern.SimpleLantern;

import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;

@GameRegistry.ObjectHolder(SimpleLantern.MODID)
public class ModItems {

    @GameRegistry.ObjectHolder("coal_fuel")
    public static final Item COAL_FUEL = null;

    @GameRegistry.ObjectHolder("lantern_off")
    public static final Item LANTERN_OFF = null;

    @GameRegistry.ObjectHolder("lantern_on")
    public static final Item LANTERN_ON = null;
}
