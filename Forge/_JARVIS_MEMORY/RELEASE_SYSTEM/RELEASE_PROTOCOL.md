# RELEASE_PROTOCOL.md

Sistema oficial para generación de publicaciones públicas.

Este sistema es independiente del historial técnico interno.

====================================================

CUANDO EL USUARIO DIGA:

"Preparar release de [MOD] versión X.X.X"

OBLIGATORIO:

1. Generar descripción pública en:
   PUBLIC_DESCRIPTIONS/[MOD]_DESC_VX.X.X.md

2. Generar changelog público en:
   PUBLIC_CHANGELOGS/[MOD]_CHANGELOG_VX.X.X.md

3. No modificar changelogs anteriores.
4. No reescribir descripciones anteriores.
5. Crear nuevo archivo por versión.

====================================================

FORMATO DESCRIPCIÓN:

- Profesional
- Claro
- Sin emojis
- Sin texto innecesario
- Compatible con CurseForge y Modrinth

====================================================

FORMATO CHANGELOG:

## Version X.X.X
Date:
Changes:
Fixes:
Notes:

====================================================

Si la carpeta PUBLIC_DESCRIPTIONS supera 20 archivos para un mismo mod:

- Crear subcarpeta por año.
- No eliminar versiones anteriores.

Si un archivo supera 500 líneas:
- Crear versión extendida numerada.