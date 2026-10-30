package com.juniverse.streams.advanced;

import com.juniverse.streams.model.Employee;
import com.juniverse.streams.model.SampleData;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Streams avanzados: flatMap, mapMulti, takeWhile/dropWhile, iterate/generate, streams paralelos y errores comunes.
 */
public final class AdvancedStreamsDemo {

    private AdvancedStreamsDemo() {
    }

    /** Ejecuta la demostración. */
    public static void run() {
        // flatMap: "aplana" un stream de colecciones en un solo stream
        List<List<Integer>> listas = List.of(List.of(1, 2), List.of(3), List.of(4, 5, 6));
        System.out.println("flatMap (listas -> números): " + listas.stream().flatMap(List::stream).toList());
        System.out.println("flatMap (palabras de frases): " + Stream.of("hola mundo", "java 21")
                .flatMap(f -> Arrays.stream(f.split(" "))).toList());

        // mapMulti (Java 16): como flatMap pero sin crear streams intermedios
        System.out.println("mapMulti (cada número y su doble si es par): " + Stream.of(1, 2, 3, 4)
                .<Integer>mapMulti((n, sink) -> {
                    sink.accept(n);
                    if (n % 2 == 0) {
                        sink.accept(n * 2);
                    }
                }).toList());

        // takeWhile / dropWhile (Java 9): sobre datos ordenados
        List<Integer> ordenados = List.of(1, 3, 5, 8, 9, 11);
        System.out.println("takeWhile(< 8): " + ordenados.stream().takeWhile(n -> n < 8).toList()
                + ", dropWhile(< 8): " + ordenados.stream().dropWhile(n -> n < 8).toList());

        // iterate y generate: streams infinitos, hay que limitarlos
        System.out.println("iterate (potencias de 2): " + Stream.iterate(1, n -> n * 2).limit(8).toList());
        System.out.println("iterate con condición (Java 9): " + Stream.iterate(1, n -> n < 100, n -> n * 3).toList());

        // streams de tipos primitivos
        System.out.println("IntStream.range + boxed + collect: " + IntStream.range(0, 5).boxed().collect(Collectors.toList()));
        System.out.println("mapToInt + summaryStatistics (edades): " + SampleData.employees().stream()
                .mapToInt(Employee::age).summaryStatistics());

        // Optional encadenado
        String ciudad = SampleData.employees().stream().filter(e -> e.age() > 60).findAny()
                .map(Employee::city).orElse("sin resultados");
        System.out.println("Optional.map/orElse: " + ciudad);

        // paralelo: solo vale la pena con muchos datos y operaciones costosas
        long primos = IntStream.rangeClosed(2, 200_000).parallel().filter(AdvancedStreamsDemo::isPrime).count();
        System.out.println("parallel (primos hasta 200000): " + primos);

        // Error clásico: reutilizar un stream
        Stream<String> unaVez = Stream.of("a", "b");
        unaVez.count();
        try {
            unaVez.count();
        } catch (IllegalStateException e) {
            System.out.println("Un stream solo se consume una vez: " + e.getMessage());
        }
        System.out.println("Consejo: no modifiques variables externas dentro de un stream (usa collect/reduce).");
    }

    private static boolean isPrime(int n) {
        return n > 1 && IntStream.rangeClosed(2, (int) Math.sqrt(n)).noneMatch(d -> n % d == 0);
    }
}
