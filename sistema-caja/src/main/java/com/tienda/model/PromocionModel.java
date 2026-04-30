package com.tienda.model;

public class PromocionModel {
    private int id;
    private String descripcion;
    private double descuento; // Por si el diagrama implica un porcentaje
    private int idProducto;

    // Constructores
    public PromocionModel() {}

    // Getters y Setters (IntelliJ los puede generar con Alt+Insert)
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public int getIdProducto() { return idProducto; }
    public void setIdProducto(int idProducto) { this.idProducto = idProducto; }
}