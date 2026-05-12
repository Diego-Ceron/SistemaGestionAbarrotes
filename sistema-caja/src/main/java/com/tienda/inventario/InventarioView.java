package com.tienda.inventario;

import java.util.List;

public class InventarioView {

    // Muestra la lista general de productos en inventario
    public void mostrarProductos(List<com.tienda.producto.ProductoModel> productos) {
        if (productos == null || productos.isEmpty()) {
            System.out.println("No hay productos registrados en el inventario.");
            return;
        }
        System.out.println("================= INVENTARIO =================");
        System.out.printf("%-5s %-20s %-10s %-8s %-15s%n",
                "ID", "Nombre", "Precio", "Stock", "Vencimiento");
        System.out.println("----------------------------------------------");
        for (com.tienda.producto.ProductoModel p : productos) {
            System.out.printf("%-5d %-20s $%-9.2f %-8d %-15s%n",
                    p.getId(),
                    p.getNombre(),
                    p.getPrecio(),
                    p.getCantidad(),
                    p.getVencimiento());
        }
        System.out.println("==============================================");
    }

    // Muestra alerta cuando un producto tiene stock bajo
    public void mostrarAlertaStock(com.tienda.producto.ProductoModel p, int umbral) {
        System.out.println("⚠ ALERTA DE STOCK ⚠");
        System.out.printf("El producto '%s' (ID: %d) tiene solo %d unidades disponibles (umbral mínimo: %d).%n",
                p.getNombre(), p.getId(), p.getCantidad(), umbral);
    }

    // Muestra el historial de movimientos de un producto
    public void mostrarHistorialMovimientos(int idProducto, List<MovimientoInventarioModel> movimientos) {
        System.out.println("===== HISTORIAL DE MOVIMIENTOS — Producto ID: " + idProducto + " =====");
        if (movimientos == null || movimientos.isEmpty()) {
            System.out.println("Sin movimientos registrados para este producto.");
        } else {
            System.out.printf("%-5s %-20s %-10s %-8s%n", "ID", "Fecha", "Tipo", "Cantidad");
            System.out.println("----------------------------------------------------");
            for (MovimientoInventarioModel m : movimientos) {
                System.out.printf("%-5d %-20s %-10s %-8d%n",
                        m.getId(),
                        m.getFecha(),
                        m.getTipo(),
                        m.getCantidad());
            }
        }
        System.out.println("====================================================");
    }

    // Mensaje genérico
    public void mostrarMensaje(String mensaje) {
        System.out.println("[INFO] " + mensaje);
    }
}