package com.tontoque28.tinyfarmboss;

import com.tontoque28.tinyfarmboss.config.TinyFarmBossConfig;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;

@Mod(TinyFarmBoss.MODID)
public class TinyFarmBoss {
    public static final String MODID = "tinyfarmbossaddon";

    public TinyFarmBoss() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, TinyFarmBossConfig.COMMON_SPEC);

        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onRegisterCommands(RegisterCommandsEvent event) {
        ModCommands.register(event.getDispatcher());
    }
}
