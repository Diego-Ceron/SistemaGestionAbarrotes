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
}
