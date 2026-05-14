package com.tienda.inventario;

import java.util.List;
import java.util.Scanner;

import com.tienda.producto.ProductoModel;

public class InventarioView {

    // ===================== PRODUCTOS =====================

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

    // Recopila datos para agregar/editar un producto
    public ProductoModel pedirDatosProducto(Scanner scanner) {
        ProductoModel p = new ProductoModel();
        try {
            System.out.print("Nombre      : "); p.setNombre(scanner.nextLine().trim());
            System.out.print("Código      : "); p.setCodigo(scanner.nextLine().trim());
            System.out.print("Precio      : "); p.setPrecio(Double.parseDouble(scanner.nextLine().trim()));
            System.out.print("Cantidad    : "); p.setCantidad(Integer.parseInt(scanner.nextLine().trim()));
            System.out.print("Vencimiento (YYYY-MM-DD o vacío): ");
            String venc = scanner.nextLine().trim();
            p.setVencimiento(venc.isEmpty() ? null : venc);
            System.out.print("Descripción : "); p.setDescripcion(scanner.nextLine().trim());
            System.out.print("Categoría   : "); p.setCategoria(scanner.nextLine().trim());
            System.out.print("Proveedor   : "); p.setProveedor(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Entrada numérica inválida. Operación cancelada.");
            return null;
        }
        return p;
    }

    // Pide el ID de un producto
    public int pedirIdProducto(Scanner scanner) {
        System.out.print("ID del producto: ");
        try {
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("ID inválido.");
            return -1;
        }
    }

    // ===================== STOCK =====================

    public void mostrarAlertaStock(ProductoModel p, int umbral) {
        System.out.println("⚠️ ALERTA DE STOCK ⚠️");
        System.out.printf("El producto '%s' (ID: %d) tiene solo %d unidades disponibles (umbral mínimo: %d).%n",
                p.getNombre(), p.getId(), p.getCantidad(), umbral);
    }

    public void mostrarResumenAlertasStock(List<ProductoModel> bajos, int umbral) {
        if (bajos == null || bajos.isEmpty()) {
            System.out.println("✔️ Todos los productos tienen stock suficiente.");
            return;
        }
        System.out.println("========== ALERTAS DE STOCK BAJO ==========");
        for (ProductoModel p : bajos) mostrarAlertaStock(p, umbral);
        System.out.println("===========================================");
    }

    // ===================== MOVIMIENTOS =====================

    public void mostrarHistorialMovimientos(int idProducto, List<MovimientoInventarioModel> movimientos) {
        System.out.println("===== HISTORIAL DE MOVIMIENTOS — Producto ID: " + idProducto + " =====");
        if (movimientos == null || movimientos.isEmpty()) {
            System.out.println("Sin movimientos registrados para este producto.");
        } else {
            System.out.printf("%-5s %-20s %-10s %-8s%n", "ID", "Fecha", "Tipo", "Cantidad");
            System.out.println("----------------------------------------------------");
            for (MovimientoInventarioModel m : movimientos) {
                System.out.printf("%-5d %-20s %-10s %-8d%n",
                        m.getId(), m.getFecha(), m.getTipo(), m.getCantidad());
            }
        }
        System.out.println("====================================================");
    }

    // Recopila datos para registrar un movimiento — solo devuelve el modelo, NO llama al DAO
    public MovimientoInventarioModel pedirDatosMovimiento(Scanner scanner) {
        MovimientoInventarioModel m = new MovimientoInventarioModel();
        try {
            System.out.print("ID producto               : "); m.setProductoId(Integer.parseInt(scanner.nextLine().trim()));
            System.out.print("Tipo (ENTRADA/SALIDA/AJUSTE): "); m.setTipo(scanner.nextLine().trim().toUpperCase());
            System.out.print("Cantidad                  : "); m.setCantidad(Integer.parseInt(scanner.nextLine().trim()));
            m.setFecha(java.time.LocalDateTime.now().toString());
        } catch (NumberFormatException e) {
            System.out.println("Entrada numérica inválida. Operación cancelada.");
            return null;
        }
        return m;
    }

    // ===================== GENERAL =====================

    public void mostrarMensaje(String mensaje) {
        System.out.println("[INFO] " + mensaje);
    }

    public void mostrarError(String mensaje) {
        System.out.println("[ERROR] " + mensaje);
    }

    // Menú interactivo de inventario para agregar/editar/eliminar productos y registrar movimientos
    public void mostrarMenu(Scanner scanner, MovimientoController controller) {
        boolean running = true;
        while (running) {
            System.out.println("\n--- Inventario ---");
            System.out.println("1) Listar productos");
            System.out.println("2) Agregar producto");
            System.out.println("3) Editar producto");
            System.out.println("4) Eliminar producto");
            System.out.println("5) Registrar movimiento");
            System.out.println("6) Ver historial de movimientos por producto");
            System.out.println("0) Volver");
            System.out.print("Opción: ");
            String opt = scanner.nextLine().trim();
            switch (opt) {
                case "1" -> mostrarProductos(controller.listarProductos());
                case "2" -> {
                    ProductoModel p = pedirDatosProducto(scanner);
                    if (p != null) controller.agregarProducto(p);
                }
                case "3" -> {
                    int id = pedirIdProducto(scanner);
                    if (id > 0) {
                        ProductoModel existente = controller.listarProductos().stream().filter(x -> x.getId() == id).findFirst().orElse(null);
                        if (existente == null) { System.out.println("Producto no encontrado."); break; }
                        System.out.println("Ingrese nuevos datos (dejar vacío para mantener)");
                        System.out.print("Nombre (actual: " + existente.getNombre() + "): ");
                        String nombre = scanner.nextLine().trim(); if (!nombre.isEmpty()) existente.setNombre(nombre);
                        System.out.print("Precio (actual: " + existente.getPrecio() + "): ");
                        String precio = scanner.nextLine().trim(); if (!precio.isEmpty()) try { existente.setPrecio(Double.parseDouble(precio)); } catch (NumberFormatException e) {}
                        System.out.print("Cantidad (actual: " + existente.getCantidad() + "): ");
                        String cant = scanner.nextLine().trim(); if (!cant.isEmpty()) try { existente.setCantidad(Integer.parseInt(cant)); } catch (NumberFormatException e) {}
                        controller.actualizarProducto(existente);
                    }
                }
                case "4" -> {
                    int id = pedirIdProducto(scanner);
                    if (id > 0) controller.eliminarProducto(id);
                }
                case "5" -> {
                    MovimientoInventarioModel m = pedirDatosMovimiento(scanner);
                    if (m != null) controller.registrarMovimiento(m);
                }
                case "6" -> {
                    int id = pedirIdProducto(scanner);
                    if (id > 0) mostrarHistorialMovimientos(id, controller.verHistorialMovimientos(id));
                }
                case "0" -> running = false;
                default -> System.out.println("Opción inválida");
            }
        }
    }
}