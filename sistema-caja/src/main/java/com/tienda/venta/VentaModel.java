package com.tienda.venta;

import java.util.Date;

import com.tienda.producto.ProductoModel;

public class VentaModel {
    private int id;
    private Date fecha;
    private double total;
    private ProductoModel[] productos;
    private String cliente; // nombre del cliente

    // Getters
    public int getId() { return id; }
    public Date getFecha() { return fecha; }
    public double getTotal() { return total; }
    public ProductoModel[] getProductos() { return productos != null ? productos : new ProductoModel[0]; }
    public String getCliente() { return cliente; }

    // Setters
    public void setId(int id) { this.id = id; }
    public void setFecha(Date fecha) { this.fecha = fecha; }
    public void setTotal(double total) { this.total = total; }
    public void setProductos(ProductoModel[] productos) { this.productos = productos; }
    public void setCliente(String cliente) { this.cliente = cliente; }
}