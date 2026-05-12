# CHANGELOG_INTERNAL.md
# Historial de Cambios Interno y Público para T28's Universal Veinminer

Este archivo documenta todos los cambios realizados en el mod, tanto a nivel de desarrollo interno como las notas de la versión pública.

---

## [1.0.2] - 2026-04-11
### Añadido
- **Detección Automática de Minerales por Nombre:** El mod ahora reconoce automáticamente cualquier bloque cuyo ID contenga `_ore` como un mineral veinminable. Esto proporciona compatibilidad inmediata con la mayoría de los mods que añaden nuevos minerales, sin necesidad de configuración manual.

### Eliminado
- **Veinminer para Troncos:** Se ha eliminado la funcionalidad que permitía usar el Veinminer en árboles (bloques con la etiqueta `logs`). El mod ahora se enfoca exclusivamente en minerales y bloques configurados manualmente.

---

## [1.0.1] - 2026-04-11
### Añadido
- **Soporte para Ancient Debris:** Se ha añadido una lógica especial para que el Veinminer sea compatible con `ancient_debris`.
  - Al minar con Fortuna, ahora suelta directamente `netherite_scrap`, con probabilidades de obtener más fragmentos según el nivel del encantamiento.
  - Al minar con Toque de Seda, suelta el bloque de `ancient_debris` como es de esperar.

### Mejorado
- **Algoritmo de Detección:** Se ha reescrito el algoritmo de búsqueda de bloques para que ahora explore un área de 3x3x3 alrededor de cada bloque, incluyendo diagonales. Esto soluciona el problema de que quedaran bloques flotantes en vetas grandes o con formas irregulares.
- **Eficiencia de Búsqueda:** La nueva lógica de búsqueda es más robusta y se expande de manera más eficiente, asegurando que se alcance el límite máximo de bloques configurado incluso en las vetas más complejas.

---

## [1.0.0] - 2026-04-11
### Añadido
- **Lanzamiento Inicial:** "T28's Universal Veinminer" para Forge 1.20.1.
- **Mecánica Central:** Sistema de minado en cadena (Veinminer) activado al estar agachado (`Sneak`).
- **Detección Dinámica:** Identificación automática de minerales (`Tags.Blocks.ORES`) y madera (`BlockTags.LOGS`) utilizando el sistema de etiquetas de Forge.
- **Algoritmo de Búsqueda:** Implementación BFS (Búsqueda en Anchura) 3D para encontrar bloques idénticos adyacentes.
- **Límite de Seguridad:** Configuración para limitar el número máximo de bloques minados (por defecto: 64) para prevenir lag.
- **Integración Vanilla Completa:**
  - Respeto total por encantamientos: *Fortuna* (Loot y XP) y *Toque de Seda*.
  - Consumo correcto de durabilidad de la herramienta por cada bloque, respetando *Irrompibilidad*.
  - Prevención de rotura de herramienta: el proceso se detiene si a la herramienta le queda 1 punto de durabilidad.
  - Agotamiento de hambre aplicado por cada bloque minado.
- **Personalización (In-Game):**
  - **Tecla Dedicada (Keybinding):** Nueva tecla (por defecto 'V') para activar/desactivar la funcionalidad de Veinminer de forma global. Muestra mensaje en el Action Bar.
  - **Traducciones:** Añadidos archivos de idioma (en_us y es_es) para que los nombres de las teclas y mensajes se muestren correctamente y no como claves sin formato. Corregido el formato del mensaje al alternar el mod para que use el argumento del estado correctamente.
  - **Comandos:** Nuevo comando `/universalveinminer` (requiere permisos OP nivel 2) con subcomandos:
    - `addblock <block_id>`: Añade un bloque específico a la lista permitida.
    - `removeblock <block_id>`: Elimina un bloque de la lista.
    - `listblocks`: Muestra todos los bloques añadidos manualmente.
- **Sistema de Configuración:** Archivo de configuración `universalveinminer-common.toml` para persistencia del estado (activado/desactivado), el límite de bloques y la lista personalizada.
