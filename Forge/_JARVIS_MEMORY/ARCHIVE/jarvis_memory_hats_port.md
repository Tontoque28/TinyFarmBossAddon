# Registro de Proyecto: Hats (iChun) Port

**Usuario:** Tontoque28

**Resumen del Proyecto:**
Este proyecto es un port del mod "Hats" de iChun para Minecraft 1.20.1 utilizando Forge.

**Historial de Intervenciones:**

**Fecha:** 2026-04-03
**Problema:** El mod fallaba al iniciar con un error `java.lang.NullPointerException: Cannot invoke "java.lang.Class.getName()" because "this.modClass" is null` durante la fase de carga de mods de FML.

**Diagnóstico:**
1.  Se revisó `mods.toml` y se confirmó que usaba placeholders de Gradle, lo cual es correcto.
2.  Se inspeccionó la clase principal `Hats.java` y se verificó que la anotación `@Mod(Hats.MOD_ID)` era correcta.
3.  Se analizó `build.gradle` y se confirmó que la tarea `processResources` estaba configurada para reemplazar los placeholders en `mods.toml`.
4.  Se examinó `gradle.properties` y se validó que todas las propiedades, incluyendo `mod_id`, estaban definidas.
5.  Se detectó una inconsistencia entre `mod_group_id` en `gradle.properties` (`com.tontoque28.hats`) y la estructura de paquetes real (`com.Tontoque28.Hats`).
6.  Se identificó que, a pesar de la configuración correcta, FML no lograba resolver la clase principal del mod, una causa común del error `FMLModContainer` NullPointerException.

**Solución Implementada:**
1.  Se corrigió el `mod_group_id` en `gradle.properties` a `com.Tontoque28.Hats` para que coincidiera con la estructura de paquetes.
2.  Se modificó el archivo `build.gradle` para añadir explícitamente la propiedad `modclass` al manifiesto del JAR (`MANIFEST.MF`). Esto proporciona una referencia directa a la clase principal del mod, asegurando que Forge pueda localizarla durante el proceso de carga.
    ```groovy
    tasks.named('jar', Jar).configure {
        manifest {
            attributes([
                // ... otras propiedades
                'modclass': 'com.Tontoque28.Hats.Hats'
            ])
        }
    }
    ```

**Estado:** Cambios aplicados. Pendiente de que el usuario confirme la resolución del problema ejecutando el cliente.