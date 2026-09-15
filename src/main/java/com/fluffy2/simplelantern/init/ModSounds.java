package com.fluffy2.simplelantern.init;

import com.fluffy2.simplelantern.SimpleLantern;

import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 * В 1.7.10 звук игрался по строке "simplelantern:Lanternon".
 * В 1.12.2 звуки — полноценные объекты реестра.
 */
@GameRegistry.ObjectHolder(SimpleLantern.MODID)
public class ModSounds {

    @GameRegistry.ObjectHolder("lantern_on")
    public static final SoundEvent LANTERN_ON = null;

    @GameRegistry.ObjectHolder("lantern_off")
    public static final SoundEvent LANTERN_OFF = null;

    @GameRegistry.ObjectHolder("changemode")
    public static final SoundEvent CHANGEMODE = null;

    public static SoundEvent create(String name) {
        net.minecraft.util.ResourceLocation id =
                new net.minecraft.util.ResourceLocation(SimpleLantern.MODID, name);
        return new SoundEvent(id).setRegistryName(id);
    }
}
