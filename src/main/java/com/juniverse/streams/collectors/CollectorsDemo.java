package com.juniverse.streams.collectors;

import com.juniverse.streams.model.Employee;
import com.juniverse.streams.model.SampleData;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

/**
 * {@code Collectors}: groupingBy, partitioningBy, toMap, joining, counting, averaging, summarizing, mapping y teeing.
 */
public final class CollectorsDemo {

    private CollectorsDemo() {
    }

    /** Ejecuta la demostración. */
    public static void run() {
        List<Employee> staff = SampleData.employees();

        // groupingBy: agrupa en un Map<clave, List<elementos>>
        Map<String, List<String>> porDepartamento = staff.stream()
                .collect(Collectors.groupingBy(Employee::department, TreeMap::new,
                        Collectors.mapping(Employee::name, Collectors.toList())));
        System.out.println("groupingBy + mapping: " + porDepartamento);

        // groupingBy con un collector "downstream"
        System.out.println("groupingBy + counting:  " + new TreeMap<>(staff.stream()
                .collect(Collectors.groupingBy(Employee::city, Collectors.counting()))));
        System.out.println("groupingBy + averagingDouble (salario medio): " + new TreeMap<>(staff.stream()
                .collect(Collectors.groupingBy(Employee::department, Collectors.averagingDouble(Employee::salary)))));
        System.out.println("groupingBy + summingDouble (nómina): " + new TreeMap<>(staff.stream()
                .collect(Collectors.groupingBy(Employee::department, Collectors.summingDouble(Employee::salary)))));

        // agrupación en dos niveles
        Map<String, Map<String, Long>> dosNiveles = staff.stream().collect(Collectors.groupingBy(Employee::department,
                TreeMap::new, Collectors.groupingBy(Employee::city, TreeMap::new, Collectors.counting())));
        System.out.println("groupingBy en dos niveles (departamento -> ciudad -> cuántos): " + dosNiveles);

        // el mejor pagado de cada departamento
        Map<String, String> mejorPagado = staff.stream().collect(Collectors.groupingBy(Employee::department, TreeMap::new,
                Collectors.collectingAndThen(Collectors.maxBy(java.util.Comparator.comparingDouble(Employee::salary)),
                        o -> o.get().name())));
        System.out.println("maxBy dentro de groupingBy: " + mejorPagado);

        // partitioningBy: siempre dos grupos (true / false)
        System.out.println("partitioningBy (salario >= 30000): " + staff.stream()
                .collect(Collectors.partitioningBy(e -> e.salary() >= 30_000, Collectors.mapping(Employee::name, Collectors.toList()))));

        // toMap: clave única (si hay repetidas, hay que dar una función para combinarlas)
        System.out.println("toMap nombre -> salario (primeros): " + new TreeMap<>(staff.stream()
                .collect(Collectors.toMap(Employee::name, Employee::salary))).firstEntry());
        System.out.println("toMap con merge (ciudad -> nómina): " + new TreeMap<>(staff.stream()
                .collect(Collectors.toMap(Employee::city, Employee::salary, Double::sum))));

        // joining
        System.out.println("joining: " + staff.stream().map(Employee::name).collect(Collectors.joining(", ", "{", "}")));

        // summarizingDouble: count, sum, min, average y max en una pasada
        DoubleSummaryStatistics stats = staff.stream().collect(Collectors.summarizingDouble(Employee::salary));
        System.out.printf("summarizingDouble: n=%d, min=%.0f, media=%.0f, max=%.0f%n",
                stats.getCount(), stats.getMin(), stats.getAverage(), stats.getMax());

        // teeing (Java 12): dos collectors en una sola pasada y se combinan
        String rango = staff.stream().collect(Collectors.teeing(
                Collectors.minBy(java.util.Comparator.comparingInt(Employee::age)),
                Collectors.maxBy(java.util.Comparator.comparingInt(Employee::age)),
                (min, max) -> min.get().name() + " (" + min.get().age() + ") y " + max.get().name() + " (" + max.get().age() + ")"));
        System.out.println("teeing (más joven y mayor): " + rango);

        // toUnmodifiableList / Stream.toList (Java 16)
        List<String> inmutable = staff.stream().map(Employee::city).distinct().toList();
        System.out.println("Stream.toList() es inmutable: " + inmutable);
    }
}
