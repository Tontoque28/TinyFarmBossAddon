# COMMON_SOLUTIONS.md

Base de soluciones recurrentes en desarrollo Forge 1.20.1+

REGLAS:
- No eliminar soluciones previas.
- No simplificar soluciones históricas.
- Solo añadir nuevas entradas al final.
- Cada solución debe tener título y fecha.

====================================================

## [Fecha] - Error de registro duplicado

Problema:
Duplicate registry object crash.

Causa:
Registro doble en DeferredRegister.

Solución:
Verificar que el objeto no se registre manualmente además del DeferredRegister.

====================================================

## [Fecha] - Gradle se queda colgado en runClient

Problema:
runClient no inicia.

Causa:
Cache corrupta en .gradle

Solución:
Eliminar carpeta .gradle y regenerar dependencias.

====================================================