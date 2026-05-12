# FULL_CODE_REFERENCES.md
# Referencias Completas de Código para Better Survival: World Awakening

Este archivo contiene snippets de código completos y funcionales, sirviendo como biblioteca de referencia.

---

## [Fecha Actual] - Zombie Variant System (No-Entity Approach)

### ZombieVariantManager (Spawn Logic)
```java
@SubscribeEvent
public static void onEntityJoinLevel(EntityJoinLevelEvent event) {
    if (event.getLevel().isClientSide) return;

    if (event.getEntity() instanceof Zombie zombie) {
        if (zombie.getPersistentData().contains(NBT_VARIANT_KEY)) return;

        ServerPlayer nearestPlayer = (ServerPlayer) event.getLevel().getNearestPlayer(zombie, 64);
        if (nearestPlayer == null) return;

        // Calculate logic based on Director...
        ZombieVariant variant = determineVariant(stage);
        applyVariant(zombie, variant);
    }
}
```

### ClientZombieVariantHandler (Visual Scaling)
```java
@SubscribeEvent
public static void onRenderLivingPre(RenderLivingEvent.Pre<?, ?> event) {
    if (event.getEntity() instanceof Zombie zombie) {
        ZombieVariant variant = ZombieVariantManager.getVariant(zombie);

        // Fallback check for name-based variants (Debug)
        if (variant == ZombieVariant.NORMAL && zombie.hasCustomName()) { ... }

        if (variant != ZombieVariant.NORMAL) {
            PoseStack poseStack = event.getPoseStack();
            float scale = variant.getScale();
            poseStack.pushPose();
            poseStack.scale(scale, scale, scale);
            // Stack is popped by renderer automatically for next entity
        }
    }
}
```

## [Fecha Actual] - Evolution Event Logic

### EvolutionEventManager (Global Event & Lives)
```java
@SubscribeEvent
public static void onPlayerDeath(LivingDeathEvent event) {
    if (event.getEntity() instanceof ServerPlayer player && isEventActive(player.level())) {
        CompoundTag tag = player.getPersistentData();
        if (tag.contains(NBT_LIVES_KEY)) {
            int lives = tag.getInt(NBT_LIVES_KEY);

            if (lives > 0) {
                event.setCanceled(true); // Cancel death
                lives--;
                tag.putInt(NBT_LIVES_KEY, lives);

                player.setHealth(player.getMaxHealth());
                player.teleportTo(spawn);
                player.sendSystemMessage(Component.literal("Lives remaining: " + lives));
            }
        }
    }
}
```

## [Fecha Actual] - Director Persistence (NBT)

### DirectorManager (Save/Load)
```java
public static void initForPlayer(ServerPlayer player) {
    // ... instances creation ...
    CompoundTag data = player.getPersistentData();
    progressTracker.load(data);

    // Auto-sync logic
    if (!data.contains(NBT_KEY_SYNCED)) {
        long worldTime = player.level().getDayTime();
        progressTracker.setPlaytimeTicks(worldTime);
        data.putBoolean(NBT_KEY_SYNCED, true);
        progressTracker.save(data);
    }
}

@SubscribeEvent
public static void onPlayerLoggedOut(PlayerEvent.PlayerLoggedOutEvent event) {
    saveDataForPlayer((ServerPlayer) event.getEntity());
    removeForPlayer(event.getEntity().getUUID());
}
```
