package com.tienda.reporte;

import java.util.List;
import java.util.Scanner;

public class ReporteView {

    public void mostrarMenu(Scanner scanner, ReporteController controller) {
        boolean running = true;
        while (running) {
            System.out.println("\n--- Reportes ---");
            System.out.println("1) Ventas (diario/semanal/mensual/anual)");
            System.out.println("2) Inventario");
            System.out.println("3) Producto vendido por ID");
            System.out.println("4) Productos vendidos por categoría");
            System.out.println("0) Volver");
            System.out.print("Opción: ");
            String opt = scanner.nextLine().trim();
            switch (opt) {
                case "1" -> {
                    System.out.print("Tipo (diario/semanal/mensual/anual): ");
                    String tipo = scanner.nextLine().trim();
                    List<ReporteModel> reps = controller.ventasPorPeriodo(tipo);
                    System.out.println("Resultados para: " + tipo);
                    for (ReporteModel r : reps) {
                        System.out.println("Periodo=" + r.getPeriodo() + " | Cantidad=" + r.getCantidadVentas() + " | Total=" + r.getTotalVentas());
                    }
                }
                case "2" -> {
                    List<ReporteModel> inv = controller.reporteInventario();
                    System.out.println("Inventario (ID | Nombre | Cantidad | Categoria):");
                    for (ReporteModel r : inv) {
                        System.out.println(r.getIdReporte() + " | " + r.getCategoria() + " | " + r.getCantidadVentas() + " | " + r.getPeriodo());
                    }
                }
                case "3" -> {
                    System.out.print("ID producto: ");
                    try {
                        int id = Integer.parseInt(scanner.nextLine().trim());
                        ReporteModel r = controller.productoPorId(id);
                        if (r == null) {
                            System.out.println("No hay ventas para ese producto o no existe.");
                        } else {
                            System.out.println("Producto: " + r.getCategoria() + " | Total vendido: " + r.getCantidadVentas() + " | Total ventas: " + r.getTotalVentas());
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("ID inválido");
                    }
                }
                case "4" -> {
                    System.out.print("Categoría (texto parcial): ");
                    String cat = scanner.nextLine().trim();
                    List<ReporteModel> list = controller.productosPorCategoria(cat);
                    System.out.println("Productos vendidos en categoría (ID | Nombre | Cantidad | Total):");
                    for (ReporteModel r : list) {
                        System.out.println(r.getIdReporte() + " | " + r.getCategoria() + " | " + r.getCantidadVentas() + " | " + r.getTotalVentas());
                    }
                }
                case "0" -> running = false;
                default -> System.out.println("Opción inválida");
            }
        }
    }
}
