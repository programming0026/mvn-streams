package com.juniverse.streams.model;

import java.util.List;

/** Datos de ejemplo. */
public final class SampleData {

    private SampleData() {
    }

    /**
     * Lista fija de empleados.
     *
     * @return empleados de ejemplo
     */
    public static List<Employee> employees() {
        return List.of(
                new Employee("Ana", "Ingeniería", "CDMX", 42_000, 31),
                new Employee("Luis", "Ingeniería", "Guadalajara", 38_000, 27),
                new Employee("María", "Ingeniería", "CDMX", 55_000, 40),
                new Employee("Carlos", "Ventas", "Monterrey", 25_000, 35),
                new Employee("Elena", "Ventas", "CDMX", 28_000, 29),
                new Employee("Jorge", "Ventas", "Monterrey", 22_000, 23),
                new Employee("Sofía", "Recursos Humanos", "Guadalajara", 30_000, 45),
                new Employee("Pablo", "Recursos Humanos", "CDMX", 27_000, 38));
    }
}
