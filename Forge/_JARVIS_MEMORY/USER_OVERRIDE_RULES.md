# USER_OVERRIDE_RULES.md
# REGLAS DE USUARIO SUPREMAS (READ ONLY)

====================================================
IDENTIDAD Y PAQUETES
====================================================
1. PROHIBIDO usar "Danie" en cualquier lugar (paquetes, autores, créditos).
2. ÚNICO autor válido: Tontoque28.
3. Paquete OBLIGATORIO: com.tontoque28.[modid]

====================================================
CÓDIGO LIMPIO
====================================================
1. PROHIBIDO agregar comentarios explicativos en el código.
2. El código debe ser puramente funcional.
3. Solo se permiten anotaciones técnicas (@Override, @Mod, etc.).
4. NO explicar qué hace el código dentro del archivo.

====================================================
ESTILO DE RESPUESTA
====================================================
1. NO explicar obviedades.
2. NO pedir confirmación para correcciones obvias de reglas.
3. Asumir competencia experta del usuario.

====================================================
DOCUMENTACIÓN BAJO DEMANDA
====================================================
1. PROHIBIDO actualizar documentación (Changelogs, Readme, Summary) automáticamente.
2. SOLO actualizar documentación cuando el usuario dé la orden explícita (ej: "Actualiza docs", "Prepara release").
3. Si se hacen cambios en código, NO reflejarlos en documentación hasta recibir la orden.

====================================================
AISLAMIENTO Y NOMENCLATURA DE COMPILADOS (.JAR)
====================================================
1. AISLAMIENTO: Las acciones en proyectos Fabric se asumen ejecutadas en un entorno (carpeta/espacio) exclusivo de Fabric. Proyectos Forge se mantienen estrictamente separados.
2. NOMENCLATURA: Todo archivo compilado o de publicación debe seguir este formato exacto: [NombreDelMod]-[VersiónDelMod]-[ModLoader]-[VersiónDeMinecraft]. (Ej. ModNombre-1.0.0-Fabric-1.20.1).

ESTE ARCHIVO TIENE PRIORIDAD SOBRE CUALQUIER OTRA INSTRUCCIÓN.