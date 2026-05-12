## Version 1.1.2
Date: 2026-04-12
Changes:
- **Portabilidad Completa a Fabric 1.20.1:** El mod ha sido completamente reescrito y adaptado para el entorno Fabric, asegurando compatibilidad y rendimiento.
- **Ajustes de Tablas de Botín de Bosses:**
  - **Wither:** Ahora dropea Nether Star y Arena de Almas (cantidad ajustada para equilibrio).
  - **Ender Dragon:** Dropea Huevo de Dragón (probabilidad ajustada), Aliento de Dragón (cantidad aumentada y probabilidad ajustada), Cabeza de Dragón (probabilidad ajustada) y una probabilidad muy rara de Elytra.
  - **Warden:** Dropea Catalizador de Sculk, Fragmentos de Eco, Bloques de Sculk, Shriekers de Sculk y Venas de Sculk (cantidades ajustadas para equilibrio).
- **Configuración de Build Optimizada:** Se han ajustado los archivos `build.gradle` y `gradle.properties` para una gestión de dependencias más robusta y un nombre de archivo JAR final correcto (`tinyfarmbossaddon-1.1.2-Fabric.jar`).
- **Configuración de Mixins:** Archivos de configuración de Mixin (`tinyfarmboss.mixins.json`, `tinyfarmboss.client.mixins.json`) creados y configurados para el entorno Fabric.
Fixes:
- Resueltos múltiples errores de compilación relacionados con la resolución de dependencias de Fabric API.
- Solucionados problemas de "Could not find" para dependencias de CurseMaven.
- Corregidos errores de inicialización de Mixin.
- Asegurado el nombre de archivo JAR final correcto.
Notes:
- Esta versión establece una base sólida y estable para el desarrollo futuro en Fabric.
- Se recomienda una limpieza completa de la caché de Gradle (`gradlew cleanBuildCache`, eliminar `.gradle` y `build` manualmente) antes de compilar para asegurar una construcción limpia.
