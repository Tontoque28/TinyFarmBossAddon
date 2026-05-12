# 02_FORGE_PATTERNS.md
# Biblioteca Técnica Reutilizable - Forge 1.20.1+

====================================================
INYECCIÓN DE DEPENDENCIAS (Main Class)
====================================================

@Mod(MyMod.MODID)
public class MyMod {

    public static final boolean DEV_MODE = true;

    public MyMod(FMLJavaModLoadingContext context) {
        IEventBus modEventBus = context.getModEventBus();
        modEventBus.addListener(this::commonSetup);
        MinecraftForge.EVENT_BUS.register(this);
    }
}

====================================================
MODIFICACIÓN SEGURA DE ATRIBUTOS
====================================================

Usar AttributeModifier con UUID fijo.

private static final UUID BUFF_UUID =
    UUID.fromString("c0d0b000-0000-0000-0000-000000000001");

AttributeInstance attr = entity.getAttribute(Attributes.MAX_HEALTH);

if (attr != null) {
    attr.removeModifier(BUFF_UUID);
    attr.addPermanentModifier(
        new AttributeModifier(
            BUFF_UUID,
            "Jarvis Buff",
            value,
            AttributeModifier.Operation.MULTIPLY_TOTAL
        )
    );
}

====================================================
PERSISTENCIA GLOBAL (SavedData)
====================================================

public class MyModSavedData extends SavedData {

    private static final String DATA_NAME = "mymod_data";

    public static MyModSavedData get(ServerLevel level) {
        return level.getDataStorage()
            .computeIfAbsent(MyModSavedData::load,
                             MyModSavedData::new,
                             DATA_NAME);
    }

    public static MyModSavedData load(CompoundTag tag) {
        return new MyModSavedData();
    }

    @Override
    public CompoundTag save(CompoundTag tag) {
        return tag;
    }
}

====================================================
REGISTRO DE COMANDOS
====================================================

@SubscribeEvent
public void onRegisterCommands(RegisterCommandsEvent event) {
    MyModCommands.register(event.getDispatcher());
}