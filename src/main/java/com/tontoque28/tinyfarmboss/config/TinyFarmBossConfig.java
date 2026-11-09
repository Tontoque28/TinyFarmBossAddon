package com.tontoque28.tinyfarmboss.config;

import net.minecraftforge.common.config.Configuration;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class TinyFarmBossConfig {
    public static List<String> customFarmableMobs = Collections.emptyList();
    public static List<String> blacklistedMobs = Collections.singletonList("minecolonies:*");

    public static void init(File file) {
        Configuration config = new Configuration(file);
        try {
            config.load();
            String[] custom = config.getStringList("custom_farmable_mobs", "FarmingSettings", new String[0],
                    "Lista de mobs personalizados que pueden ser capturados con el Lasso. Formato: modid:mob_name (ejemplo: cataclysm:ignis)");
            String[] blacklist = config.getStringList("blacklisted_mobs", "FarmingSettings", new String[]{"minecolonies:*"},
                    "Lista de mobs que NO pueden ser capturados por seguridad (Safeguard). Soporta comodines (*) al final para bloquear mods enteros. Ejemplo: minecolonies:* bloquea todos los mobs de MineColonies.");
            customFarmableMobs = Arrays.asList(custom);
            blacklistedMobs = Arrays.asList(blacklist);
        } finally {
            if (config.hasChanged()) {
                config.save();
            }
        }
    }
}
