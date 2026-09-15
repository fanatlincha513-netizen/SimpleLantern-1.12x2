package com.fluffy2.simplelantern.init;

import com.fluffy2.simplelantern.SimpleLantern;
import com.fluffy2.simplelantern.blocks.BlockLanternOff;
import com.fluffy2.simplelantern.blocks.BlockLanternOn;
import com.fluffy2.simplelantern.blocks.TileEntityLantern;
import com.fluffy2.simplelantern.crafting.RecipeRefuelLantern;
import com.fluffy2.simplelantern.items.ItemCoalFuel;
import com.fluffy2.simplelantern.items.ItemLanternOff;
import com.fluffy2.simplelantern.items.ItemLanternOn;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Вся регистрация 1.12.2 идёт через события реестра.
 * Прямых вызовов GameRegistry.registerItem/registerBlock больше не существует.
 */
@Mod.EventBusSubscriber(modid = SimpleLantern.MODID)
public class ModRegistry {

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        event.getRegistry().registerAll(
                new BlockLanternOff(),
                new BlockLanternOn()
        );
        GameRegistry.registerTileEntity(TileEntityLantern.class,
                new ResourceLocation(SimpleLantern.MODID, "lantern"));
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        event.getRegistry().registerAll(
                new ItemCoalFuel(),
                new ItemLanternOff(),
                new ItemLanternOn()
        );
    }

    @SubscribeEvent
    public static void registerSounds(RegistryEvent.Register<SoundEvent> event) {
        event.getRegistry().registerAll(
                ModSounds.create("lantern_on"),
                ModSounds.create("lantern_off"),
                ModSounds.create("changemode")
        );
    }

    @SubscribeEvent
    public static void registerRecipes(RegistryEvent.Register<IRecipe> event) {
        // Обычные рецепты лежат в assets/simplelantern/recipes/*.json
        event.getRegistry().register(new RecipeRefuelLantern()
                .setRegistryName(SimpleLantern.MODID, "refuel_lantern"));
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public static void registerModels(ModelRegistryEvent event) {
        register(ModItems.COAL_FUEL);
        register(ModItems.LANTERN_OFF);
        register(ModItems.LANTERN_ON);
    }

    @SideOnly(Side.CLIENT)
    private static void register(Item item) {
        ModelLoader.setCustomModelResourceLocation(item, 0,
                new ModelResourceLocation(item.getRegistryName(), "inventory"));
    }
}
