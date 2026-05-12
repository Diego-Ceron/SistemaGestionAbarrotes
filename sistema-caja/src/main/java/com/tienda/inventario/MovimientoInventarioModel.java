package com.tienda.inventario;

public class MovimientoInventarioModel {
    private String fecha; // ISO datetime string
    private String tipo;
    private int cantidad;
    private int productoId;
    private String nota;

    // Getters
    public String getFecha() { return fecha; }
    public String getTipo() { return tipo; }
    public int getCantidad() { return cantidad; }
    public int getProductoId() { return productoId; }
    public String getNota() { return nota; }

    // Setters
    public void setFecha(String fecha) { this.fecha = fecha; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }
    public void setNombre(int productoId) { this.productoId = productoId; }
    public void setNota(String nota) { this.nota = nota; }

    public void setProductoId(int productoId) {
    }
}