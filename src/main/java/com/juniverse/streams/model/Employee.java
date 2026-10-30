package com.juniverse.streams.model;

/**
 * Empleado: los datos de ejemplo con los que trabajan todas las demostraciones.
 *
 * @param name nombre
 * @param department departamento
 * @param city ciudad
 * @param salary salario mensual
 * @param age edad
 */
public record Employee(String name, String department, String city, double salary, int age) {
}
