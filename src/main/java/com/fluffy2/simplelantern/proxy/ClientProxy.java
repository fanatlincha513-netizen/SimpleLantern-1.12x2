package com.fluffy2.simplelantern.proxy;

import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ClientProxy extends CommonProxy {

    @Override
    public void preInit(FMLPreInitializationEvent event) {
        super.preInit(event);
        // Модели регистрируются в ModRegistry#registerModels (ModelRegistryEvent)
    }

    @Override
    public void init(FMLInitializationEvent event) {
        super.init(event);
        // Здесь при желании регистрируется TileEntitySpecialRenderer для кастомной модели фонаря
    }
}
