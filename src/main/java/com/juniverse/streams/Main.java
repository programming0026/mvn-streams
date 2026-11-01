package com.juniverse.streams;

import com.juniverse.streams.advanced.AdvancedStreamsDemo;
import com.juniverse.streams.basics.BasicOperationsDemo;
import com.juniverse.streams.collectors.CollectorsDemo;

import java.util.logging.Logger;
/**
 * Guía práctica de la API de Streams de Java: operaciones básicas, collectors y técnicas avanzadas.
 */
public final class Main {
    private static final Logger LOGGER = AppLogger.get(Main.class);


    private Main() {
    }

    private static void title(String text) {
        LOGGER.info("\n=========== " + text + " ===========");
    }

    /**
     * Punto de entrada.
     *
     * @param args no se usan
     */
    public static void main(String[] args) {
        // Salida en UTF-8 para que se vean bien los acentos en cualquier consola
        System.setOut(new java.io.PrintStream(System.out, true, java.nio.charset.StandardCharsets.UTF_8));
        title("OPERACIONES BÁSICAS: filter, map, sorted, reduce...");
        BasicOperationsDemo.run();
        title("COLLECTORS: groupingBy, partitioningBy, toMap, teeing...");
        CollectorsDemo.run();
        title("AVANZADO: flatMap, mapMulti, takeWhile, iterate, parallel");
        AdvancedStreamsDemo.run();
    }
}
