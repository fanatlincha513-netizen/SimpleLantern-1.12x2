package com.fluffy2.simplelantern;

import com.fluffy2.simplelantern.network.PacketSyncConfig;
import com.fluffy2.simplelantern.proxy.CommonProxy;

import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemArmor;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

@Mod(modid = SimpleLantern.MODID,
     name = SimpleLantern.NAME,
     version = SimpleLantern.VERSION,
     acceptedMinecraftVersions = "[1.12.2]")
public class SimpleLantern {

    public static final String MODID = "simplelantern";
    public static final String NAME = "Simple Lantern";
    public static final String VERSION = "1.1.0";

    @Mod.Instance(MODID)
    public static SimpleLantern instance;

    @SidedProxy(clientSide = "com.fluffy2.simplelantern.proxy.ClientProxy",
                serverSide = "com.fluffy2.simplelantern.proxy.CommonProxy")
    public static CommonProxy proxy;

    /** Канал для синхронизации конфига сервер -> клиент. */
    public static SimpleNetworkWrapper NETWORK;

    /** Материал "брони" для надетого фонаря. Защиты не даёт. */
    public static final ItemArmor.ArmorMaterial LANTERN_MATERIAL = EnumHelper.addArmorMaterial(
            "lantern", MODID + ":lantern", 0, new int[]{0, 0, 0, 0}, 0,
            SoundEvents.ITEM_ARMOR_EQUIP_GENERIC, 0.0F);

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        NETWORK = NetworkRegistry.INSTANCE.newSimpleChannel(MODID);
        NETWORK.registerMessage(PacketSyncConfig.Handler.class, PacketSyncConfig.class, 0, Side.CLIENT);
        proxy.preInit(event);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.init(event);
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        proxy.postInit(event);
    }
}
