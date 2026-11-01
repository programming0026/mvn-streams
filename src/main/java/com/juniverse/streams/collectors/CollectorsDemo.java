package com.juniverse.streams.collectors;

import com.juniverse.streams.model.Employee;
import com.juniverse.streams.model.SampleData;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import java.util.logging.Logger;
import com.juniverse.streams.AppLogger;
/**
 * {@code Collectors}: groupingBy, partitioningBy, toMap, joining, counting, averaging, summarizing, mapping y teeing.
 */
public final class CollectorsDemo {
    private static final Logger LOGGER = AppLogger.get(CollectorsDemo.class);


    private CollectorsDemo() {
    }

    /** Ejecuta la demostración. */
    public static void run() {
        List<Employee> staff = SampleData.employees();

        // groupingBy: agrupa en un Map<clave, List<elementos>>
        Map<String, List<String>> byDepartment = staff.stream()
                .collect(Collectors.groupingBy(Employee::department, TreeMap::new,
                        Collectors.mapping(Employee::name, Collectors.toList())));
        LOGGER.info("groupingBy + mapping: " + byDepartment);

        // groupingBy con un collector "downstream"
        LOGGER.info("groupingBy + counting:  " + new TreeMap<>(staff.stream()
                .collect(Collectors.groupingBy(Employee::city, Collectors.counting()))));
        LOGGER.info("groupingBy + averagingDouble (salario medio): " + new TreeMap<>(staff.stream()
                .collect(Collectors.groupingBy(Employee::department, Collectors.averagingDouble(Employee::salary)))));
        LOGGER.info("groupingBy + summingDouble (nómina): " + new TreeMap<>(staff.stream()
                .collect(Collectors.groupingBy(Employee::department, Collectors.summingDouble(Employee::salary)))));

        // agrupación en dos niveles
        Map<String, Map<String, Long>> twoLevels = staff.stream().collect(Collectors.groupingBy(Employee::department,
                TreeMap::new, Collectors.groupingBy(Employee::city, TreeMap::new, Collectors.counting())));
        LOGGER.info("groupingBy en dos niveles (departamento -> ciudad -> cuántos): " + twoLevels);

        // el mejor pagado de cada departamento
        Map<String, String> bestPaid = staff.stream().collect(Collectors.groupingBy(Employee::department, TreeMap::new,
                Collectors.collectingAndThen(Collectors.maxBy(java.util.Comparator.comparingDouble(Employee::salary)),
                        o -> o.get().name())));
        LOGGER.info("maxBy dentro de groupingBy: " + bestPaid);

        // partitioningBy: siempre dos grupos (true / false)
        LOGGER.info("partitioningBy (salario >= 30000): " + staff.stream()
                .collect(Collectors.partitioningBy(e -> e.salary() >= 30_000, Collectors.mapping(Employee::name, Collectors.toList()))));

        // toMap: clave única (si hay repetidas, hay que dar una función para combinarlas)
        LOGGER.info("toMap nombre -> salario (primeros): " + new TreeMap<>(staff.stream()
                .collect(Collectors.toMap(Employee::name, Employee::salary))).firstEntry());
        LOGGER.info("toMap con merge (ciudad -> nómina): " + new TreeMap<>(staff.stream()
                .collect(Collectors.toMap(Employee::city, Employee::salary, Double::sum))));

        // joining
        LOGGER.info("joining: " + staff.stream().map(Employee::name).collect(Collectors.joining(", ", "{", "}")));

        // summarizingDouble: count, sum, min, average y max en una pasada
        DoubleSummaryStatistics stats = staff.stream().collect(Collectors.summarizingDouble(Employee::salary));
        LOGGER.info(String.format("summarizingDouble: n=%d, min=%.0f, media=%.0f, max=%.0f",
                stats.getCount(), stats.getMin(), stats.getAverage(), stats.getMax()));

        // teeing (Java 12): dos collectors en una sola pasada y se combinan
        String range = staff.stream().collect(Collectors.teeing(
                Collectors.minBy(java.util.Comparator.comparingInt(Employee::age)),
                Collectors.maxBy(java.util.Comparator.comparingInt(Employee::age)),
                (min, max) -> min.get().name() + " (" + min.get().age() + ") y " + max.get().name() + " (" + max.get().age() + ")"));
        LOGGER.info("teeing (más joven y mayor): " + range);

        // toUnmodifiableList / Stream.toList (Java 16)
        List<String> immutable = staff.stream().map(Employee::city).distinct().toList();
        LOGGER.info("Stream.toList() es inmutable: " + immutable);
    }
}
