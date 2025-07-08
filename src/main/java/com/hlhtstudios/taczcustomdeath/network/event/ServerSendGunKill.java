package com.hlhtstudios.taczcustomdeath.network.event;

import com.hlhtstudios.taczcustomdeath.Taczcustomdeath;
import com.hlhtstudios.taczcustomdeath.client.KillHUD;
import com.hlhtstudios.taczcustomdeath.network.message.ClientGunKillHandler;
import com.hlhtstudios.taczcustomdeath.network.message.GunKillMessage;
import com.tacz.guns.api.TimelessAPI;
import com.tacz.guns.client.resource.GunDisplayInstance;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.scores.Team;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.network.NetworkEvent;
import net.minecraft.world.entity.LivingEntity;

import java.util.UUID;
import java.util.function.Supplier;

public class ServerSendGunKill {
    public final ResourceLocation gunId;
    public final ResourceLocation displayId;
    public final UUID attackerId;
    public final UUID victimId;
    public final String attackerName;
    public final String victimName;
    public final String attackerTeam;
    public final String victimTeam;
    public final boolean isHeadShot;

    public ServerSendGunKill(ResourceLocation gunId, ResourceLocation displayId,
                             UUID attackerId, UUID victimId,
                             String attackerName, String victimName,
                             String attackerTeam, String victimTeam,
                             boolean isHeadShot) {
        this.gunId = gunId;
        this.displayId = displayId;
        this.attackerId = attackerId;
        this.victimId = victimId;
        this.attackerName = attackerName;
        this.victimName = victimName;
        this.attackerTeam = attackerTeam;
        this.victimTeam = victimTeam;
        this.isHeadShot = isHeadShot;
    }

    public static void encode(ServerSendGunKill pkt, FriendlyByteBuf buf) {
        Taczcustomdeath.LOGGER.info("Encoding packet: " + pkt.attackerId + ", " + pkt.attackerName);
        Taczcustomdeath.LOGGER.info("Att Team: " + pkt.attackerTeam + "Vic Team" + pkt.victimTeam);
        buf.writeResourceLocation(pkt.gunId);
        buf.writeResourceLocation(pkt.displayId);
        buf.writeUUID(pkt.attackerId);
        buf.writeUUID(pkt.victimId);
        buf.writeUtf(pkt.attackerName);
        buf.writeUtf(pkt.victimName);
        buf.writeUtf(pkt.attackerTeam);
        buf.writeUtf(pkt.victimTeam);
        buf.writeBoolean(pkt.isHeadShot);
    }

    public static ServerSendGunKill decode(FriendlyByteBuf buf) {
        Taczcustomdeath.LOGGER.info("Readable bytes: " + buf.readableBytes());
        return new ServerSendGunKill(
                buf.readResourceLocation(),
                buf.readResourceLocation(),
                buf.readUUID(),
                buf.readUUID(),
                buf.readUtf(32767),
                buf.readUtf(32767),
                buf.readUtf(32767),
                buf.readUtf(32767),
                buf.readBoolean()
        );
    }

    public static void handle(ServerSendGunKill pkt, Supplier<NetworkEvent.Context> ctx) {
        NetworkEvent.Context context = ctx.get();
        Taczcustomdeath.LOGGER.info("Someone has been killed Both");

        if (context.getDirection().getReceptionSide().isClient()) {
            context.enqueueWork(() -> {
                // Only client-side logic here!
                ClientGunKillHandler.handleGunKill(pkt);
            });
        }

        context.setPacketHandled(true);
    }
}
