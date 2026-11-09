package com.tontoque28.tinyfarmboss;

import com.tontoque28.tinyfarmboss.config.TinyFarmBossConfig;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

@Mod(modid = TinyFarmBoss.MODID, name = "Tiny Mob Farm Addon: Bosses", version = "1.3.2", dependencies = "required-after:tinymobfarm@[1.0,);after:iceandfire", acceptedMinecraftVersions = "[1.12.2]")
public class TinyFarmBoss {
    public static final String MODID = "tinyfarmbossaddon";

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        TinyFarmBossConfig.init(event.getSuggestedConfigurationFile());
    }

    @Mod.EventHandler
    public void serverStarting(FMLServerStartingEvent event) {
        event.registerServerCommand(new ModCommands());
    }
}
