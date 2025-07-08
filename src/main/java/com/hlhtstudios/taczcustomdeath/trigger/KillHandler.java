package com.hlhtstudios.taczcustomdeath.trigger;

import com.hlhtstudios.taczcustomdeath.network.event.ServerSendGunKill;
import com.tacz.guns.api.TimelessAPI;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import com.tacz.guns.api.event.common.EntityKillByGunEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;
import com.hlhtstudios.taczcustomdeath.network.NetworkHandler;


@Mod.EventBusSubscriber(modid = "taczcustomdeath")
public class KillHandler {

    @SubscribeEvent
    public static void onGunKill(EntityKillByGunEvent event) {
        // Ensure we are on the server side
        if (event.getLogicalSide() != LogicalSide.SERVER) return;

        LivingEntity attacker = event.getAttacker();
        LivingEntity victim = event.getKilledEntity();
        ResourceLocation gunId = event.getGunId();
        ResourceLocation displayId = event.getGunDisplayId();
        boolean isHeadshot = event.isHeadShot();

        if (attacker == null || victim == null) return;

        // Broadcast to all nearby players who should see the kill
        // You can optimize this to filter by dimension, proximity, or just send to the victim/attacker only
        if (attacker.level().isClientSide) return; // safety check

        int attackerType = 0;
        int victimType = 0;
        if (!(attacker instanceof Player) || !(victim instanceof Player)) return;

        boolean isSelf = attacker.getUUID().equals(victim.getUUID());
        boolean isTeammate = attacker.isAlliedTo(victim);
        String attackerTeam;
        String victimTeam;
        if (attacker.getTeam() == null){
            attackerTeam = "";
        }
        else{
            attackerTeam = attacker.getTeam().getName();
        }
        if (victim.getTeam() == null){
            victimTeam = "";
        }
        else{
            victimTeam = victim.getTeam().getName();
        }



        for (Player player : attacker.level().players()) {
            if (player instanceof ServerPlayer serverPlayer) {
                NetworkHandler.CHANNEL.send(
                        PacketDistributor.PLAYER.with(() -> serverPlayer),
                        new ServerSendGunKill(
                                gunId,
                                displayId,
                                attacker.getUUID(),
                                victim.getUUID(),
                                attacker.getName().getString(),
                                victim.getName().getString(),
                                attackerTeam,
                                victimTeam,
                                isHeadshot
                        )
                );
            }
        }
    }
}