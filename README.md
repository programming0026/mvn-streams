# Streams de Java: map, filter, collect, groupingBy...

Guía práctica y ejecutable de la **API de Streams** con ejemplos sobre una lista de empleados. Java 21, sin dependencias externas.

## Idea clave

Un stream es una **tubería perezosa**: las operaciones intermedias (`filter`, `map`, `sorted`...) no se ejecutan hasta que una operación terminal (`collect`, `count`, `reduce`, `forEach`...) pide el resultado. Un stream solo se puede consumir una vez.

## Qué contiene cada paquete

| Paquete | Clase | Contenido |
|---------|-------|-----------|
| `basics` | `BasicOperationsDemo` | `filter`, `map`, `sorted`, `distinct`, `limit`, `skip`, `reduce`, `findFirst`, `anyMatch`/`allMatch`/`noneMatch`, `count`, `max`, `IntStream` y la ejecución perezosa con `peek` |
| `collectors` | `CollectorsDemo` | `groupingBy` (con `counting`, `averagingDouble`, `summingDouble`, `mapping`, en dos niveles y con `maxBy`), `partitioningBy`, `toMap` (con función de combinación), `joining`, `summarizingDouble` y `teeing` |
| `advanced` | `AdvancedStreamsDemo` | `flatMap`, `mapMulti`, `takeWhile`/`dropWhile`, `iterate`/`generate`, streams primitivos, `Optional`, streams paralelos y el error de reutilizar un stream |
| `model` | `Employee`, `SampleData` | Un `record` y los datos de ejemplo |

## Guía rápida

| Quiero... | Uso |
|-----------|-----|
| Quedarme con algunos elementos | `filter` |
| Transformar cada elemento | `map` (o `mapToInt` para evitar el boxing) |
| Aplanar listas de listas | `flatMap` |
| Agrupar por una clave | `Collectors.groupingBy` |
| Separar en cumplen / no cumplen | `Collectors.partitioningBy` |
| Convertir en un mapa | `Collectors.toMap` (con función de combinación si las claves pueden repetirse) |
| Unir textos | `Collectors.joining` |
| Estadísticas (n, suma, mínimo, media, máximo) | `summarizingDouble` / `summaryStatistics` |
| Calcular dos cosas en una pasada | `Collectors.teeing` |
| Obtener una lista inmutable | `Stream.toList()` |

## Requisitos

- JDK 21 y Maven 3.9 o superior.

## Compilar y ejecutar

```bash
mvn compile exec:java

# o como JAR ejecutable (fat jar)
mvn clean package
java -jar target/mvn-streams-1.0.0-SNAPSHOT-launcher.jar
```

## Buenas prácticas

- No modifiques variables externas dentro del stream: usa `collect` o `reduce`.
- Usa `.parallel()` solo con muchos datos y operaciones costosas; mídelo antes.
- Prefiere los streams primitivos (`IntStream`...) cuando trabajes con números.
- Un `for` sencillo sigue siendo mejor cuando el stream no aporta claridad.

## Licencia

Código de ejemplo de uso libre con fines educativos.
