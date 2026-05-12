package com.tontoque28.tinyfarmboss;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;

public class ModCommands {

    private static final String LASSO_NAMESPACE = "tinymobfarm";
    private static final String LASSO_PATH = "lasso";

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("tinyfarmbossaddon")
            .requires(source -> source.hasPermission(2))
            .then(Commands.literal("give")
                .then(Commands.argument("boss", StringArgumentType.word())
                    .suggests(SUGGEST_BOSSES)
                    .executes(ModCommands::giveLasso)
                )
            )
            .then(Commands.literal("test")
                .executes(ModCommands::giveAllTestLassos)
            )
        );
    }

    private static final SuggestionProvider<CommandSourceStack> SUGGEST_BOSSES = (context, builder) ->
            SharedSuggestionProvider.suggest(new String[]{"wither", "ender_dragon", "warden"}, builder);

    private static int giveAllTestLassos(CommandContext<CommandSourceStack> context) {
        CommandSourceStack source = context.getSource();

        if (!(source.getEntity() instanceof net.minecraft.world.entity.player.Player player)) {
            source.sendSuccess(() -> Component.literal("Comando ejecutado desde consola, pero requiere jugador."), true);
            return 1;
        }

        Item lassoItem = ForgeRegistries.ITEMS.getValue(ResourceLocation.fromNamespaceAndPath(LASSO_NAMESPACE, LASSO_PATH));
        if (lassoItem == null) {
            source.sendFailure(Component.literal("Error: No se encontró el item " + LASSO_NAMESPACE + ":" + LASSO_PATH));
            return 0;
        }

        // Estructura: {mobId, displayName, lootTableLocation, maxHealth, ageTicks (opcional)}
        String[][] testMobs = {
            {"minecraft:wither", "Wither", "tinyfarmbossaddon:entities/wither_custom", "300.0", "0"},
            {"minecraft:ender_dragon", "Ender Dragon", "tinyfarmbossaddon:entities/ender_dragon_custom", "200.0", "0"},
            {"minecraft:warden", "Warden", "tinyfarmbossaddon:entities/warden_custom", "500.0", "0"},
            {"minecraft:slime", "Slime", "minecraft:entities/slime", "10.0", "0"},
            {"iceandfire:fire_dragon", "Fire Dragon (Stage 4)", "iceandfire:entities/fire_dragon", "500.0", "1728000"},
            {"iceandfire:ice_dragon", "Ice Dragon (Stage 5)", "iceandfire:entities/ice_dragon", "500.0", "2000000"}
        };

        for (String[] mobDataArray : testMobs) {
            String mobId = mobDataArray[0];
            String displayName = "Test " + mobDataArray[1];
            String lootTable = mobDataArray[2];
            double maxHealth = Double.parseDouble(mobDataArray[3]);
            long ageTicks = Long.parseLong(mobDataArray[4]);

            ItemStack lassoStack = new ItemStack(lassoItem);
            CompoundTag capturedMobTag = new CompoundTag();
            CompoundTag mobData = new CompoundTag();

            mobData.putString("id", mobId);
            mobData.putFloat("Health", (float) maxHealth);
            if (ageTicks > 0) {
                mobData.putLong("AgeTicks", ageTicks);
            }

            capturedMobTag.putString("mobName", displayName);
            capturedMobTag.putBoolean("mobHostile", true);
            capturedMobTag.putDouble("mobMaxHealth", maxHealth);
            capturedMobTag.putString("mobId", mobId);
            capturedMobTag.putString("mobLootTableLocation", lootTable);
            capturedMobTag.put("mobData", mobData);
            capturedMobTag.putDouble("mobHealth", maxHealth);

            lassoStack.getOrCreateTag().put("capturedMob", capturedMobTag);
            lassoStack.setHoverName(Component.literal(displayName));

            if (!player.getInventory().add(lassoStack)) {
                ItemEntity itemEntity = player.drop(lassoStack, false);
                if (itemEntity != null) {
                    itemEntity.setNoPickUpDelay();
                    itemEntity.setThrower(player.getUUID());
                }
            }
        }

        source.sendSuccess(() -> Component.literal("§a[TinyFarmBoss] §fSe entregaron todos los lassos de prueba (Jefes, Slime, Dragones Stage 4 y 5)."), true);
        return 1;
    }

    private static int giveLasso(CommandContext<CommandSourceStack> context) {
        String bossName = StringArgumentType.getString(context, "boss");
        CommandSourceStack source = context.getSource();
        
        Item lassoItem = ForgeRegistries.ITEMS.getValue(ResourceLocation.fromNamespaceAndPath(LASSO_NAMESPACE, LASSO_PATH));
        if (lassoItem == null) {
            source.sendFailure(Component.literal("Error: No se encontró el item " + LASSO_NAMESPACE + ":" + LASSO_PATH));
            return 0;
        }

        ItemStack lassoStack = new ItemStack(lassoItem);
        CompoundTag capturedMobTag = new CompoundTag();
        CompoundTag mobData = new CompoundTag();

        String mobId;
        String lootTable;
        double maxHealth;
        String displayName;

        switch (bossName) {
            case "wither":
                mobId = "minecraft:wither";
                lootTable = "tinyfarmbossaddon:entities/wither_custom";
                maxHealth = 300.0d;
                displayName = "Wither";
                break;
            case "ender_dragon":
                mobId = "minecraft:ender_dragon";
                lootTable = "tinyfarmbossaddon:entities/ender_dragon_custom";
                maxHealth = 200.0d;
                displayName = "Ender Dragon";
                break;
            case "warden":
                mobId = "minecraft:warden";
                lootTable = "tinyfarmbossaddon:entities/warden_custom";
                maxHealth = 500.0d;
                displayName = "Warden";
                break;
            default:
                source.sendFailure(Component.literal("Boss desconocido: " + bossName));
                return 0;
        }

        // Configurar mobData básico
        mobData.putString("id", mobId);
        mobData.putFloat("Health", (float) maxHealth);

        // Configurar capturedMob
        capturedMobTag.putString("mobName", displayName);
        capturedMobTag.putBoolean("mobHostile", true);
        capturedMobTag.putDouble("mobMaxHealth", maxHealth);
        capturedMobTag.putString("mobId", mobId);
        capturedMobTag.putString("mobLootTableLocation", lootTable);
        capturedMobTag.put("mobData", mobData);
        capturedMobTag.putDouble("mobHealth", maxHealth);

        lassoStack.getOrCreateTag().put("capturedMob", capturedMobTag);
        lassoStack.setHoverName(Component.literal(displayName));

        // Dar al jugador o dropear si no hay espacio
        if (source.getEntity() instanceof net.minecraft.world.entity.player.Player player) {
            if (!player.getInventory().add(lassoStack)) {
                ItemEntity itemEntity = player.drop(lassoStack, false);
                if (itemEntity != null) {
                    itemEntity.setNoPickUpDelay();
                    itemEntity.setThrower(player.getUUID()); // CORREGIDO: setOwner -> setThrower
                }
            }
        } else {
            source.sendSuccess(() -> Component.literal("Comando ejecutado desde consola, pero requiere jugador."), true);
            return 1;
        }

        source.sendSuccess(() -> Component.literal("Lasso de " + displayName + " entregado."), true);
        return 1;
    }
}
