package com.tontoque28.tinyfarmboss.events;

import com.tontoque28.tinyfarmboss.ModCommands;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = "tinyfarmbossaddon")
public class PlayerEvents {

    private static final String UPDATE_FLAG = "tinyfarmbossaddon_updated_v1.2.2"; // Incremented version

    @SubscribeEvent
    public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        Player player = event.getEntity();
        CompoundTag playerData = player.getPersistentData();

        if (!playerData.getBoolean(UPDATE_FLAG)) {
            for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
                ItemStack stack = player.getInventory().getItem(i);
                ModCommands.updateLassoNBT(player, stack);
            }
            // Also check armor and offhand
            for (ItemStack stack : player.getInventory().armor) {
                ModCommands.updateLassoNBT(player, stack);
            }
            ModCommands.updateLassoNBT(player, player.getInventory().offhand.get(0));

            playerData.putBoolean(UPDATE_FLAG, true);
        }
    }
}
