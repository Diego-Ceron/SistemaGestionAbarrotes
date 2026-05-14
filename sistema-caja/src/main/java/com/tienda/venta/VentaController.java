package com.tienda.venta;

import com.tienda.inventario.MovimientoDAO;
import com.tienda.inventario.MovimientoInventarioModel;
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
        // Por cada producto vendido, registrar un movimiento de tipo SALIDA para ajustar inventario
        MovimientoDAO movDao = new MovimientoDAO();
        for (ProductoModel p : v.getProductos()) {
            MovimientoInventarioModel m = new MovimientoInventarioModel();
            m.setProductoId(p.getId());
            m.setTipo("SALIDA");
            m.setCantidad(p.getCantidad());
            m.setFecha(java.time.LocalDateTime.now().toString());
            movDao.registrar(m);
        }
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
                            " = $" + String.format("%.2f", p.getPrecio() * p.getCantidad())
            );
        }

        System.out.println("Total: $" + String.format("%.2f", calcularTotal(v)));
        if (v.getMetodoPago() != null) {
            System.out.println("Método de pago: " + v.getMetodoPago());
            if (v.getMetodoPago().equalsIgnoreCase("Efectivo")) {
                System.out.println("Monto recibido: $" + String.format("%.2f", v.getMontoPagado()));
                System.out.println("Cambio: $" + String.format("%.2f", v.getCambio()));
            } else if (v.getMetodoPago().equalsIgnoreCase("Tarjeta")) {
                System.out.println("Tarjeta: " + v.getNumeroTarjeta());
            }
        }
        System.out.println("==================");
    }
}