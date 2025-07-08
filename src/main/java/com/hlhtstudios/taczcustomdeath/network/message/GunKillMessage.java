package com.hlhtstudios.taczcustomdeath.network.message;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;

public class GunKillMessage {
    public final ResourceLocation image;
    public final LivingEntity attacker;
    public final LivingEntity victim;
    public final boolean isHeadShot;

    public GunKillMessage(ResourceLocation image, LivingEntity attacker, LivingEntity victim,
                          boolean isHeadShot) {
        this.image = image;
        this.attacker = attacker;
        this.victim = victim;
        this.isHeadShot = isHeadShot;
    }
}
