package com.hlhtstudios.taczcustomdeath.network;

import com.hlhtstudios.taczcustomdeath.Taczcustomdeath;
import com.hlhtstudios.taczcustomdeath.network.event.ServerSendGunKill;
import com.tacz.guns.network.message.ClientMessagePlayerShoot;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

public class NetworkHandler {
    private static final String VERSION = "1.0";
    public static SimpleChannel CHANNEL;


    public static void init() {
        int id = 0;
        CHANNEL = NetworkRegistry.newSimpleChannel(
                new ResourceLocation("taczcustomdeath", "main"),
                () -> VERSION, VERSION::equals, VERSION::equals
        );
        CHANNEL.registerMessage(id++, ServerSendGunKill.class, ServerSendGunKill::encode, ServerSendGunKill::decode, ServerSendGunKill::handle);
    }

}
