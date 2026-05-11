package main.java.com.tienda.inventario;

import java.util.Date;

public class MovimientoInventarioModel {
    private int id;
    private Date fecha;
    private String tipo;
    private int cantidad;

    //Getters
    public int getId() { return id; }
    public Date getFecha() { return fecha; }
    public String getTipo() { return tipo; }
    public int getCantidad() { return cantidad; }

    //Setters
    public void setId(int id) { this.id = id; }
    public void setFecha(Date fecha) { this.fecha = fecha; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }
}