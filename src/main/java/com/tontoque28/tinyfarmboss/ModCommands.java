package com.tontoque28.tinyfarmboss;

import com.daqem.tinymobfarm.TinyMobFarm;
import com.daqem.tinymobfarm.item.component.LassoData;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

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
                .then(Commands.literal("update_lassos")
                        .executes(ModCommands::updateLassos)
                )
        );
    }

    private static int updateLassos(CommandContext<CommandSourceStack> context) {
        CommandSourceStack source = context.getSource();
        if (!(source.getEntity() instanceof net.minecraft.world.entity.player.Player player)) {
            source.sendFailure(Component.literal("This command can only be run by a player."));
            return 0;
        }

        int updatedCount = 0;
        for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
            ItemStack stack = player.getInventory().getItem(i);
            if (updateLassoData(stack)) {
                updatedCount++;
            }
        }

        int finalCount = updatedCount;
        source.sendSuccess(() -> Component.literal(finalCount + " lassos updated successfully."), true);
        return updatedCount;
    }

    public static boolean updateLassoData(ItemStack stack) {
        if (stack.isEmpty()) return false;
        ResourceLocation itemRegistryName = BuiltInRegistries.ITEM.getKey(stack.getItem());
        if (itemRegistryName == null || !itemRegistryName.toString().equals(LASSO_NAMESPACE + ":" + LASSO_PATH)) {
            return false;
        }

        LassoData lassoData = stack.get(TinyMobFarm.LASSO_DATA.get());
        if (lassoData == null) return false;

        String mobId = lassoData.mobId().toString();
        CompoundTag mobData = lassoData.mobData();

        String newLootTable = SafeMobSimulationHandler.getSafeLootTableFromStoredData(mobId, mobData);
        String currentLootTable = lassoData.mobLootTableLocation().toString();

        if (!newLootTable.equals("minecraft:empty") && !newLootTable.equals(currentLootTable)) {
            ResourceLocation newLootTableId = ResourceLocation.tryParse(newLootTable);
            if (newLootTableId == null) return false;
            stack.set(TinyMobFarm.LASSO_DATA.get(), new LassoData(
                    lassoData.mobName(),
                    lassoData.mobId(),
                    mobData,
                    lassoData.mobHealth(),
                    lassoData.mobMaxHealth(),
                    lassoData.mobHostile(),
                    newLootTableId
            ));
            return true;
        }
        return false;
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
        Item lassoItem = BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath(LASSO_NAMESPACE, LASSO_PATH));
        if (lassoItem == null || lassoItem == net.minecraft.world.item.Items.AIR) {
            source.sendFailure(Component.literal("Error: No se encontró el item " + LASSO_NAMESPACE + ":" + LASSO_PATH));
            return;
        }

        ItemStack lassoStack = new ItemStack(lassoItem);
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
                lootTable = "tinyfarmboss:entities/fire_dragon_stage5_red_loot";
                maxHealth = 200.0d;
                displayName = "Fire Dragon (Stage 5, Red)";
                break;
            case "ice_dragon":
                mobId = "iceandfire:ice_dragon";
                lootTable = "tinyfarmboss:entities/ice_dragon_stage5_blue_loot";
                maxHealth = 200.0d;
                displayName = "Ice Dragon (Stage 5, Blue)";
                break;
            case "lightning_dragon":
                mobId = "iceandfire:lightning_dragon";
                lootTable = "tinyfarmboss:entities/lightning_dragon_stage5_amethyst_loot";
                maxHealth = 200.0d;
                displayName = "Lightning Dragon (Stage 5, Amethyst)";
                break;
            default:
                source.sendFailure(Component.literal("Boss desconocido: " + bossName));
                return;
        }

        mobData.putString("id", mobId);
        mobData.putFloat("Health", (float) maxHealth);
        if (mobId.startsWith("iceandfire:")) {
            mobData.putLong("AgeTicks", 2400000L);
            mobData.putInt("Variant", 0);
        }

        lassoStack.set(TinyMobFarm.LASSO_DATA.get(), new LassoData(
                displayName,
                ResourceLocation.parse(mobId),
                mobData,
                (float) maxHealth,
                (float) maxHealth,
                true,
                ResourceLocation.parse(lootTable)
        ));
        lassoStack.set(DataComponents.CUSTOM_NAME, Component.literal(displayName));

        if (source.getEntity() instanceof net.minecraft.world.entity.player.Player player) {
            if (!player.getInventory().add(lassoStack)) {
                ItemEntity itemEntity = player.drop(lassoStack, false);
                if (itemEntity != null) {
                    itemEntity.setNoPickUpDelay();
                    itemEntity.setThrower(player);
                }
            }
        }
    }
}
