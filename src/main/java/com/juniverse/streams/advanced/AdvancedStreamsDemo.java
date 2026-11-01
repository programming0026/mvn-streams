package com.juniverse.streams.advanced;

import com.juniverse.streams.model.Employee;
import com.juniverse.streams.model.SampleData;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import java.util.logging.Logger;
import com.juniverse.streams.AppLogger;
/**
 * Streams avanzados: flatMap, mapMulti, takeWhile/dropWhile, iterate/generate, streams paralelos y errores comunes.
 */
public final class AdvancedStreamsDemo {
    private static final Logger LOGGER = AppLogger.get(AdvancedStreamsDemo.class);


    private AdvancedStreamsDemo() {
    }

    /** Ejecuta la demostración. */
    public static void run() {
        // flatMap: "aplana" un stream de colecciones en un solo stream
        List<List<Integer>> lists = List.of(List.of(1, 2), List.of(3), List.of(4, 5, 6));
        LOGGER.info("flatMap (listas -> números): " + lists.stream().flatMap(List::stream).toList());
        LOGGER.info("flatMap (palabras de frases): " + Stream.of("hola mundo", "java 21")
                .flatMap(f -> Arrays.stream(f.split(" "))).toList());

        // mapMulti (Java 16): como flatMap pero sin crear streams intermedios
        LOGGER.info("mapMulti (cada número y su doble si es par): " + Stream.of(1, 2, 3, 4)
                .<Integer>mapMulti((n, sink) -> {
                    sink.accept(n);
                    if (n % 2 == 0) {
                        sink.accept(n * 2);
                    }
                }).toList());

        // takeWhile / dropWhile (Java 9): sobre datos ordenados
        List<Integer> sorted = List.of(1, 3, 5, 8, 9, 11);
        LOGGER.info("takeWhile(< 8): " + sorted.stream().takeWhile(n -> n < 8).toList()
                + ", dropWhile(< 8): " + sorted.stream().dropWhile(n -> n < 8).toList());

        // iterate y generate: streams infinitos, hay que limitarlos
        LOGGER.info("iterate (potencias de 2): " + Stream.iterate(1, n -> n * 2).limit(8).toList());
        LOGGER.info("iterate con condición (Java 9): " + Stream.iterate(1, n -> n < 100, n -> n * 3).toList());

        // streams de tipos primitivos
        LOGGER.info("IntStream.range + boxed + collect: " + IntStream.range(0, 5).boxed().collect(Collectors.toList()));
        LOGGER.info("mapToInt + summaryStatistics (edades): " + SampleData.employees().stream()
                .mapToInt(Employee::age).summaryStatistics());

        // Optional encadenado
        String city = SampleData.employees().stream().filter(e -> e.age() > 60).findAny()
                .map(Employee::city).orElse("sin resultados");
        LOGGER.info("Optional.map/orElse: " + city);

        // paralelo: solo vale la pena con muchos datos y operaciones costosas
        long primes = IntStream.rangeClosed(2, 200_000).parallel().filter(AdvancedStreamsDemo::isPrime).count();
        LOGGER.info("parallel (primos hasta 200000): " + primes);

        // Error clásico: reutilizar un stream
        Stream<String> once = Stream.of("a", "b");
        once.count();
        try {
            once.count();
        } catch (IllegalStateException e) {
            LOGGER.info("Un stream solo se consume una vez: " + e.getMessage());
        }
        LOGGER.info("Consejo: no modifiques variables externas dentro de un stream (usa collect/reduce).");
    }

    private static boolean isPrime(int n) {
        return n > 1 && IntStream.rangeClosed(2, (int) Math.sqrt(n)).noneMatch(d -> n % d == 0);
    }
}
