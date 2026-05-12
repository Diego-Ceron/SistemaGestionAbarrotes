package com.tienda.venta;

import com.tienda.producto.ProductoModel;

public class VentaController {

    private IVentaDAO ventaDAO;

    // Constructor
    public VentaController(IVentaDAO ventaDAO) {
        this.ventaDAO = ventaDAO;
    }

    // Registrar venta
    public void registrarVenta(VentaModel v) {
        ventaDAO.registrarVenta(v);
    }

    // Calcular total de venta
    public double calcularTotal(VentaModel v) {

        double total = 0;

        for (ProductoModel p : v.getProductos()) {
            total += p.getPrecio() * p.getCantidad();
        }

        return total;
    }

    // Aplicar descuento
    public double aplicarDescuento(double total, double descuento) {
        return total - (total * descuento / 100);
    }

    // Generar ticket
    public void generarTicket(VentaModel v) {

        System.out.println("===== TICKET =====");
        System.out.println("Cliente: " + v.getCliente());

        for (ProductoModel p : v.getProductos()) {
            System.out.println(
                    p.getNombre() +
                            " x" + p.getCantidad() +
                            " = $" + (p.getPrecio() * p.getCantidad())
            );
        }

        System.out.println("Total: $" + calcularTotal(v));
        System.out.println("==================");
    }
}