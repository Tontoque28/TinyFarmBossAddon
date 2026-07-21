package com.tontoque28.tinyfarmboss.config;

import net.minecraftforge.common.ForgeConfigSpec;
import java.util.List;

public class TinyFarmBossConfig {
    public static final ForgeConfigSpec COMMON_SPEC;
    
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> CUSTOM_FARMABLE_MOBS;
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> BLACKLISTED_MOBS;

    static {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();

        builder.push("FarmingSettings");

        CUSTOM_FARMABLE_MOBS = builder
                .comment("Lista de mobs personalizados que pueden ser capturados con el Lasso.",
                         "Formato: 'modid:mob_name' (ejemplo: 'cataclysm:ignis')")
                .defineList("custom_farmable_mobs", List.of(), obj -> obj instanceof String);

        BLACKLISTED_MOBS = builder
                .comment("Lista de mobs que NO pueden ser capturados por seguridad (Safeguard).",
                         "Soporta comodines (*) al final para bloquear mods enteros.",
                         "Ejemplo: 'minecolonies:*' bloquea todos los mobs de MineColonies.")
                .defineList("blacklisted_mobs", List.of("minecolonies:*"), obj -> obj instanceof String);

        builder.pop();
        COMMON_SPEC = builder.build();
    }
}
