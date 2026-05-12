@echo off
cd /d "%~dp0"

for /f %%i in ('powershell -NoProfile -Command "Get-Date -Format yyyy-MM-dd"') do set today=%%i

set target=%~dp0CORE\03_CURRENT_DATE.md

(
echo === CURRENT SYSTEM DATE ===
echo.
echo Fecha actual del sistema:
echo.
echo %today%
echo.
echo Regla:
echo Este archivo contiene la fecha real del sistema.
echo Siempre debe leerse antes de registrar cualquier cambio.
echo Nunca reutilizar fechas anteriores.
echo Nunca copiar fecha desde otros archivos.
echo Usar únicamente la fecha presente aquí.
) > "%target%"

echo.
echo Fecha actualizada correctamente a %today%
pause