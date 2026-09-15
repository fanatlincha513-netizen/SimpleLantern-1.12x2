package com.fluffy2.simplelantern;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * Замена ручной Configuration из 1.7.10. В 1.12.2 конфиг удобнее описывать
 * аннотациями: файл создаётся сам, GUI конфига работает из коробки.
 */
@Config(modid = SimpleLantern.MODID, name = "SimpleLantern")
public class ModConfig {

    @Config.Comment("Уровень света от фонаря (максимум 15). Изменения применяются после перезапуска игры.")
    @Config.RangeInt(min = 0, max = 15)
    public static int lanternLightValue = 14;

    @Config.Comment("Требуется ли фонарю топливо.")
    public static boolean lanternRequireFuel = true;

    @Config.Comment("Сколько минут горения добавляет одна бутылка топлива.")
    @Config.RangeInt(min = 1, max = 10000)
    public static int minutesPerBottle = 10;

    @Config.Comment("Сколько минут горит полностью заправленный фонарь.")
    @Config.RangeInt(min = 1, max = 10000)
    public static int lanternFuelCapacity = 60;

    public static int capacityTicks() {
        return lanternFuelCapacity * 60 * 20;
    }

    public static int bottleTicks() {
        return minutesPerBottle * 60 * 20;
    }

    @Mod.EventBusSubscriber(modid = SimpleLantern.MODID)
    public static class ConfigSync {
        @SubscribeEvent
        public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
            if (SimpleLantern.MODID.equals(event.getModID())) {
                ConfigManager.sync(SimpleLantern.MODID, Config.Type.INSTANCE);
            }
        }
    }
}
