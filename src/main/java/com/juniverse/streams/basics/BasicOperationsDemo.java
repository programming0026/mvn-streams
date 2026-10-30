package com.juniverse.streams.basics;

import com.juniverse.streams.model.Employee;
import com.juniverse.streams.model.SampleData;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Operaciones básicas de {@code Stream}: filter, map, sorted, distinct, limit, skip, reduce y operaciones terminales.
 *
 * <p>Un stream es una <b>tubería perezosa</b>: las operaciones intermedias (filter, map...) no hacen nada hasta que
 * una operación terminal (collect, forEach, count, reduce...) pide el resultado. Un stream solo se puede consumir una vez.</p>
 */
public final class BasicOperationsDemo {

    private BasicOperationsDemo() {
    }

    /** Ejecuta la demostración. */
    public static void run() {
        List<Employee> staff = SampleData.employees();

        // filter: se queda con los elementos que cumplen la condición
        List<String> ingenieros = staff.stream()
                .filter(e -> e.department().equals("Ingeniería"))
                .map(Employee::name)
                .toList();
        System.out.println("filter + map (nombres de Ingeniería): " + ingenieros);

        // map: transforma cada elemento
        List<Double> conAumento = staff.stream().map(e -> e.salary() * 1.10).limit(3).toList();
        System.out.println("map (salario +10%, primeros 3): " + conAumento);

        // sorted con Comparator, limit y skip
        System.out.println("sorted por salario descendente, top 3: " + staff.stream()
                .sorted((a, b) -> Double.compare(b.salary(), a.salary()))
                .limit(3).map(Employee::name).toList());
        System.out.println("skip(5) (los demás): " + staff.stream().skip(5).map(Employee::name).toList());

        // distinct
        System.out.println("distinct (ciudades): " + staff.stream().map(Employee::city).distinct().toList());

        // reduce
        double total = staff.stream().map(Employee::salary).reduce(0.0, Double::sum);
        System.out.printf("reduce (nómina total): $%,.2f%n", total);

        // búsquedas y comprobaciones (cortocircuito: se detienen en cuanto saben la respuesta)
        Optional<Employee> primerMayor = staff.stream().filter(e -> e.age() > 40).findFirst();
        System.out.println("findFirst (primero mayor de 40): " + primerMayor.map(Employee::name).orElse("nadie"));
        System.out.println("anyMatch salario > 50000: " + staff.stream().anyMatch(e -> e.salary() > 50_000));
        System.out.println("allMatch edad >= 18:      " + staff.stream().allMatch(e -> e.age() >= 18));
        System.out.println("noneMatch ciudad 'Lima':  " + staff.stream().noneMatch(e -> e.city().equals("Lima")));
        System.out.println("count (de Ventas): " + staff.stream().filter(e -> e.department().equals("Ventas")).count());

        // max / min con Comparator
        System.out.println("max por edad: " + staff.stream().max(java.util.Comparator.comparingInt(Employee::age)).get().name());

        // streams de números: IntStream evita el boxing y trae sum, average, etc.
        System.out.println("IntStream.rangeClosed(1, 5): suma=" + IntStream.rangeClosed(1, 5).sum()
                + ", promedio=" + IntStream.rangeClosed(1, 5).average().orElse(0));
        System.out.println("Stream.of + collect(joining): " + Stream.of("a", "b", "c").collect(Collectors.joining("-", "[", "]")));

        // laziness: peek muestra el orden real de ejecución (un elemento a la vez por toda la tubería)
        System.out.println("Ejecución perezosa:");
        Stream.of("x", "yy", "zzz").peek(s -> System.out.println("  entra " + s)).filter(s -> s.length() > 1)
                .peek(s -> System.out.println("  pasa  " + s)).findFirst();
    }
}
