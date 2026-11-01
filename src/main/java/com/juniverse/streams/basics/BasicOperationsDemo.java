package com.juniverse.streams.basics;

import com.juniverse.streams.model.Employee;
import com.juniverse.streams.model.SampleData;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import java.util.logging.Logger;
import com.juniverse.streams.AppLogger;
/**
 * Operaciones básicas de {@code Stream}: filter, map, sorted, distinct, limit, skip, reduce y operaciones terminales.
 *
 * <p>Un stream es una <b>tubería perezosa</b>: las operaciones intermedias (filter, map...) no hacen nada hasta que
 * una operación terminal (collect, forEach, count, reduce...) pide el resultado. Un stream solo se puede consumir una vez.</p>
 */
public final class BasicOperationsDemo {
    private static final Logger LOGGER = AppLogger.get(BasicOperationsDemo.class);


    private BasicOperationsDemo() {
    }

    /** Ejecuta la demostración. */
    public static void run() {
        List<Employee> staff = SampleData.employees();

        // filter: se queda con los elementos que cumplen la condición
        List<String> engineers = staff.stream()
                .filter(e -> e.department().equals("Ingeniería"))
                .map(Employee::name)
                .toList();
        LOGGER.info("filter + map (nombres de Ingeniería): " + engineers);

        // map: transforma cada elemento
        List<Double> withRaise = staff.stream().map(e -> e.salary() * 1.10).limit(3).toList();
        LOGGER.info("map (salario +10%, primeros 3): " + withRaise);

        // sorted con Comparator, limit y skip
        LOGGER.info("sorted por salario descendente, top 3: " + staff.stream()
                .sorted((a, b) -> Double.compare(b.salary(), a.salary()))
                .limit(3).map(Employee::name).toList());
        LOGGER.info("skip(5) (los demás): " + staff.stream().skip(5).map(Employee::name).toList());

        // distinct
        LOGGER.info("distinct (ciudades): " + staff.stream().map(Employee::city).distinct().toList());

        // reduce
        double total = staff.stream().map(Employee::salary).reduce(0.0, Double::sum);
        LOGGER.info(String.format("reduce (nómina total): $%,.2f", total));

        // búsquedas y comprobaciones (cortocircuito: se detienen en cuanto saben la respuesta)
        Optional<Employee> firstOlder = staff.stream().filter(e -> e.age() > 40).findFirst();
        LOGGER.info("findFirst (primero mayor de 40): " + firstOlder.map(Employee::name).orElse("nadie"));
        LOGGER.info("anyMatch salario > 50000: " + staff.stream().anyMatch(e -> e.salary() > 50_000));
        LOGGER.info("allMatch edad >= 18:      " + staff.stream().allMatch(e -> e.age() >= 18));
        LOGGER.info("noneMatch ciudad 'Lima':  " + staff.stream().noneMatch(e -> e.city().equals("Lima")));
        LOGGER.info("count (de Ventas): " + staff.stream().filter(e -> e.department().equals("Ventas")).count());

        // max / min con Comparator
        LOGGER.info("max por edad: " + staff.stream().max(java.util.Comparator.comparingInt(Employee::age)).get().name());

        // streams de números: IntStream evita el boxing y trae sum, average, etc.
        LOGGER.info("IntStream.rangeClosed(1, 5): suma=" + IntStream.rangeClosed(1, 5).sum()
                + ", promedio=" + IntStream.rangeClosed(1, 5).average().orElse(0));
        LOGGER.info("Stream.of + collect(joining): " + Stream.of("a", "b", "c").collect(Collectors.joining("-", "[", "]")));

        // laziness: peek muestra el orden real de ejecución (un elemento a la vez por toda la tubería)
        LOGGER.info("Ejecución perezosa:");
        Stream.of("x", "yy", "zzz").peek(s -> LOGGER.info("  entra " + s)).filter(s -> s.length() > 1)
                .peek(s -> LOGGER.info("  pasa  " + s)).findFirst();
    }
}
