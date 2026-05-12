# FULL_CODE_REFERENCES.md
# Referencias Completas de Código para T28's Universal Veinminer

Este archivo contiene los fragmentos de código de la versión final (1.0.0).

---

## 1. `VeinminerConfig.java`
```java
package com.tontoque28.universalveinminer.config;

import net.minecraftforge.common.ForgeConfigSpec;
import java.util.ArrayList;
import java.util.List;

public class VeinminerConfig {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;
    public static final ForgeConfigSpec.IntValue MAX_BLOCKS_MINED;
    public static final ForgeConfigSpec.BooleanValue VEINMINER_ENABLED;
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> CUSTOM_VEINMINABLE_BLOCKS;

    static {
        BUILDER.push("General");
        VEINMINER_ENABLED = BUILDER.comment("Enable or disable the Veinminer functionality.").define("veinminerEnabled", true);
        MAX_BLOCKS_MINED = BUILDER.comment("Maximum number of blocks that can be mined in a single Veinminer action.").defineInRange("maxBlocksMined", 64, 1, 1024);
        CUSTOM_VEINMINABLE_BLOCKS = BUILDER.comment("List of custom block IDs (e.g., 'minecraft:stone', 'modid:custom_ore') that can be veinmined.").defineListAllowEmpty("customVeinminableBlocks", () -> new ArrayList<String>(), obj -> obj instanceof String);
        BUILDER.pop();
        SPEC = BUILDER.build();
    }
}
```

## 2. `VeinminerEvents.java`
```java
package com.tontoque28.universalveinminer.event;

import com.tontoque28.universalveinminer.UniversalVeinminer;
import com.tontoque28.universalveinminer.config.VeinminerConfig;
import com.tontoque28.universalveinminer.logic.VeinminerLogic;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.common.Tags;
import java.util.List;

@Mod.EventBusSubscriber(modid = UniversalVeinminer.MOD_ID)
public class VeinminerEvents {
    @SubscribeEvent
    public static void onBlockBreak(BlockEvent.BreakEvent event) {
        if (event.isCanceled()) return;
        if (!VeinminerConfig.VEINMINER_ENABLED.get()) return;

        Player player = event.getPlayer();
        Level level = (Level) event.getLevel();
        BlockPos originalPos = event.getPos();
        BlockState originalState = level.getBlockState(originalPos);
        ItemStack heldItem = player.getMainHandItem();

        if (!player.isCrouching()) return;
        if (!heldItem.isCorrectToolForDrops(originalState)) return;

        boolean isOre = originalState.is(Tags.Blocks.ORES);
        boolean isLog = originalState.is(BlockTags.LOGS);
        List<? extends String> customVeinminableBlocks = VeinminerConfig.CUSTOM_VEINMINABLE_BLOCKS.get();
        String blockId = BuiltInRegistries.BLOCK.getKey(originalState.getBlock()).toString();
        boolean isCustomVeinminable = customVeinminableBlocks.contains(blockId);

        if (!isOre && !isLog && !isCustomVeinminable) return;

        VeinminerLogic.mineVein(level, player, originalPos, originalState, heldItem, VeinminerConfig.MAX_BLOCKS_MINED.get());
        event.setCanceled(true);
    }
}
```

