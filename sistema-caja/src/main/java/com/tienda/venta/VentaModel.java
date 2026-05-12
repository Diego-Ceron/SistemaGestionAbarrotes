package com.tienda.venta;

import com.tienda.producto.ProductoModel;

import java.util.Date;

public class VentaModel {
    private int id;
    private Date fecha;
    private double total;

    //Getters
    public int getId() { return id; }
    public String getFecha() { return fecha.toString(); }
    public double getTotal() { return total; }

    //Setters
    public void setId(int id) { this.id = id; }
    public void setFecha(Date fecha) { this.fecha = fecha; }
    public void setTotal(double total) { this.total = total; }

    public ProductoModel[] getProductos() {
    }
}