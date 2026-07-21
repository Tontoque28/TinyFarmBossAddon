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
import net.minecraft.core.registries.BuiltInRegistries;

import java.util.Arrays;

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
                .executes(ModCommands::giveAllLassos)
            )
        );
    }

    private static final SuggestionProvider<CommandSourceStack> SUGGEST_BOSSES = (context, builder) ->
            SharedSuggestionProvider.suggest(new String[]{"wither", "ender_dragon", "warden", "slime", "barbarian", "fire_dragon", "ice_dragon", "lightning_dragon"}, builder);

    private static int giveLasso(CommandContext<CommandSourceStack> context) {
        String bossName = StringArgumentType.getString(context, "boss");
        CommandSourceStack source = context.getSource();
        
        giveLassoByName(source, bossName);
        
        return 1;
    }

    private static int giveAllLassos(CommandContext<CommandSourceStack> context) {
        CommandSourceStack source = context.getSource();
        String[] allBosses = {"wither", "ender_dragon", "warden", "slime", "barbarian", "fire_dragon", "ice_dragon", "lightning_dragon"};
        
        Arrays.stream(allBosses).forEach(bossName -> giveLassoByName(source, bossName));

        source.sendSuccess(() -> Component.literal("Todos los lazos de prueba han sido entregados."), true);
        return 1;
    }

    private static void giveLassoByName(CommandSourceStack source, String bossName) {
        Item lassoItem = BuiltInRegistries.ITEM.get(new ResourceLocation(LASSO_NAMESPACE, LASSO_PATH));
        if (lassoItem == null || lassoItem == net.minecraft.world.item.Items.AIR) {
            source.sendFailure(Component.literal("Error: No se encontró el item " + LASSO_NAMESPACE + ":" + LASSO_PATH));
            return;
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
            case "slime":
                mobId = "minecraft:slime";
                lootTable = "minecraft:entities/slime";
                maxHealth = 16.0d;
                displayName = "Slime";
                break;
            case "barbarian":
                mobId = "minecolonies:barbarian";
                lootTable = "tinyfarmboss:entities/safe_minecolonies_barbarian";
                maxHealth = 20.0d;
                displayName = "Barbarian";
                break;
            case "fire_dragon":
                mobId = "iceandfire:fire_dragon";
                lootTable = "tinyfarmbossaddon:entities/safe_iceandfire_fire_dragon";
                maxHealth = 200.0d;
                displayName = "Fire Dragon";
                break;
            case "ice_dragon":
                mobId = "iceandfire:ice_dragon";
                lootTable = "tinyfarmbossaddon:entities/safe_iceandfire_ice_dragon";
                maxHealth = 200.0d;
                displayName = "Ice Dragon";
                break;
            case "lightning_dragon":
                mobId = "iceandfire:lightning_dragon";
                lootTable = "tinyfarmbossaddon:entities/safe_iceandfire_lightning_dragon";
                maxHealth = 200.0d;
                displayName = "Lightning Dragon";
                break;
            default:
                source.sendFailure(Component.literal("Boss desconocido: " + bossName));
                return;
        }

        mobData.putString("id", mobId);
        mobData.putFloat("Health", (float) maxHealth);

        capturedMobTag.putString("mobName", displayName);
        capturedMobTag.putBoolean("mobHostile", true);
        capturedMobTag.putDouble("mobMaxHealth", maxHealth);
        capturedMobTag.putString("mobId", mobId);
        capturedMobTag.putString("mobLootTableLocation", lootTable);
        capturedMobTag.put("mobData", mobData);
        capturedMobTag.putDouble("mobHealth", maxHealth);

        lassoStack.getOrCreateTag().put("capturedMob", capturedMobTag);
        lassoStack.setHoverName(Component.literal(displayName));

        if (source.getEntity() instanceof net.minecraft.world.entity.player.Player player) {
            if (!player.getInventory().add(lassoStack)) {
                ItemEntity itemEntity = player.drop(lassoStack, false);
                if (itemEntity != null) {
                    itemEntity.setNoPickUpDelay();
                    itemEntity.setThrower(player.getUUID());
                }
            }
        }
    }
}