## 3. `VeinminerLogic.java`
```java
package com.tontoque28.universalveinminer.logic;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraftforge.common.ForgeHooks;
import java.util.*;

public class VeinminerLogic {
    private static final Direction[] DIRECTIONS = Direction.values();

    public static void mineVein(Level level, Player player, BlockPos originalPos, BlockState originalState, ItemStack tool, int maxBlocks) {
        Queue<BlockPos> queue = new LinkedList<>();
        Set<BlockPos> visited = new HashSet<>();
        List<BlockPos> blocksToMine = new ArrayList<>();

        queue.offer(originalPos);
        visited.add(originalPos);

        int blocksMinedCount = 0;

        while (!queue.isEmpty() && blocksMinedCount < maxBlocks) {
            BlockPos currentPos = queue.poll();
            BlockState currentState = level.getBlockState(currentPos);

            if (currentState.is(originalState.getBlock()) && ForgeHooks.isCorrectToolForDrops(currentState, player)) {
                blocksToMine.add(currentPos);
                blocksMinedCount++;

                if (tool.getDamageValue() >= tool.getMaxDamage() - 1) break;

                for (Direction direction : DIRECTIONS) {
                    BlockPos neighborPos = currentPos.relative(direction);
                    if (!visited.contains(neighborPos) && level.isLoaded(neighborPos)) {
                        BlockState neighborState = level.getBlockState(neighborPos);
                        if (neighborState.is(originalState.getBlock()) && ForgeHooks.isCorrectToolForDrops(neighborState, player)) {
                            queue.offer(neighborPos);
                            visited.add(neighborPos);
                        }
                    }
                }
            }
        }

        for (BlockPos pos : blocksToMine) {
            BlockState state = level.getBlockState(pos);
            Block block = state.getBlock();

            if (!level.isEmptyBlock(pos) && ForgeHooks.isCorrectToolForDrops(state, player)) {
                if (level instanceof ServerLevel serverLevel) {
                    List<ItemStack> drops = Block.getDrops(state, serverLevel, pos, serverLevel.getBlockEntity(pos), player, tool);
                    for (ItemStack drop : drops) {
                        Block.popResource(serverLevel, pos, drop);
                    }

                    int fortuneLevel = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.BLOCK_FORTUNE, tool);
                    int silkTouchLevel = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.SILK_TOUCH, tool);
                    int exp = state.getExpDrop(serverLevel, serverLevel.random, pos, fortuneLevel, silkTouchLevel);
                    if (exp > 0) block.popExperience(serverLevel, pos, exp);

                    serverLevel.setBlockAndUpdate(pos, net.minecraft.world.level.block.Blocks.AIR.defaultBlockState());
                    serverLevel.gameEvent(player, GameEvent.BLOCK_DESTROY, pos);
                } else {
                    level.setBlockAndUpdate(pos, net.minecraft.world.level.block.Blocks.AIR.defaultBlockState());
                    level.gameEvent(player, GameEvent.BLOCK_DESTROY, pos);
                }

                tool.hurtAndBreak(1, player, (p) -> p.broadcastBreakEvent(player.getUsedItemHand()));
                player.causeFoodExhaustion(0.005F);
            }
        }
    }
}
```

## 4. `ClientEvents.java`
```java
package com.tontoque28.universalveinminer.client;

import com.tontoque28.universalveinminer.UniversalVeinminer;
import com.tontoque28.universalveinminer.config.VeinminerConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

public class ClientEvents {
    @Mod.EventBusSubscriber(modid = UniversalVeinminer.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onKeyRegister(RegisterKeyMappingsEvent event) {
            event.register(KeyBindings.TOGGLE_VEINMINER);
        }
    }

    @Mod.EventBusSubscriber(modid = UniversalVeinminer.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
    public static class ClientForgeEvents {
        @SubscribeEvent
        public static void onClientTick(TickEvent.ClientTickEvent event) {
            if (event.phase == TickEvent.Phase.END) {
                while (KeyBindings.TOGGLE_VEINMINER.consumeClick()) {
                    boolean newState = !VeinminerConfig.VEINMINER_ENABLED.get();
                    VeinminerConfig.VEINMINER_ENABLED.set(newState);
                    VeinminerConfig.SPEC.save();

                    if (Minecraft.getInstance().player != null) {
                        Component stateComponent = newState ? 
                                Component.translatable("message." + UniversalVeinminer.MOD_ID + ".enabled") : 
                                Component.translatable("message." + UniversalVeinminer.MOD_ID + ".disabled");
                                
                        Minecraft.getInstance().player.displayClientMessage(
                                Component.translatable("message." + UniversalVeinminer.MOD_ID + ".veinminer_toggled", stateComponent), 
                                true
                        );
                    }
                }
            }
        }
    }
}
```

## 5. `KeyBindings.java`
```java
package com.tontoque28.universalveinminer.client;

import com.mojang.blaze3d.platform.InputConstants;
import com.tontoque28.universalveinminer.UniversalVeinminer;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.client.settings.KeyModifier;
import org.lwjgl.glfw.GLFW;

public class KeyBindings {
    public static final String KEY_CATEGORY_VEINMINER = "key.category." + UniversalVeinminer.MOD_ID;
    public static final KeyMapping TOGGLE_VEINMINER = new KeyMapping(
            "key." + UniversalVeinminer.MOD_ID + ".toggle_veinminer",
            KeyConflictContext.IN_GAME,
            KeyModifier.NONE,
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_V,
            KEY_CATEGORY_VEINMINER
    );
}
```

