package com.hlhtstudios.taczcustomdeath.trigger;
import com.hlhtstudios.taczcustomdeath.Taczcustomdeath;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.scores.Scoreboard;
import net.minecraft.world.scores.Team;
import net.minecraft.world.scores.PlayerTeam;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

@Mod.EventBusSubscriber(modid = "taczcustomdeath")
public class PlayerDeathEvent {
    private static final String SILENT_TEAM = "silent_death";
    //private static final List<DamageType> GUN_DAMAGE = List.of(ModDamageTypes.BULLET,);
    @SubscribeEvent
    public static void onPlayerDeath(LivingDeathEvent event) {
        Taczcustomdeath.LOGGER.info("On Player Death");
        if (!(event.getEntity() instanceof ServerPlayer player)) return;
        Taczcustomdeath.LOGGER.info(event.getSource().type().msgId());
        //if !(event.getSource().type() in )
        Scoreboard sb = player.level().getScoreboard();
        PlayerTeam team = sb.getPlayerTeam(SILENT_TEAM);
        if (team == null) {
            team = sb.addPlayerTeam(SILENT_TEAM);
            team.setDeathMessageVisibility(Team.Visibility.NEVER); //
        }

        //sb.addPlayerToTeam(player.getScoreboardName(), team);
    }
}
