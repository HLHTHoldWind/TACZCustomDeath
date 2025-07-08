package com.hlhtstudios.taczcustomdeath.network.message;

import com.hlhtstudios.taczcustomdeath.network.event.ServerSendGunKill;
import com.hlhtstudios.taczcustomdeath.Taczcustomdeath;
import com.hlhtstudios.taczcustomdeath.client.KillHUD;
import com.hlhtstudios.taczcustomdeath.network.message.GunKillMessage;
import com.tacz.guns.api.TimelessAPI;
import com.tacz.guns.client.resource.GunDisplayInstance;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.scores.Team;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.network.NetworkEvent;
import net.minecraft.world.entity.LivingEntity;

@OnlyIn(Dist.CLIENT)
public class ClientGunKillHandler {

    public static void handleGunKill(ServerSendGunKill pkt) {
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
            Minecraft mc = Minecraft.getInstance();
            mc.execute(() -> {
                Taczcustomdeath.LOGGER.info("Someone has been killed Client");

                if (mc.level == null || mc.player == null) {
                    Taczcustomdeath.LOGGER.error("Minecraft level or player is null");
                    return;
                }

                Taczcustomdeath.LOGGER.info("Attacker ID");
                Taczcustomdeath.LOGGER.info(pkt.attackerName);
                Taczcustomdeath.LOGGER.info(pkt.attackerId.toString());
                Taczcustomdeath.LOGGER.info("Victim ID");
                Taczcustomdeath.LOGGER.info(pkt.victimName);
                Taczcustomdeath.LOGGER.info(pkt.victimId.toString());



                GunDisplayInstance display = TimelessAPI.getGunDisplay(pkt.displayId, pkt.gunId).orElse(null);

                ResourceLocation image = (display != null) ?
                        display.getHUDTexture() :
                        new ResourceLocation("taczcustomdeath", "textures/hud/default_icon.png");

                Taczcustomdeath.LOGGER.info("Calling pushKill...");
                KillHUD.INSTANCE.pushKill(pkt.attackerId, pkt.victimId,
                        pkt.attackerName, pkt.victimName,
                        pkt.attackerTeam, pkt.victimTeam,
                        image, pkt.isHeadShot);
            });
            return;
        });
    }
}
