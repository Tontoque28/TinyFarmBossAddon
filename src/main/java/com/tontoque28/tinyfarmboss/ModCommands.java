package com.tontoque28.tinyfarmboss;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.fml.common.Loader;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class ModCommands extends CommandBase {

    private static final String LASSO_NAMESPACE = "tinymobfarm";
    private static final String LASSO_PATH = "lasso";

    @Override
    public String getName() {
        return "tfbaddon";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "/tfbaddon <give <wither|ender_dragon>|test|update_lassos>";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if (args.length == 0) {
            throw new WrongUsageException(getUsage(sender));
        }
        String sub = args[0];
        if (sub.equals("give") && args.length == 2) {
            giveLasso(sender, args[1]);
        } else if (sub.equals("test") && args.length == 1) {
            giveAllTestLassos(sender);
        } else if (sub.equals("update_lassos") && args.length == 1) {
            updateLassos(sender);
        } else {
            throw new WrongUsageException(getUsage(sender));
        }
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
        if (args.length == 1) {
            return getListOfStringsMatchingLastWord(args, "give", "test", "update_lassos");
        }
        if (args.length == 2 && args[0].equals("give")) {
            return getListOfStringsMatchingLastWord(args, "wither", "ender_dragon");
        }
        return Collections.emptyList();
    }

    private static void error(ICommandSender sender, String message) {
        TextComponentString text = new TextComponentString(message);
        text.getStyle().setColor(TextFormatting.RED);
        sender.sendMessage(text);
    }

    private static void feedback(ICommandSender sender, String message) {
        sender.sendMessage(new TextComponentString(message));
    }

    private static EntityPlayer playerOf(ICommandSender sender) {
        if (sender.getCommandSenderEntity() instanceof EntityPlayer) {
            return (EntityPlayer) sender.getCommandSenderEntity();
        }
        error(sender, "This command can only be run by a player.");
        return null;
    }

    private static void updateLassos(ICommandSender sender) {
        EntityPlayer player = playerOf(sender);
        if (player == null) return;

        int updatedCount = 0;
        for (int i = 0; i < player.inventory.getSizeInventory(); i++) {
            ItemStack stack = player.inventory.getStackInSlot(i);
            if (updateLassoNBT(player, stack)) {
                updatedCount++;
            }
        }

        feedback(sender, updatedCount + " lassos updated successfully.");
    }

    public static boolean updateLassoNBT(EntityPlayer player, ItemStack stack) {
        if (stack.isEmpty()) return false;
        ResourceLocation key = Item.REGISTRY.getNameForObject(stack.getItem());
        if (key == null || !key.toString().equals(LASSO_NAMESPACE + ":" + LASSO_PATH)) {
            return false;
        }

        NBTTagCompound tag = stack.getTagCompound();
        if (tag == null || !tag.hasKey("capturedMob")) return false;

        NBTTagCompound capturedMobTag = tag.getCompoundTag("capturedMob");
        NBTTagCompound mobData = capturedMobTag.hasKey("mobData") ? capturedMobTag.getCompoundTag("mobData") : null;

        String mobId = capturedMobTag.getString("mobId");
        if (mobId.isEmpty() && mobData != null) {
            mobId = mobData.getString("id");
        }
        if (mobId.isEmpty()) return false;

        String newLootTable = SafeMobSimulationHandler.getSafeLootTableFromStoredData(mobId, mobData);
        String currentLootTable = capturedMobTag.hasKey("mobLootTableLocation")
                ? capturedMobTag.getString("mobLootTableLocation")
                : "";

        if (!newLootTable.equals("minecraft:empty") && !newLootTable.equals(currentLootTable)) {
            capturedMobTag.setString("mobLootTableLocation", newLootTable);
            tag.setTag("capturedMob", capturedMobTag);
            stack.setTagCompound(tag);
            return true;
        }
        return false;
    }

    private static void giveStack(EntityPlayer player, ItemStack stack) {
        if (!player.inventory.addItemStackToInventory(stack)) {
            player.dropItem(stack, false);
        }
    }

    private static String translate(String key, String fallback) {
        String value = I18n.translateToLocal(key);
        return value == null || value.equals(key) ? fallback : value;
    }

    private static void giveAllTestLassos(ICommandSender sender) {
        EntityPlayer player = playerOf(sender);
        if (player == null) return;

        Item lassoItem = Item.REGISTRY.getObject(new ResourceLocation(LASSO_NAMESPACE, LASSO_PATH));
        if (lassoItem == null) {
            error(sender, "Lasso item not found.");
            return;
        }

        if (!Loader.isModLoaded("iceandfire")) {
            error(sender, "Ice and Fire mod is not loaded.");
            return;
        }

        Object[][] dragons = {
                {"fire", "Red", 0, 4}, {"fire", "Green", 1, 4}, {"fire", "Bronze", 2, 4}, {"fire", "Gray", 3, 4},
                {"fire", "Red", 0, 5}, {"fire", "Green", 1, 5}, {"fire", "Bronze", 2, 5}, {"fire", "Gray", 3, 5},
                {"ice", "Blue", 0, 4}, {"ice", "White", 1, 4}, {"ice", "Sapphire", 2, 4}, {"ice", "Silver", 3, 4},
                {"ice", "Blue", 0, 5}, {"ice", "White", 1, 5}, {"ice", "Sapphire", 2, 5}, {"ice", "Silver", 3, 5}
        };

        for (Object[] data : dragons) {
            String dragonType = (String) data[0];
            String mobId = "iceandfire:" + dragonType + "dragon";
            String name = (String) data[1];
            int variant = (int) data[2];
            int stage = (int) data[3];

            String color = name.toLowerCase(Locale.ROOT);
            String lootTable = "tinyfarmboss:entities/" + dragonType + "_dragon_stage" + stage + "_" + color + "_loot";
            long ageTicks = (stage == 4) ? 1800000L : 2400000L;
            String displayName = "Test Female " + name + " Dragon (Stage " + stage + ")";

            ItemStack lassoStack = new ItemStack(lassoItem);
            NBTTagCompound capturedMobTag = new NBTTagCompound();
            NBTTagCompound mobData = new NBTTagCompound();

            mobData.setString("id", mobId);
            mobData.setLong("AgeTicks", ageTicks);
            mobData.setByte("Gender", (byte) 0);
            mobData.setInteger("Variant", variant);

            capturedMobTag.setString("mobName", displayName);
            capturedMobTag.setBoolean("mobHostile", true);
            capturedMobTag.setString("mobId", mobId);
            capturedMobTag.setString("mobLootTableLocation", lootTable);
            capturedMobTag.setTag("mobData", mobData);

            NBTTagCompound stackTag = new NBTTagCompound();
            stackTag.setTag("capturedMob", capturedMobTag);
            lassoStack.setTagCompound(stackTag);
            lassoStack.setStackDisplayName(displayName);

            giveStack(player, lassoStack);
        }

        feedback(sender, "Test dragons created successfully.");
    }

    private static void giveLasso(ICommandSender sender, String bossArg) {
        EntityPlayer player = playerOf(sender);
        if (player == null) return;

        String boss = bossArg.toLowerCase(Locale.ROOT);
        String mobId;
        String lootTable;
        String displayName;
        double maxHealth;
        double health;

        switch (boss) {
            case "wither":
                mobId = "minecraft:wither";
                lootTable = "tinyfarmboss:entities/wither_custom";
                displayName = translate("entity.WitherBoss.name", "Wither");
                maxHealth = 300.0D;
                health = 300.0D;
                break;
            case "ender_dragon":
                mobId = "minecraft:ender_dragon";
                lootTable = "tinyfarmboss:entities/ender_dragon_custom";
                displayName = translate("entity.EnderDragon.name", "Ender Dragon");
                maxHealth = 200.0D;
                health = 200.0D;
                break;
            default:
                error(sender, "Unknown boss: " + boss);
                return;
        }

        Item lassoItem = Item.REGISTRY.getObject(new ResourceLocation(LASSO_NAMESPACE, LASSO_PATH));
        if (lassoItem == null) {
            error(sender, "Lasso item not found.");
            return;
        }

        ItemStack lassoStack = new ItemStack(lassoItem);
        NBTTagCompound mobData = new NBTTagCompound();
        mobData.setString("id", mobId);

        NBTTagCompound capturedMobTag = new NBTTagCompound();
        capturedMobTag.setString("mobName", displayName);
        capturedMobTag.setBoolean("mobHostile", true);
        capturedMobTag.setDouble("mobMaxHealth", maxHealth);
        capturedMobTag.setString("mobId", mobId);
        capturedMobTag.setString("mobLootTableLocation", lootTable);
        capturedMobTag.setTag("mobData", mobData);
        capturedMobTag.setDouble("mobHealth", health);

        NBTTagCompound stackTag = new NBTTagCompound();
        stackTag.setTag("capturedMob", capturedMobTag);
        lassoStack.setTagCompound(stackTag);
        lassoStack.setStackDisplayName(displayName);

        giveStack(player, lassoStack);

        feedback(sender, "Successfully gave lasso for " + displayName);
    }
}
