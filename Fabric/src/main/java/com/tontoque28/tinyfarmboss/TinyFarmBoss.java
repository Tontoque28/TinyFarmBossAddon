package com.tontoque28.tinyfarmboss;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TinyFarmBoss implements ModInitializer {
    // Regla de Jarvis: El MODID debe coincidir exactamente con tu fabric.mod.json
    public static final String MODID = "tinyfarmbossaddon";
    public static final Logger LOGGER = LoggerFactory.getLogger(MODID);
    public static ModConfig config;

    @Override
    public void onInitialize() {
        LOGGER.info("Starting TinyFarmBoss Remastered - Fabric Edition [1.20.1]");

        // Cargar configuración
        config = ModConfig.load();

        // 1. Registro de Comandos
        // Reemplaza al RegisterCommandsEvent de Forge
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            ModCommands.register(dispatcher);
            LOGGER.debug("Comandos de TinyFarmBoss registrados correctamente.");
        });

        // 2. Registro de Eventos de Interacción (Lazo)
        // Reemplaza al PlayerInteractEvent.EntityInteract de Forge
        // NOTA: Asegúrate de actualizar el método onEntityInteract en LassoHandler
        // para que coincida con la firma de UseEntityCallback (ver abajo).
        UseEntityCallback.EVENT.register(LassoHandler::onEntityInteract);

        // 3. Inyección de Botín Global
        LootTableInjector.register();

        LOGGER.info("TinyFarmBoss: Todos los sistemas de captura y comandos están operativos.");
    }
}