# 02_MEMORY_PROTOCOL.md
# Protocolo Anti-Saturación y Escalabilidad
ESTADO: SEMI-PROTEGIDO

Regla:
Este archivo no puede modificarse automáticamente.
Solo puede actualizarse si el usuario dice explícitamente:

"Actualizar protocolo de memoria"

En cualquier otro caso:
NO editar.

====================================================
REGLA DE TAMAÑO
====================================================

Si cualquier archivo supera:

- 800 líneas
o
- 70% del contexto útil estimado

Se debe:


1. Crear archivo adicional numerado.
2. Mover contenido antiguo al ARCHIVE.
3. Mantener referencia cruzada.
4. Nunca eliminar información histórica.


====================================================
DIVISIÓN POR VERSIONES
====================================================

Si un proyecto tiene más de 5 versiones documentadas:

Crear:

/PROJECTS/[MOD]/VERSIONS/

Y dividir por versión individual.

====================================================
PRESERVACIÓN HISTÓRICA
====================================================

- Nunca borrar proyectos antiguos.
- Nunca sobrescribir versiones bloqueadas.
- Si un proyecto queda inactivo, moverlo a ARCHIVE/.

====================================================
PROTOCOLO DE CREACIÓN DE NUEVO PROYECTO
====================================================

Cuando el usuario indique que inicia un nuevo mod:

OBLIGATORIO:

1. Crear nueva carpeta en:
   ai/PROJECTS/[NOMBRE_DEL_MOD]/

2. Crear dentro:

   PROJECT_INFO.md
   ARCHITECTURE.md
   FULL_CODE_REFERENCES.md
   CHANGELOG_INTERNAL.md
   PUBLISH_INFO.md (Nuevo estándar para descripciones públicas)

3. Registrar el nuevo proyecto en:
   ai/PROJECT_INDEX.md

4. No mezclar información con proyectos existentes.

5. No reutilizar carpetas previas.

====================================================
ESTÁNDAR DE DOCUMENTACIÓN PÚBLICA
====================================================

- CHANGELOG_INTERNAL.md: Contiene tanto el historial técnico como el texto listo para copiar (Changelog Público).
- PUBLISH_INFO.md: Contiene la descripción completa del mod (Description) lista para CurseForge/Modrinth.
- README.md: Solo si es necesario para repositorios Git, debe ser idéntico a PUBLISH_INFO.md pero en raíz (opcional).

====================================================
PROHIBICIÓN DE IDENTIDAD
====================================================

PROHIBIDO usar el nombre "Danie" en:
- Paquetes
- Autores
- Créditos
- Descripciones
- Cualquier metadato del mod

El único autor válido es: Tontoque28

====================================================
La documentación (CHANGELOG, SUMMARY, registro de cambios)
====================================================

solo debe actualizarse cuando el usuario lo indique explícitamente
con frases como:

- "Actualiza la documentación"
- "Cerrar versión"
- "Registrar cambios"
- "Preparar release"

No actualizar documentación automáticamente después de cada acción.