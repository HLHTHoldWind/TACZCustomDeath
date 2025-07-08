package com.hlhtstudios.taczcustomdeath.client;

import com.tacz.guns.client.gui.overlay.GunHudOverlay;
import com.tacz.guns.client.gui.overlay.HeatBarOverlay;
import com.tacz.guns.client.gui.overlay.InteractKeyTextOverlay;
import com.tacz.guns.client.gui.overlay.KillAmountOverlay;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import static net.minecraftforge.client.gui.overlay.VanillaGuiOverlay.CROSSHAIR;

@Mod.EventBusSubscriber(modid = "taczcustomdeath", value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientSetupEvent {

    @SubscribeEvent
    public static void onRegisterGuiOverlays(RegisterGuiOverlaysEvent event) {
        // init HUD
        KillHUD.INSTANCE = new KillHUD();
        event.registerAboveAll("tac_kill_info_overlay", KillHUD.INSTANCE);

    }

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        // Optional: init HUD if needed
        // GunIconRegistry.registerDefaults();
    }

}
