package com.fluffy2.simplelantern.network;

import com.fluffy2.simplelantern.ModConfig;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/** Сервер присылает клиенту свои настройки при входе. */
public class PacketSyncConfig implements IMessage {

    private int lightValue;
    private boolean requireFuel;
    private int minutesPerBottle;
    private int fuelCapacity;

    public PacketSyncConfig() {}

    public static PacketSyncConfig fromConfig() {
        PacketSyncConfig p = new PacketSyncConfig();
        p.lightValue = ModConfig.lanternLightValue;
        p.requireFuel = ModConfig.lanternRequireFuel;
        p.minutesPerBottle = ModConfig.minutesPerBottle;
        p.fuelCapacity = ModConfig.lanternFuelCapacity;
        return p;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(lightValue);
        buf.writeBoolean(requireFuel);
        buf.writeInt(minutesPerBottle);
        buf.writeInt(fuelCapacity);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        lightValue = buf.readInt();
        requireFuel = buf.readBoolean();
        minutesPerBottle = buf.readInt();
        fuelCapacity = buf.readInt();
    }

    public static class Handler implements IMessageHandler<PacketSyncConfig, IMessage> {
        @Override
        public IMessage onMessage(final PacketSyncConfig message, MessageContext ctx) {
            // Обработчик выполняется в сетевом потоке — переносим в главный
            Minecraft.getMinecraft().addScheduledTask(new Runnable() {
                @Override
                public void run() {
                    ModConfig.lanternLightValue = message.lightValue;
                    ModConfig.lanternRequireFuel = message.requireFuel;
                    ModConfig.minutesPerBottle = message.minutesPerBottle;
                    ModConfig.lanternFuelCapacity = message.fuelCapacity;
                }
            });
            return null;
        }
    }
}
