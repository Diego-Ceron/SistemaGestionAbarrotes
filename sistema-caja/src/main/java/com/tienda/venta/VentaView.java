package com.tienda.venta;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import com.tienda.cliente.ClienteDAO;
import com.tienda.cliente.ClienteModel;
import com.tienda.producto.ProductoDAO;
import com.tienda.producto.ProductoModel;

public class VentaView {

    // Muestra un ticket/venta completa
    public void mostrarVenta(VentaModel v) {
        if (v == null) {
            System.out.println("Venta no encontrada.");
            return;
        }
        System.out.println("===== TICKET =====");
        System.out.println("ID: " + v.getId());
        System.out.println("Fecha: " + v.getFecha());
        System.out.println("Cliente: " + v.getCliente());
        System.out.println("Productos:");
        for (ProductoModel p : v.getProductos()) {
            System.out.printf(" - %s x%d  @ $%.2f  = $%.2f%n",
                    p.getNombre(), p.getCantidad(), p.getPrecio(), p.getPrecio() * p.getCantidad());
        }
        System.out.printf("Total: $%.2f%n", v.getTotal());
        System.out.println("==================");
    }

    // Recopila datos básicos para una venta: cliente + lista de productos (id + cantidad)
    public VentaModel pedirDatosVenta(Scanner scanner) {
        VentaModel v = new VentaModel();
        ProductoDAO productoDAO = new ProductoDAO();
        List<ProductoModel> seleccion = new ArrayList<>();

        // Pedir teléfono para verificar cliente registrado, o vacío para venta sin cliente
        System.out.print("Teléfono cliente (vacío para venta sin cliente): ");
        String telefono = scanner.nextLine().trim();
        if (telefono.isEmpty()) {
            v.setCliente(""); // venta sin cliente registrado
        } else {
            ClienteDAO cdao = new ClienteDAO();
            ClienteModel c = cdao.buscarPorTelefono(telefono);
            if (c != null) {
                v.setCliente(c.getNombre());
                System.out.println("Cliente verificado: " + c.getNombre());
            } else {
                System.out.println("No existe un cliente registrado con ese teléfono.");
                System.out.print("Continuar sin cliente? (s/n): ");
                String r = scanner.nextLine().trim().toLowerCase();
                if (!r.equals("s")) return null;
                v.setCliente("");
            }
        }

        while (true) {
            System.out.print("ID producto (vacío para terminar): ");
            String s = scanner.nextLine().trim();
            if (s.isEmpty()) break;
            int id;
            try { id = Integer.parseInt(s); } catch (NumberFormatException e) {
                System.out.println("ID inválido."); continue;
            }
            ProductoModel encontrado = productoDAO.listarTodos().stream()
                    .filter(p -> p.getId() == id).findFirst().orElse(null);
            if (encontrado == null) {
                System.out.println("Producto no encontrado para ID=" + id);
                continue;
            }
            System.out.print("Cantidad: ");
            int qty;
            try { qty = Integer.parseInt(scanner.nextLine().trim()); } catch (NumberFormatException e) {
                System.out.println("Cantidad inválida."); continue;
            }
            ProductoModel copia = new ProductoModel();
            copia.setId(encontrado.getId());
            copia.setNombre(encontrado.getNombre());
            copia.setPrecio(encontrado.getPrecio());
            copia.setCantidad(qty);
            seleccion.add(copia);
        }

        v.setProductos(seleccion.toArray(new ProductoModel[0]));
        v.setFecha(new Date());
        double total = seleccion.stream().mapToDouble(p -> p.getPrecio() * p.getCantidad()).sum();
        v.setTotal(total);
        return v;
    }

    // Muestra una lista de ventas (resumen)
    public void mostrarHistorial(List<VentaModel> ventas) {
        if (ventas == null || ventas.isEmpty()) {
            System.out.println("No hay ventas registradas.");
            return;
        }
        System.out.println("===== HISTORIAL DE VENTAS =====");
        for (VentaModel v : ventas) {
            System.out.printf("ID:%d  Fecha:%s  Cliente:%s  Total:$%.2f%n",
                    v.getId(), v.getFecha(), v.getCliente(), v.getTotal());
        }
        System.out.println("===============================");
    }
}
