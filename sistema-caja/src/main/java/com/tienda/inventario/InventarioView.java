package com.tienda.inventario;

import java.util.List;
import java.util.Scanner;

import com.tienda.producto.ProductoModel;

public class InventarioView {

    // Muestra la lista general de productos en inventario
    public void mostrarProductos(List<ProductoModel> productos) {
        if (productos == null || productos.isEmpty()) {
            System.out.println("No hay productos registrados en el inventario.");
            return;
        }
        System.out.println("================= INVENTARIO =================");
        System.out.printf("%-5s %-20s %-10s %-8s %-12s %-15s%n",
                "ID", "Nombre", "Precio", "Stock", "Código", "Vencimiento");
        System.out.println("---------------------------------------------------------------");
        for (ProductoModel p : productos) {
            System.out.printf("%-5d %-20s $%-9.2f %-8d %-12s %-15s%n",
                    p.getId(),
                    p.getNombre(),
                    p.getPrecio(),
                    p.getCantidad(),
                    p.getCodigo() == null ? "-" : p.getCodigo(),
                    p.getVencimiento() == null ? "-" : p.getVencimiento());
        }
        System.out.println("==============================================");
    }

    // Muestra alerta cuando un producto tiene stock bajo
    public void mostrarAlertaStock(ProductoModel p, int umbral) {
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

    // Registrar movimiento mediante interacción por consola
    public void registrarMovimientoInteractivo(Scanner scanner) {
        MovimientoDAO movimientoDAO = new MovimientoDAO();
        MovimientoInventarioModel m = new MovimientoInventarioModel();
        try {
            System.out.print("ID producto: "); m.setProductoId(Integer.parseInt(scanner.nextLine().trim()));
            System.out.print("Tipo (ENTRADA/SALIDA/AJUSTE): "); m.setTipo(scanner.nextLine().trim());
            System.out.print("Cantidad: "); m.setCantidad(Integer.parseInt(scanner.nextLine().trim()));
            m.setFecha(java.time.LocalDateTime.now().toString());
            movimientoDAO.registrar(m);
            System.out.println("Movimiento registrado.");
        } catch (NumberFormatException e) {
            System.out.println("Entrada numérica inválida.");
        }
    }

    // Mensaje genérico
    public void mostrarMensaje(String mensaje) {
        System.out.println("[INFO] " + mensaje);
    }
}