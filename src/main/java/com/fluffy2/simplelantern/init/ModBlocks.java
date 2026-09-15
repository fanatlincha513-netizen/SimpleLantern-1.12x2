package com.fluffy2.simplelantern.init;

import com.fluffy2.simplelantern.SimpleLantern;

import net.minecraft.block.Block;
import net.minecraftforge.fml.common.registry.GameRegistry;

@GameRegistry.ObjectHolder(SimpleLantern.MODID)
public class ModBlocks {

    @GameRegistry.ObjectHolder("lantern_off")
    public static final Block LANTERN_OFF = null;

    @GameRegistry.ObjectHolder("lantern_on")
    public static final Block LANTERN_ON = null;
}
