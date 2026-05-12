package com.tienda.venta;

import java.util.ArrayList;
import java.util.List;

public class VentaDAO implements IVentaDAO {

    private final List<VentaModel> ventas = new ArrayList<>();
    private int nextId = 1;

    @Override
    public void registrar(VentaModel v) {
        // alias a registrarVenta
        registrarVenta(v);
    }

    @Override
    public void historial() {
        for (VentaModel v : ventas) {
            System.out.println("Venta ID:" + v.getId() + " Cliente:" + v.getCliente() + " Total:" + v.getTotal());
        }
    }

    @Override
    public com.tienda.reporte.ReporteModel generarReporte(String periodo) {
        return null; // no implementado
    }

    @Override
    public void historialPorCliente(int clienteId) {
        // no implementado: requiere relación con cliente ID en VentaModel
    }

    @Override
    public void registrarVenta(VentaModel v) {
        v.setId(nextId++);
        ventas.add(v);
    }

    // Helper para listar en memoria
    public List<VentaModel> listarTodos() {
        return new ArrayList<>(ventas);
    }
}
