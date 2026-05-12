# ARCHITECTURE.md
# Arquitectura del Proyecto: MVEL

## Estructura de Paquetes
`com.tontoque28.mvel`
- `.mixin`: Contiene las clases Mixin para inyectar código en clases vanilla.
- `.config`: (Pendiente) Configuración para controlar los niveles máximos.

## Componentes Principales

### Mixins
- `EnchantmentMixin`: Modifica `Enchantment#getMaxLevel()` para encantamientos base que no tienen una subclase específica.
- `VanillaEnchantmentSubclassesMixin`: Modifica `getMaxLevel()` para un conjunto específico de encantamientos vanilla que sí tienen subclases, evitando así la necesidad de un mixin por cada uno.

## Notas de Diseño
- Se utiliza una estrategia de doble Mixin para cubrir tanto la clase base `Enchantment` como sus subclases específicas.
- Esto asegura una máxima compatibilidad y cobertura de todos los encantamientos vanilla.
- El objetivo es que sea configurable (futuro).
