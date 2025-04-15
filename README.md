# Calculadora de Subredes VLSM para Android

## Descripción

Esta aplicación Android permite realizar cálculos de subredes utilizando el método VLSM (Variable Length Subnet Masking). La herramienta está diseñada para ayudar a estudiantes, técnicos y profesionales de redes a planificar y subdividir redes IP de manera eficiente, asignando el espacio justo necesario para cada subred según sus requerimientos específicos de hosts.

## Características

- Cálculo de subredes VLSM a partir de una dirección IP base
- Soporte para diferentes prefijos CIDR (Classless Inter-Domain Routing)
- Creación dinámica de múltiples subredes según necesidad
- Especificación del número exacto de hosts requeridos para cada subred
- Visualización clara de resultados en formato tabular
- Cálculo automático de:
  - Dirección IP de la subred
  - Prefijo CIDR de cada subred
  - Rango de direcciones IP utilizables
  - Dirección de broadcast
  - Número de hosts disponibles vs. requeridos

## Requisitos

- Android 5.0 (API nivel 21) o superior
- Java 8+
- Android Studio 4.0+

## Instalación

1. Clone el repositorio:
   ```bash
   git clone https://github.com/Antho-321/Calculadora_VLSM.git
   ```

2. Abra el proyecto en Android Studio:
   - Seleccione "Open an existing Android Studio project"
   - Navegue hasta la carpeta del proyecto y seleccione "OK"

3. Compile y ejecute la aplicación:
   - Seleccione "Run" > "Run 'app'" en Android Studio
   - Elija un dispositivo o emulador para ejecutar la aplicación

## Uso de la aplicación

1. **Dirección IP Base**: Ingrese la dirección de red base (ej. 192.168.1.0)
2. **Prefijo CIDR**: Seleccione el prefijo CIDR para la red base (ej. /24)
3. **Número de subredes**: Indique cuántas subredes desea crear
4. **Hosts por subred**: Para cada subred, especifique el número de hosts requeridos
5. **Calcular**: Presione el botón "Calcular Subredes" para obtener los resultados
6. **Resultados**: Se mostrará una tabla con la información de cada subred

## Estructura del proyecto

```
ec/edu/utn/example/calculadora_vlsm/
├── MainActivity.java         # Controlador principal y UI
├── VLSMCalculator.java       # Lógica de cálculo VLSM
└── UIValidator.java          # Validación de entradas

res/layout/
├── activity_main.xml         # Layout principal
└── subnet_results_layout.xml # Layout para resultados
```

## Componentes clave

### MainActivity

Maneja la interfaz de usuario y la interacción con el usuario. Incluye:
- Gestión dinámica de campos de entrada para hosts
- Validación de entrada
- Presentación de resultados

### VLSMCalculator

Implementa la lógica de cálculo VLSM:
- Conversión entre notación IP decimal y formato binario
- Cálculo del tamaño de subred óptimo para los hosts requeridos
- Asignación de direcciones IP a subredes
- Cálculo de rangos de dirección y broadcast

### UIValidator

Proporciona funciones de validación para:
- Direcciones IPv4
- Valores CIDR
- Conteo de subredes y hosts

## Algoritmo VLSM

El algoritmo implementado sigue estos pasos:
1. Ordena las subredes por número de hosts (de mayor a menor)
2. Para cada subred:
   - Calcula el prefijo CIDR mínimo necesario
   - Asigna direcciones IP desde el espacio disponible
   - Calcula la dirección de broadcast y el rango utilizable
3. Verifica que todas las subredes encajen en la red base

## Contribuciones

Las contribuciones son bienvenidas. Por favor, siga estos pasos:
1. Haga fork del proyecto
2. Cree una rama para su característica (`git checkout -b feature/nueva-caracteristica`)
3. Haga commit de sus cambios (`git commit -m 'Añadir nueva característica'`)
4. Haga push a la rama (`git push origin feature/nueva-caracteristica`)
5. Abra un Pull Request

## Licencia

Este proyecto está licenciado bajo la Licencia MIT - vea el archivo LICENSE para detalles.

## Contacto

Si tiene preguntas o sugerencias, por favor abra un issue en el repositorio.