## 6. `ModCommands.java`
```java
package com.tontoque28.universalveinminer.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.tontoque28.universalveinminer.UniversalVeinminer;
import com.tontoque28.universalveinminer.config.VeinminerConfig;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.ResourceLocationArgument;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

public class ModCommands {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal(UniversalVeinminer.MOD_ID)
                .requires(cs -> cs.hasPermission(2))
                .then(Commands.literal("addblock")
                        .then(Commands.argument("block_id", ResourceLocationArgument.id())
                                .suggests((context, builder) -> SharedSuggestionProvider.suggestResource(BuiltInRegistries.BLOCK.keySet(), builder))
                                .executes(ModCommands::addVeinminableBlock)
                        )
                )
                .then(Commands.literal("removeblock")
                        .then(Commands.argument("block_id", ResourceLocationArgument.id())
                                .suggests((context, builder) -> SharedSuggestionProvider.suggest(VeinminerConfig.CUSTOM_VEINMINABLE_BLOCKS.get().stream().map(String::valueOf), builder))
                                .executes(ModCommands::removeVeinminableBlock)
                        )
                )
                .then(Commands.literal("listblocks")
                        .executes(ModCommands::listVeinminableBlocks)
                )
        );
    }

    private static int addVeinminableBlock(CommandContext<CommandSourceStack> context) {
        ResourceLocation blockRL = ResourceLocationArgument.getId(context, "block_id");
        String blockIdString = blockRL.toString();

        if (!BuiltInRegistries.BLOCK.containsKey(blockRL)) {
            context.getSource().sendFailure(Component.translatable("command." + UniversalVeinminer.MOD_ID + ".addblock.not_found", blockIdString));
            return 0;
        }

        List<String> customBlocks = new ArrayList<>(VeinminerConfig.CUSTOM_VEINMINABLE_BLOCKS.get());
        if (customBlocks.contains(blockIdString)) {
            context.getSource().sendFailure(Component.translatable("command." + UniversalVeinminer.MOD_ID + ".addblock.already_added", blockIdString));
            return 0;
        }

        customBlocks.add(blockIdString);
        VeinminerConfig.CUSTOM_VEINMINABLE_BLOCKS.set(customBlocks);
        VeinminerConfig.SPEC.save();

        context.getSource().sendSuccess(() -> Component.translatable("command." + UniversalVeinminer.MOD_ID + ".addblock.success", blockIdString), true);
        return 1;
    }

    private static int removeVeinminableBlock(CommandContext<CommandSourceStack> context) {
        ResourceLocation blockRL = ResourceLocationArgument.getId(context, "block_id");
        String blockIdString = blockRL.toString();

        List<String> customBlocks = new ArrayList<>(VeinminerConfig.CUSTOM_VEINMINABLE_BLOCKS.get());
        if (!customBlocks.contains(blockIdString)) {
            context.getSource().sendFailure(Component.translatable("command." + UniversalVeinminer.MOD_ID + ".removeblock.not_found_in_list", blockIdString));
            return 0;
        }

        customBlocks.remove(blockIdString);
        VeinminerConfig.CUSTOM_VEINMINABLE_BLOCKS.set(customBlocks);
        VeinminerConfig.SPEC.save();

        context.getSource().sendSuccess(() -> Component.translatable("command." + UniversalVeinminer.MOD_ID + ".removeblock.success", blockIdString), true);
        return 1;
    }

    private static int listVeinminableBlocks(CommandContext<CommandSourceStack> context) {
        List<? extends String> customBlocks = VeinminerConfig.CUSTOM_VEINMINABLE_BLOCKS.get();
        if (customBlocks.isEmpty()) {
            context.getSource().sendSuccess(() -> Component.translatable("command." + UniversalVeinminer.MOD_ID + ".listblocks.empty"), false);
        } else {
            context.getSource().sendSuccess(() -> Component.translatable("command." + UniversalVeinminer.MOD_ID + ".listblocks.header"), false);
            for (String blockId : customBlocks) {
                context.getSource().sendSuccess(() -> Component.literal("- " + blockId), false);
            }
        }
        return 1;
    }
}
