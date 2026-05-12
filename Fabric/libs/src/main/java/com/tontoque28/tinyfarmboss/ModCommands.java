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
        dispatcher.register(Commands.literal("tinyfarmboss")
            .requires(source -> source.hasPermission(2))
            .then(Commands.literal("give")
                .then(Commands.argument("boss", StringArgumentType.word())
                    .suggests(SUGGEST_BOSSES)
                    .executes(ModCommands::giveLasso)
                )
            )
        );
    }

    private static final SuggestionProvider<CommandSourceStack> SUGGEST_BOSSES = (context, builder) ->
            SharedSuggestionProvider.suggest(new String[]{"wither", "ender_dragon", "warden"}, builder);

    private static int giveLasso(CommandContext<CommandSourceStack> context) {
        String bossName = StringArgumentType.getString(context, "boss");
        CommandSourceStack source = context.getSource();
        
        Item lassoItem = ForgeRegistries.ITEMS.getValue(new ResourceLocation(LASSO_NAMESPACE, LASSO_PATH));
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
                lootTable = "tinyfarmboss:entities/wither_custom";
                maxHealth = 300.0d;
                displayName = "Wither";
                break;
            case "ender_dragon":
                mobId = "minecraft:ender_dragon";
                lootTable = "tinyfarmboss:entities/ender_dragon_custom";
                maxHealth = 200.0d;
                displayName = "Ender Dragon";
                break;
            case "warden":
                mobId = "minecraft:warden";
                lootTable = "tinyfarmboss:entities/warden_custom";
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