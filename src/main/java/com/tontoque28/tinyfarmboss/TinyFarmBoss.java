package com.tontoque28.tinyfarmboss;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TinyFarmBoss implements ModInitializer {

    public static final String MODID = "tinyfarmbossaddon";
    public static final Logger LOGGER = LoggerFactory.getLogger(MODID);
    public static ModConfig config;

    @Override
    public void onInitialize() {
        LOGGER.info("Starting TinyFarmBoss Remastered - Fabric Edition [1.20.1]");

        config = ModConfig.load();

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            ModCommands.register(dispatcher);
            LOGGER.debug("Comandos de TinyFarmBoss registrados correctamente.");
        });

        UseEntityCallback.EVENT.register(LassoHandler::onEntityInteract);

        LootTableInjector.register();

        LOGGER.info("TinyFarmBoss: Todos los sistemas de captura y comandos están operativos.");
    }
}
