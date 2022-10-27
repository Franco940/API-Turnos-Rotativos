# API TURNOS ROTATIVOS - INSTRUCTIVO
## Funcionalidades

- Crear un empleado
- Crear tipos de jornada de trabajo
- Crear una jornada de trabajo
- Actualizar el estado de vacaciones de un empleado
- Actualizar las horas de trabajo de una jornada
- Listar para cada empleado la cantidad de horas cargadas por cada tipo de jornada laboral

## Cómo crear un empleado

Para crear un empleado se debe hacer una petición POST a la siguiente URI: localhost:8080/empleado
La petición tendrá que contener un body de formato JSON que debe contener los siguientes atributos:
- nombre
- apellido
-  numero de documento
-  email

Por ejemplo:
```sh
{
    "nombre" : "Prueba",
    "apellido" : "Test",
    "numeroDeDocumento" : 12312312
    "email" : "test@gmail.com"
}
```
#### Causa de excepciones
Estas excepciones devuelven un mensaje de error marcando el error con un estado HTTP 400.
A continuación se detallara las causas de una posible petición erronea:
- Nombre o apellido vacío o nulo.
- Número de documento menor a 1.000.000 o mayor a 999.999.999
- Formato incorrecto de email

En caso de que exista un empleado con la información que se envía. Se enviará un mensaje de error con un estado HTTP 409.

## Actualizar un empleado que se fue o vuelve de vacaciones
Para actualizar un empleado, se debe hacer una petición PUT a la siguiente URI: localhost:8080/empleado
Se necesitan dos para atributos actualizar, el número de documento y un valor booleano los cuales se deben enviar como
parametros en la URI.

El valor booleano deberá de ser TRUE si se va de vacaciones o FALSE si vuelve.

Los atributos se deberán de llamar:
- numeroDeDocumento
- estadoVacaciones

Por ejemplo: localhost:8080/empleado/vacaciones?numeroDeDocumento=12312312&estadoVacaciones=true

#### Causa de excepciones
Se lanza una excepcion si no se encuentra un empleado con el número de documento que se envía.
Esto devuelve un mensaje de error con el estado HTTP 400.

## Cómo crear un tipo de jornada laboral
Para crear un tiop de jornada laboral se deberá de hacer una petición POST a la sigueinte URI: localhost:8080/jornada/crear
La petición tendrá que contener un body de formato JSON que debe contener los siguientes atributos:
- nombre

Por ejemplo:
```sh
{
    "nombre" : "Tipo de prueba"
}
```

#### Causa de excepciones
Se lanzará una excepcion si ya existe un tipo de jornada con el mismo nombre. Se devolverá un mensaje de error con el estado HTTP 409.

## Cómo crear una jornada laboral
Para crear un tiop de jornada laboral se deberá de hacer una petición POST a la sigueinte URI: localhost:8080/jornada/laboral
La petición tendrá que contener un body de formato JSON que debe contener los siguientes atributos:
- Número de documento del empleado
- Fecha
-  Hora de comienzo de la jornada
-  Hora de finalizacion de la jornada
-  Tipo de jornada laboral

Por ejemplo:
```sh
{
    "numeroDeDocumento" : 12312312,
    "fecha" : "2022-03-05",
    "horaComienzo" : "10:00:00",
    "horaFinalizacion" : "18:00:00",
    "tipoDeJornadaLaboral" : "Turno normal"
}
```

Para ingresar un día libre, simplemente lo indica poniendo "Dia libre" en el atributo "tipoDeJornadaLaboral"

#### Causa de excepciones
Las siguientes causas devuelven un mensaje de erro con un estado HTTP 400:
- Cualquier campo vacío
- Número de documento menor a 1.000.000 o mayor a 999.999.999
- Fecha incorrecta. El formato correcto es: año/mes/día. Completando con 0(cero) en el día y mes si el número es menor a 10.
  Ejemplo: 2000-01-01 ó 2000-12-20
- Hora de comienzo o finalizacion incorrecta. Ej: La hora no puede ser mayor a 24 y, los minutos y segundos mayores a 59.
  Como tambien no se aceptan numeros negativos
- Tipo de jornada laboral incorrecto
- El empleado tenga más de 48 horas semanales
- No haya más espacio para un turno laboral (máximo 2 personas por turno en la jornada)
- Cantidad de horas de trabajo en la jornada
  6 a 8 horas en un turno normal
  2 a 6 horas en un turno extra
  12 horas máximo en un turno combinado
- Ya hay un registro de la misma persona en el mismo turno en el mismo día
- Si el empleado de la petición está de vacaciones
- Si el empleado de la petición está en un día libre
- Si el empleado de la petición solicita día libre y ya no tiene más días libres en la semana. Máximo 2 días libres por
  semana

## Cómo actualizar las horas de una jornada laboral
Para crear un tiop de jornada laboral se deberá de hacer una petición PUT a la sigueinte URI: localhost:8080/jornada/laboral
La petición tendrá que contener un body de formato JSON que debe contener los siguientes atributos:
- Número de documento del empleado
- Fecha
-  Nuevas horas de trabajo

Por ejemplo:
```sh
{
    "numeroDeDocumento" : 12312312,
    "fecha" : "2022-03-05",
    "nuevasHorasDeTrabajo" : 8
}
```
#### Causa de excepciones
Las siguientes causas devuelven un mensaje de erro con un estado HTTP 400:
- Cualquier campo vacío
- Número de documento menor a 1.000.000 o mayor a 999.999.999
- Fecha incorrecta. El formato correcto es: año/mes/día. Completando con 0(cero) en el día y mes si el número es menor a 10.
- Si no se encuentra un empleado con su número de documento
- Si no se encontró ninguna jornada de trabajo con la fecha y el número de documento
- Nuevas horas de trabajo menor a 2 horas y mayor a 12
- Si las horas de trabajo superan el máximo permitido por jornada
  6 a 8 horas en un turno normal
  2 a 6 horas en un turno extra
  12 horas máximo en un turno combinado

#### Listar para cada empleado la cantidad de horas cargadas por cada tipo de jornada laboral
Para crear un tiop de jornada laboral se deberá de hacer una petición GET a la sigueinte URI: localhost:8080/jornada/all
Esto devolverá una lista informando cuántas horas de trabajo tiene cada empleado en cada tipo de jornada laboral

Ejemplo de respuesta:
```sh
{
        "nombreEmpleado": "Prueba",
        "apellidoEmpleado": "Test",
        "numeroDeDocumento": 12312312,
        "tipoJornada": "Turno normal",
        "horasTrabajadas": 8
    },
    {
        "nombreEmpleado": "Pepito",
        "apellidoEmpleado": "PepitoAPellido",
        "numeroDeDocumento": 2000000,
        "tipoJornada": "Turno combinado",
        "horasTrabajadas": 12
    }
```
