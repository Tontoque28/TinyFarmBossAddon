package com.tontoque28.tinyfarmboss;

import com.tontoque28.tinyfarmboss.config.TinyFarmBossConfig;
import com.tontoque28.tinyfarmboss.events.PlayerEvents;
import com.tontoque28.tinyfarmboss.init.ModLootModifiers;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(TinyFarmBoss.MODID)
public class TinyFarmBoss {
    public static final String MODID = "tinyfarmbossaddon";

    public TinyFarmBoss() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ModLootModifiers.REGISTRY.register(modEventBus);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, TinyFarmBossConfig.COMMON_SPEC);

        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(PlayerEvents.class);
    }

    @SubscribeEvent
    public void onRegisterCommands(RegisterCommandsEvent event) {
        ModCommands.register(event.getDispatcher());
    }
}
