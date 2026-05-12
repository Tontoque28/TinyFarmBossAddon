package com.tontoque28.tinyfarmboss;

import com.mojang.logging.LogUtils;
import com.tontoque28.tinyfarmboss.config.TinyFarmBossConfig;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(TinyFarmBoss.MODID)
public class TinyFarmBoss {
    public static final String MODID = "tinyfarmbossaddon";
    private static final Logger LOGGER = LogUtils.getLogger();

    public TinyFarmBoss() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, TinyFarmBossConfig.COMMON_SPEC);
        
        // La lógica del GLM se registrará aquí si es necesario, pero por ahora la dejamos fuera.
        
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onRegisterCommands(RegisterCommandsEvent event) {
        ModCommands.register(event.getDispatcher());
    }
}
