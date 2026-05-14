package com.tienda.promocion;

import java.util.List;
import java.util.Scanner;

public class PromocionView {

    public PromocionModel pedirDatosPromocion(Scanner scanner) {
        PromocionModel p = new PromocionModel();
        System.out.print("Descripción: "); p.setDescripcion(scanner.nextLine().trim());
        System.out.print("Porcentaje descuento (ej. 10 para 10%): ");
        try {
            p.setPorcentajeDescuento(Double.parseDouble(scanner.nextLine().trim()));
        } catch (NumberFormatException e) {
            System.out.println("Porcentaje inválido. Operación cancelada.");
            return null;
        }
        return p;
    }

    public void mostrarPromocion(PromocionModel p) {
        if (p == null) { System.out.println("Promoción no encontrada."); return; }
        System.out.println("===== PROMOCIÓN =====");
        System.out.println("ID: " + p.getId());
        System.out.println("Descripción: " + p.getDescripcion());
        System.out.printf("Descuento: %.2f%%\n", p.getPorcentajeDescuento());
        System.out.println("=====================");
    }

    public void mostrarPromociones(List<PromocionModel> lista) {
        if (lista == null || lista.isEmpty()) { System.out.println("No hay promociones."); return; }
        System.out.println("===== LISTA DE PROMOCIONES =====");
        for (PromocionModel p : lista) {
            System.out.printf("ID:%d  %s  (%.2f%%)%n", p.getId(), p.getDescripcion(), p.getPorcentajeDescuento());
        }
        System.out.println("================================");
    }

    public int pedirIdPromocion(Scanner scanner) {
        System.out.print("ID promoción: ");
        try { return Integer.parseInt(scanner.nextLine().trim()); } catch (NumberFormatException e) {
            System.out.println("ID inválido."); return -1; }
    }

    public void mostrarMenu(Scanner scanner, PromocionController controller) {
        boolean running = true;
        while (running) {
            System.out.println("\n--- Promociones ---");
            System.out.println("1) Listar promociones");
            System.out.println("2) Ver promoción por producto");
            System.out.println("3) Agregar promoción");
            System.out.println("4) Actualizar promoción");
            System.out.println("5) Eliminar promoción");
            System.out.println("0) Volver");
            System.out.print("Opción: ");
            String opt = scanner.nextLine().trim();
            switch (opt) {
                case "1" -> {
                    List<PromocionModel> lista = controller.listarPromociones();
                    mostrarPromociones(lista);
                }
                case "2" -> {
                    System.out.print("ID producto: ");
                    try {
                        int id = Integer.parseInt(scanner.nextLine().trim());
                        mostrarPromocion(controller.buscarPorProducto(id));
                    } catch (NumberFormatException e) { System.out.println("ID inválido"); }
                }
                case "3" -> {
                    PromocionModel p = pedirDatosPromocion(scanner);
                    if (p != null) controller.agregarPromocion(p);
                }
                case "4" -> {
                    int id = pedirIdPromocion(scanner);
                    if (id > 0) {
                        System.out.print("Descripción nueva: ");
                        String desc = scanner.nextLine().trim();
                        System.out.print("Porcentaje nuevo: ");
                        try {
                            double por = Double.parseDouble(scanner.nextLine().trim());
                            PromocionModel pm = new PromocionModel(); pm.setId(id); pm.setDescripcion(desc); pm.setPorcentajeDescuento(por);
                            controller.actualizarPromocion(pm);
                        } catch (NumberFormatException e) { System.out.println("Porcentaje inválido"); }
                    }
                }
                case "5" -> {
                    int id = pedirIdPromocion(scanner);
                    if (id > 0) controller.eliminarPromocion(id);
                }
                case "0" -> running = false;
                default -> System.out.println("Opción inválida");
            }
        }
    }
}
