package com.hlhtstudios.taczcustomdeath.trigger;

import com.hlhtstudios.taczcustomdeath.Taczcustomdeath;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.scores.PlayerTeam;
import net.minecraft.world.scores.Scoreboard;
import net.minecraft.world.scores.Team;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = "taczcustomdeath")
public class PlayerSpawnEvent {
    private static final String SILENT_TEAM = "silent_death";
    @SubscribeEvent
    public static void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
        Taczcustomdeath.LOGGER.info("On Player Spawn");
        if (event.getEntity() instanceof ServerPlayer player) {
            Scoreboard sb = player.level().getScoreboard();
            PlayerTeam team = sb.getPlayerTeam(SILENT_TEAM);
            if (team == null) {
                team = sb.addPlayerTeam(SILENT_TEAM);
                team.setDeathMessageVisibility(Team.Visibility.NEVER); //
            }

            sb.removePlayerFromTeam(player.getScoreboardName(), team); // Re-enable death messages for next deaths
        }
    }
}
