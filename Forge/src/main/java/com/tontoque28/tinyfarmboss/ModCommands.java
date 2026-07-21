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
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Locale;
import java.util.concurrent.atomic.AtomicInteger;

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
                .then(Commands.literal("update_lassos")
                        .executes(ModCommands::updateLassos)
                )
        );
    }

    private static int updateLassos(CommandContext<CommandSourceStack> context) {
        CommandSourceStack source = context.getSource();
        if (!(source.getEntity() instanceof Player player)) {
            source.sendFailure(Component.literal("This command can only be run by a player."));
            return 0;
        }

        AtomicInteger updatedCount = new AtomicInteger(0);
        for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
            ItemStack stack = player.getInventory().getItem(i);
            if (updateLassoNBT(player, stack)) {
                updatedCount.incrementAndGet();
            }
        }

        source.sendSuccess(() -> Component.literal(updatedCount.get() + " lassos updated successfully."), true);
        return updatedCount.get();
    }

    public static boolean updateLassoNBT(Player player, ItemStack stack) {
        if (stack.isEmpty() || !ForgeRegistries.ITEMS.getKey(stack.getItem()).toString().equals(LASSO_NAMESPACE + ":" + LASSO_PATH)) {
            return false;
        }

        CompoundTag tag = stack.getOrCreateTag();
        if (!tag.contains("capturedMob")) return false;

        CompoundTag capturedMobTag = tag.getCompound("capturedMob");
        String mobId = capturedMobTag.getString("mobId");
        if (mobId.isEmpty()) return false;

        CompoundTag mobData = capturedMobTag.contains("mobData") ? capturedMobTag.getCompound("mobData") : null;

        // Recalculamos siempre a partir de los datos guardados (mobId + AgeTicks/Variant reales),
        // en vez de intentar adivinar si el valor guardado está "desactualizado" comparando
        // prefijos de string: eso fallaba en cuanto la ruta ya tenía el namespace correcto
        // pero apuntaba a una versión vieja/genérica (sin color) de la tabla.
        String newLootTable = SafeMobSimulationHandler.getSafeLootTableFromStoredData(mobId, mobData);
        String currentLootTable = capturedMobTag.contains("mobLootTableLocation")
                ? capturedMobTag.getString("mobLootTableLocation")
                : "";

        if (!newLootTable.equals("minecraft:empty") && !newLootTable.equals(currentLootTable)) {
            capturedMobTag.putString("mobLootTableLocation", newLootTable);
            tag.put("capturedMob", capturedMobTag);
            stack.setTag(tag);
            return true;
        }
        return false;
    }

    private static final SuggestionProvider<CommandSourceStack> SUGGEST_BOSSES = (context, builder) ->
            SharedSuggestionProvider.suggest(new String[]{"wither", "ender_dragon", "warden"}, builder);

    private static int giveAllTestLassos(CommandContext<CommandSourceStack> context) {
        CommandSourceStack source = context.getSource();

        if (!(source.getEntity() instanceof Player player)) {
            source.sendFailure(Component.literal("This command can only be run by a player."));
            return 0;
        }

        Item lassoItem = ForgeRegistries.ITEMS.getValue(ResourceLocation.fromNamespaceAndPath(LASSO_NAMESPACE, LASSO_PATH));
        if (lassoItem == null) {
            source.sendFailure(Component.literal("Lasso item not found."));
            return 0;
        }

        if (!ModList.get().isLoaded("iceandfire")) {
            source.sendFailure(Component.literal("Ice and Fire mod is not loaded."));
            return 0;
        }

        Object[][] dragons = {
                {"iceandfire:fire_dragon", "Red", 0, 4}, {"iceandfire:fire_dragon", "Green", 1, 4}, {"iceandfire:fire_dragon", "Bronze", 2, 4}, {"iceandfire:fire_dragon", "Gray", 3, 4},
                {"iceandfire:fire_dragon", "Red", 0, 5}, {"iceandfire:fire_dragon", "Green", 1, 5}, {"iceandfire:fire_dragon", "Bronze", 2, 5}, {"iceandfire:fire_dragon", "Gray", 3, 5},
                {"iceandfire:ice_dragon", "Blue", 0, 4}, {"iceandfire:ice_dragon", "White", 1, 4}, {"iceandfire:ice_dragon", "Sapphire", 2, 4}, {"iceandfire:ice_dragon", "Silver", 3, 4},
                {"iceandfire:ice_dragon", "Blue", 0, 5}, {"iceandfire:ice_dragon", "White", 1, 5}, {"iceandfire:ice_dragon", "Sapphire", 2, 5}, {"iceandfire:ice_dragon", "Silver", 3, 5},
                {"iceandfire:lightning_dragon", "Amethyst", 0, 4}, {"iceandfire:lightning_dragon", "Black", 1, 4}, {"iceandfire:lightning_dragon", "Copper", 2, 4}, {"iceandfire:lightning_dragon", "Electric", 3, 4},
                {"iceandfire:lightning_dragon", "Amethyst", 0, 5}, {"iceandfire:lightning_dragon", "Black", 1, 5}, {"iceandfire:lightning_dragon", "Copper", 2, 5}, {"iceandfire:lightning_dragon", "Electric", 3, 5}
        };

        for (Object[] data : dragons) {
            String mobId = (String) data[0];
            String name = (String) data[1];
            int variant = (int) data[2];
            int stage = (int) data[3];

            String dragonType = mobId.split(":")[1].split("_")[0];
            String color = name.toLowerCase(Locale.ROOT);
            // CORRECCIÓN AQUÍ: Apuntando a tinyfarmboss:entities/
            String lootTable = "tinyfarmboss:entities/" + dragonType + "_dragon_stage" + stage + "_" + color + "_loot";
            long ageTicks = (stage == 4) ? 1800000L : 2400000L;
            String displayName = "Test Female " + name + " Dragon (Stage " + stage + ")";

            ItemStack lassoStack = new ItemStack(lassoItem);
            CompoundTag capturedMobTag = new CompoundTag();
            CompoundTag mobData = new CompoundTag();

            mobData.putString("id", mobId);
            mobData.putLong("AgeTicks", ageTicks);
            mobData.putByte("Gender", (byte) 0);
            mobData.putInt("Variant", variant);

            capturedMobTag.putString("mobName", displayName);
            capturedMobTag.putBoolean("mobHostile", true);
            capturedMobTag.putString("mobId", mobId);
            capturedMobTag.putString("mobLootTableLocation", lootTable);
            capturedMobTag.put("mobData", mobData);

            lassoStack.getOrCreateTag().put("capturedMob", capturedMobTag);
            lassoStack.setHoverName(Component.literal(displayName));

            if (!player.getInventory().add(lassoStack)) {
                player.drop(lassoStack, false);
            }
        }

        source.sendSuccess(() -> Component.literal("Test dragons created successfully."), true);
        return 1;
    }

    private static int giveLasso(CommandContext<CommandSourceStack> context) {
        CommandSourceStack source = context.getSource();
        if (!(source.getEntity() instanceof Player player)) {
            source.sendFailure(Component.literal("This command can only be run by a player."));
            return 0;
        }

        String boss = StringArgumentType.getString(context, "boss").toLowerCase(Locale.ROOT);
        String mobId;
        String lootTable;
        Component displayName;
        double maxHealth;
        double health;

        switch (boss) {
            case "wither" -> {
                mobId = "minecraft:wither";
                // CORRECCIÓN AQUÍ
                lootTable = "tinyfarmboss:entities/wither_custom";
                displayName = Component.translatable("entity.minecraft.wither");
                maxHealth = 300.0D;
                health = 300.0D;
            }
            case "ender_dragon" -> {
                mobId = "minecraft:ender_dragon";
                // CORRECCIÓN AQUÍ
                lootTable = "tinyfarmboss:entities/ender_dragon_custom";
                displayName = Component.translatable("entity.minecraft.ender_dragon");
                maxHealth = 200.0D;
                health = 200.0D;
            }
            case "warden" -> {
                mobId = "minecraft:warden";
                // CORRECCIÓN AQUÍ
                lootTable = "tinyfarmboss:entities/warden_custom";
                displayName = Component.translatable("entity.minecraft.warden");
                maxHealth = 500.0D;
                health = 500.0D;
            }
            default -> {
                source.sendFailure(Component.literal("Unknown boss: " + boss));
                return 0;
            }
        }

        Item lassoItem = ForgeRegistries.ITEMS.getValue(ResourceLocation.fromNamespaceAndPath(LASSO_NAMESPACE, LASSO_PATH));
        if (lassoItem == null) {
            source.sendFailure(Component.literal("Lasso item not found."));
            return 0;
        }

        ItemStack lassoStack = new ItemStack(lassoItem);
        CompoundTag mobData = new CompoundTag();
        mobData.putString("id", mobId);

        CompoundTag capturedMobTag = new CompoundTag();
        capturedMobTag.putString("mobName", displayName.getString());
        capturedMobTag.putBoolean("mobHostile", true);
        capturedMobTag.putDouble("mobMaxHealth", maxHealth);
        capturedMobTag.putString("mobId", mobId);
        capturedMobTag.putString("mobLootTableLocation", lootTable);
        capturedMobTag.put("mobData", mobData);
        capturedMobTag.putDouble("mobHealth", health);

        lassoStack.getOrCreateTag().put("capturedMob", capturedMobTag);
        lassoStack.setHoverName(displayName);

        if (!player.getInventory().add(lassoStack)) {
            player.drop(lassoStack, false);
        }

        source.sendSuccess(() -> Component.literal("Successfully gave lasso for " + displayName.getString()), true);
        return 1;
    }
}