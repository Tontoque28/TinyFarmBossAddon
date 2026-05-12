# 01_RULES.md
# JARVIS v2 - REGLAS OPERATIVAS ULTRA-BLINDADAS (READ ONLY)

====================================================
REGLA ABSOLUTA DE EDICIÓN
====================================================

1. PROHIBIDO eliminar líneas existentes.
2. PROHIBIDO modificar líneas existentes.
3. PROHIBIDO reordenar contenido existente.
4. SOLO se permite añadir contenido al FINAL del bloque editable correspondiente.
5. Nunca reescribir el archivo completo.
6. Nunca simplificar estructura histórica.
7. Nunca limpiar comentarios previos.

Si se requiere modificar algo previo:
- Citar línea exacta.
- Explicar motivo.
- Esperar confirmación explícita.

====================================================
FORMATO DE RESPUESTA OBLIGATORIO
====================================================

Cuando propongas cambios en archivos de memoria:

- Devuelve ÚNICAMENTE el bloque nuevo a añadir.
- No repitas contenido previo.
- No formatees nuevamente el archivo.
- No hagas refactor estructural.
- No hagas optimización editorial.

====================================================
CONTROL DE VERSIONES
====================================================

Antes de finalizar un proyecto:

Preguntar:
1. ¿A qué versión quieres subir el mod?
2. ¿Cuál es el nombre final?

Luego:
- Actualizar mods.toml
- Generar texto CurseForge
- Generar texto Modrinth
- Generar Changelog público

====================================================
REGLAS DE ADDONS Y DEPENDENCIAS
====================================================

Licencia:
- Verificar SIEMPRE licencia original.
- All Rights Reserved → No hacer addon sin permiso.
- MIT/GPL → Dar crédito explícito.

Descripción:
- Primera línea debe decir: "Unofficial Addon".
- Enlazar mod original y autor.

Código:
- No copiar código original.
- Usar APIs, Eventos o Mixins.
- Si es necesario, usar como dependencia externa.

====================================================
ARCHIVOS ESPECIALES
====================================================

changelog.txt:
- Ignorarlo completamente.

JARVIS_SUMMARY.md:
- Solo changelogs profesionales.
- No código.
- No memoria operativa.
- Versiones con fecha real quedan BLOQUEADAS.

====================================================
REGLA DE PRESERVACIÓN DE CÓDIGO:
====================================================

- Nunca simplificar snippets históricos.
- Nunca resumir implementaciones pasadas.
- Nunca optimizar código archivado.
- El código en FULL_CODE_REFERENCES.md es inmutable.

====================================================
MODO PROYECTO ACTIVO:
====================================================

Solo cargar en contexto:
- CORE/
- LIBRARY/
- PROJECTS/[PROYECTO_ACTIVO]/

No analizar proyectos no activos salvo solicitud explícita.

====================================================
PROTECCIÓN DE SNIPPETS:
====================================================

Los bloques de código dentro de:

- FULL_CODE_REFERENCES.md
- ARCHIVE/

Son históricos.

Está prohibido:
- Reformatearlos
- Simplificarlos
- Modernizarlos
- Cambiar sintaxis

Solo se permite agregar nuevos bloques.

====================================================
PRIORIDAD JERÁRQUICA DE REGLAS
====================================================

Si existe conflicto entre:

1. Optimización
2. Claridad estructural
3. Refactor sugerido
4. Limpieza editorial

Y estas reglas de preservación,

SIEMPRE tienen prioridad las reglas de preservación.

Nunca sacrificar historial por claridad.

====================================================
REGLA ANTI-REFACTOR GLOBAL
====================================================

Está prohibido:

- Dividir archivos existentes sin autorización.
- Fusionar archivos existentes.
- Reestructurar carpetas.
- Mover contenido entre archivos históricos.
- Compactar múltiples secciones en una sola.

Solo se permite crear nuevos archivos cuando sea necesario,
sin alterar archivos previos.

====================================================
REGLA ANTI-SATURACIÓN
====================================================

Si un archivo supera:

- 800 líneas
o
- 60% del contexto disponible estimado,

Se debe:

1. Crear nuevo archivo en ARCHIVE/.
2. Mover contenido antiguo allí.
3. Dejar referencia cruzada.
4. No eliminar contenido original sin respaldo.

====================================================
REGLA DE RESPUESTA SEGURA
====================================================

Cuando la modificación afecte memoria histórica:

- Generar únicamente el bloque incremental.
- Nunca devolver el archivo completo.
- Nunca usar formato "Rewrite entire file".
- Nunca aplicar limpieza automática.

====================================================
REGLA DE TONTOQUE28
====================================================

1. El código generado debe ser limpio y estructurado.
   - No añadir comentarios innecesarios.
   - No eliminar anotaciones técnicas obligatorias (@Override, @Mod, etc.).
   - No eliminar documentación funcional cuando sea importante.

2. Se permite el uso de múltiples clases, herencias y clases abstractas
   solo cuando aporte claridad arquitectónica real.
   Evitar sobre-ingeniería innecesaria.

3. No agregar comentarios decorativos o separadores visuales innecesarios,
   como líneas de guiones o encabezados tipo:
   // --- SECCIÓN ---
   // ===== BLOQUE =====
   // ---------

   Solo permitir comentarios cuando expliquen lógica relevante.
   La prioridad es claridad estructural real, no decoración visual.

4. No agregar comentarios explicativos en el código generado.
   El código debe entregarse sin comentarios de explicación,
   salvo que el usuario lo solicite explícitamente.

   Mantener únicamente anotaciones técnicas obligatorias
   (@Override, @Mod, @SubscribeEvent, etc.).

====================================================
REGLA DE SISTEMA GLOBAL DE RELEASE
====================================================

El archivo:

RELEASE_SYSTEM/RELEASE_PROTOCOL.md

pertenece exclusivamente al sistema global de publicación.

No forma parte de:
- ningún proyecto
- ningún changelog de proyecto
- ninguna descripción pública de mod
- ningún resumen JARVIS de proyecto

====================================================
SEPARACIÓN OBLIGATORIA
====================================================

RELEASE_PROTOCOL.md define únicamente:

- Procedimiento técnico de publicación
- Flujo de versionado
- Pasos de validación antes del release
- Proceso de generación de archivos finales

NO contiene:

- changelogs de mods
- descripciones de CurseForge
- descripciones de Modrinth
- notas de desarrollo
- historial de proyectos

====================================================
REGLA DE NO CONFUSIÓN
====================================================

Cuando se trabaje con proyectos dentro de:

PROJECTS/[PROYECTO]/

Los archivos de release del proyecto siguen siendo:

- changelog.txt
- JARVIS_SUMMARY.md
- descripciones de publicación

RELEASE_PROTOCOL.md nunca debe mezclarse,
interpretarse o fusionarse con estos archivos.

Su función es únicamente definir el protocolo
global de publicación del sistema JARVIS.