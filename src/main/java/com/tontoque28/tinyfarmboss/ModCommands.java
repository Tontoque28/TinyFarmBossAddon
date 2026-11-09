package com.tontoque28.tinyfarmboss;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.ISuggestionProvider;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Locale;

public class ModCommands {

    private static final String LASSO_NAMESPACE = "tinymobfarm";
    private static final String LASSO_PATH = "lasso";

    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        dispatcher.register(Commands.literal("tfbaddon")
                .requires(source -> source.hasPermissionLevel(2))
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

    private static int updateLassos(CommandContext<CommandSource> context) {
        CommandSource source = context.getSource();
        if (!(source.getEntity() instanceof PlayerEntity)) {
            source.sendErrorMessage(new StringTextComponent("This command can only be run by a player."));
            return 0;
        }
        PlayerEntity player = (PlayerEntity) source.getEntity();

        int updatedCount = 0;
        for (int i = 0; i < player.inventory.getSizeInventory(); i++) {
            ItemStack stack = player.inventory.getStackInSlot(i);
            if (updateLassoNBT(player, stack)) {
                updatedCount++;
            }
        }

        source.sendFeedback(new StringTextComponent(updatedCount + " lassos updated successfully."), true);
        return updatedCount;
    }

    public static boolean updateLassoNBT(PlayerEntity player, ItemStack stack) {
        if (stack.isEmpty() || !ForgeRegistries.ITEMS.getKey(stack.getItem()).toString().equals(LASSO_NAMESPACE + ":" + LASSO_PATH)) {
            return false;
        }

        CompoundNBT tag = stack.getOrCreateTag();
        if (!tag.contains("capturedMob")) return false;

        CompoundNBT capturedMobTag = tag.getCompound("capturedMob");
        CompoundNBT mobData = capturedMobTag.contains("mobData") ? capturedMobTag.getCompound("mobData") : null;

        String mobId = capturedMobTag.getString("mobId");
        if (mobId.isEmpty() && mobData != null) {
            mobId = mobData.getString("id");
        }
        if (mobId.isEmpty()) return false;

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

    private static final SuggestionProvider<CommandSource> SUGGEST_BOSSES = (context, builder) ->
            ISuggestionProvider.suggest(new String[]{"wither", "ender_dragon"}, builder);

    private static int giveAllTestLassos(CommandContext<CommandSource> context) {
        CommandSource source = context.getSource();

        if (!(source.getEntity() instanceof PlayerEntity)) {
            source.sendErrorMessage(new StringTextComponent("This command can only be run by a player."));
            return 0;
        }
        PlayerEntity player = (PlayerEntity) source.getEntity();

        Item lassoItem = ForgeRegistries.ITEMS.getValue(new ResourceLocation(LASSO_NAMESPACE, LASSO_PATH));
        if (lassoItem == null) {
            source.sendErrorMessage(new StringTextComponent("Lasso item not found."));
            return 0;
        }

        if (!ModList.get().isLoaded("iceandfire")) {
            source.sendErrorMessage(new StringTextComponent("Ice and Fire mod is not loaded."));
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
            String lootTable = "tinyfarmboss:entities/" + dragonType + "_dragon_stage" + stage + "_" + color + "_loot";
            long ageTicks = (stage == 4) ? 1800000L : 2400000L;
            String displayName = "Test Female " + name + " Dragon (Stage " + stage + ")";

            ItemStack lassoStack = new ItemStack(lassoItem);
            CompoundNBT capturedMobTag = new CompoundNBT();
            CompoundNBT mobData = new CompoundNBT();

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
            lassoStack.setDisplayName(new StringTextComponent(displayName));

            if (!player.inventory.addItemStackToInventory(lassoStack)) {
                player.dropItem(lassoStack, false);
            }
        }

        source.sendFeedback(new StringTextComponent("Test dragons created successfully."), true);
        return 1;
    }

    private static int giveLasso(CommandContext<CommandSource> context) {
        CommandSource source = context.getSource();
        if (!(source.getEntity() instanceof PlayerEntity)) {
            source.sendErrorMessage(new StringTextComponent("This command can only be run by a player."));
            return 0;
        }
        PlayerEntity player = (PlayerEntity) source.getEntity();

        String boss = StringArgumentType.getString(context, "boss").toLowerCase(Locale.ROOT);
        String mobId;
        String lootTable;
        ITextComponent displayName;
        double maxHealth;
        double health;

        switch (boss) {
            case "wither":
                mobId = "minecraft:wither";
                lootTable = "tinyfarmboss:entities/wither_custom";
                displayName = new TranslationTextComponent("entity.minecraft.wither");
                maxHealth = 300.0D;
                health = 300.0D;
                break;
            case "ender_dragon":
                mobId = "minecraft:ender_dragon";
                lootTable = "tinyfarmboss:entities/ender_dragon_custom";
                displayName = new TranslationTextComponent("entity.minecraft.ender_dragon");
                maxHealth = 200.0D;
                health = 200.0D;
                break;
            default:
                source.sendErrorMessage(new StringTextComponent("Unknown boss: " + boss));
                return 0;
        }

        Item lassoItem = ForgeRegistries.ITEMS.getValue(new ResourceLocation(LASSO_NAMESPACE, LASSO_PATH));
        if (lassoItem == null) {
            source.sendErrorMessage(new StringTextComponent("Lasso item not found."));
            return 0;
        }

        ItemStack lassoStack = new ItemStack(lassoItem);
        CompoundNBT mobData = new CompoundNBT();
        mobData.putString("id", mobId);

        CompoundNBT capturedMobTag = new CompoundNBT();
        capturedMobTag.putString("mobName", displayName.getString());
        capturedMobTag.putBoolean("mobHostile", true);
        capturedMobTag.putDouble("mobMaxHealth", maxHealth);
        capturedMobTag.putString("mobId", mobId);
        capturedMobTag.putString("mobLootTableLocation", lootTable);
        capturedMobTag.put("mobData", mobData);
        capturedMobTag.putDouble("mobHealth", health);

        lassoStack.getOrCreateTag().put("capturedMob", capturedMobTag);
        lassoStack.setDisplayName(displayName);

        if (!player.inventory.addItemStackToInventory(lassoStack)) {
            player.dropItem(lassoStack, false);
        }

        source.sendFeedback(new StringTextComponent("Successfully gave lasso for " + displayName.getString()), true);
        return 1;
    }
}
