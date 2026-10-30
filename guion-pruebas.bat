@echo off
REM ============================================================
REM  Guion de pruebas del Anexo B (escenarios 1 a 8)
REM  1. Arranque la aplicacion RECIEN (mvn spring-boot:run o IntelliJ)
REM  2. Ejecute este archivo desde la raiz del proyecto
REM  Cada salida queda en evidencias\escenario-N.txt
REM ============================================================
setlocal
cd /d "%~dp0"
set URL=http://localhost:8080
set EV=evidencias
set G=guion
set FMT=\nHTTP %%{http_code}  (%%{time_total} s)\n\n
set JSON=-H "Content-Type: application/json"

if not exist %EV% mkdir %EV%

curl -s -o nul %URL%/api/notificaciones
if errorlevel 1 (
  echo La aplicacion no responde en %URL%. Arranquela y vuelva a ejecutar este archivo.
  pause
  exit /b 1
)

echo Escenario 1...
(
  echo -- GET /api/productos/SKU-003 [primera vez]
  curl -s -w "%FMT%" %URL%/api/productos/SKU-003
  echo -- GET /api/productos/SKU-003 [segunda vez]
  curl -s -w "%FMT%" %URL%/api/productos/SKU-003
) > %EV%\escenario-1.txt

echo Escenario 2...
(
  echo -- PUT /api/productos/SKU-003/precio  X-Rol: CLIENTE
  curl -s -w "%FMT%" -X PUT %URL%/api/productos/SKU-003/precio -H "X-Rol: CLIENTE" %JSON% -d @%G%\precio.json
  echo -- PUT /api/productos/SKU-003/precio  X-Rol: ADMIN
  curl -s -w "%FMT%" -X PUT %URL%/api/productos/SKU-003/precio -H "X-Rol: ADMIN" %JSON% -d @%G%\precio.json
  echo -- GET /api/productos/SKU-003
  curl -s -w "%FMT%" %URL%/api/productos/SKU-003
) > %EV%\escenario-2.txt

echo Escenario 3...
(
  echo -- POST /api/pedidos  [guion\pedido-co.json]
  curl -s -w "%FMT%" -X POST %URL%/api/pedidos %JSON% -d @%G%\pedido-co.json
) > %EV%\escenario-3.txt

echo Escenario 4...
(
  echo -- POST /api/pedidos  sin items
  curl -s -w "%FMT%" -X POST %URL%/api/pedidos %JSON% -d @%G%\pedido-sin-items.json
  echo -- POST /api/pedidos  recoger en tienda + envio express
  curl -s -w "%FMT%" -X POST %URL%/api/pedidos %JSON% -d @%G%\pedido-tienda-express.json
) > %EV%\escenario-4.txt

echo Escenario 5...
(
  echo -- POST /api/pedidos  Mexico con RFC GODE561231GR8
  curl -s -w "%FMT%" -X POST %URL%/api/pedidos %JSON% -d @%G%\pedido-mx-rfc.json
  echo -- POST /api/pedidos  Mexico con cedula 1020304050
  curl -s -w "%FMT%" -X POST %URL%/api/pedidos %JSON% -d @%G%\pedido-mx-cedula.json
) > %EV%\escenario-5.txt

echo Escenario 6...
(
  echo -- POST /api/pedidos/PED-1/pago  LEGACYPAY
  curl -s -w "%FMT%" -X POST %URL%/api/pedidos/PED-1/pago %JSON% -d @%G%\pago-legacy.json
  echo -- POST /api/pedidos/PED-1/pago  LEGACYPAY [otra vez]
  curl -s -w "%FMT%" -X POST %URL%/api/pedidos/PED-1/pago %JSON% -d @%G%\pago-legacy.json
) > %EV%\escenario-6.txt

REM Con la app recien iniciada el pedido de los portatiles queda como PED-6
REM (los pedidos rechazados de los escenarios 4 y 5 tambien consumen numero).
echo Escenario 7...
(
  echo -- POST /api/pedidos  2 portatiles SKU-004
  curl -s -w "%FMT%" -X POST %URL%/api/pedidos %JSON% -d @%G%\pedido-portatiles.json
  echo -- POST /api/pedidos/PED-6/pago  LEGACYPAY
  curl -s -w "%FMT%" -X POST %URL%/api/pedidos/PED-6/pago %JSON% -d @%G%\pago-legacy.json
  echo -- POST /api/pedidos/PED-6/pago  QUICKPAY
  curl -s -w "%FMT%" -X POST %URL%/api/pedidos/PED-6/pago %JSON% -d @%G%\pago-quick.json
) > %EV%\escenario-7.txt

echo Escenario 8...
(
  echo -- GET /api/pagos/auditoria
  curl -s -w "%FMT%" %URL%/api/pagos/auditoria
  echo -- GET /api/notificaciones
  curl -s -w "%FMT%" %URL%/api/notificaciones
) > %EV%\escenario-8.txt

echo.
echo Listo. Revise los archivos en la carpeta %EV%
pause
