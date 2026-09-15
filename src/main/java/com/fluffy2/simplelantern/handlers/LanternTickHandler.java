package com.fluffy2.simplelantern.handlers;

import com.fluffy2.simplelantern.ModConfig;
import com.fluffy2.simplelantern.SimpleLantern;
import com.fluffy2.simplelantern.init.ModItems;
import com.fluffy2.simplelantern.init.ModSounds;
import com.fluffy2.simplelantern.items.ItemLanternOn;
import com.fluffy2.simplelantern.items.LanternHelper;
import com.fluffy2.simplelantern.network.PacketSyncConfig;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

@Mod.EventBusSubscriber(modid = SimpleLantern.MODID)
public class LanternTickHandler {

    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.player instanceof EntityPlayerMP) {
            SimpleLantern.NETWORK.sendTo(PacketSyncConfig.fromConfig(), (EntityPlayerMP) event.player);
        }
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END || event.player.world.isRemote) {
            return;
        }
        if (!ModConfig.lanternRequireFuel) {
            return;
        }

        EntityPlayer player = event.player;

        // надетый фонарь
        ItemStack legs = player.getItemStackFromSlot(EntityEquipmentSlot.LEGS);
        if (!legs.isEmpty() && legs.getItem() instanceof ItemLanternOn) {
            if (!burn(player, legs)) {
                player.setItemStackToSlot(EntityEquipmentSlot.LEGS, ItemLanternOn.extinguish(legs));
                announce(player);
            }
        }

        // фонари в инвентаре
        for (int i = 0; i < player.inventory.getSizeInventory(); i++) {
            ItemStack stack = player.inventory.getStackInSlot(i);
            if (stack.isEmpty() || stack.getItem() != ModItems.LANTERN_ON) {
                continue;
            }
            if (!burn(player, stack)) {
                player.inventory.setInventorySlotContents(i, ItemLanternOn.extinguish(stack));
                announce(player);
            }
        }
    }

    /** Сжигает один тик топлива. Возвращает false, если топливо кончилось. */
    private static boolean burn(EntityPlayer player, ItemStack stack) {
        int oil = LanternHelper.getOilLevel(stack);
        if (oil <= 0) {
            return false;
        }
        LanternHelper.setLantern(stack, oil - 1, LanternHelper.getReflect(stack));
        return true;
    }

    private static void announce(EntityPlayer player) {
        player.sendMessage(new TextComponentTranslation("message.simplelantern.no_fuel"));
        player.world.playSound(null, player.getPosition(), ModSounds.LANTERN_OFF,
                SoundCategory.PLAYERS, 1.0F, 1.0F);
    }
}
