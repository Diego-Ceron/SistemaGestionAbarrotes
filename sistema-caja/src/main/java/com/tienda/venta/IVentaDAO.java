package com.tienda.venta;

import com.tienda.reporte.ReporteModel;

public interface IVentaDAO {
    void registrar(VentaModel v);
    void historial();
    ReporteModel generarReporte(String periodo);
    void historialPorCliente(int clienteId);

    void registrarVenta(VentaModel v);
}
