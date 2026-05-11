package main.java.com.tienda.promocion;

public class PromocionModel {
    private int id;
    private String descripcion;
    private double porcentajeDescuento;

    //Getters
    public int getId() { return id; }
    public String getDescripcion() { return descripcion; }
    public double getPorcentajeDescuento() { return porcentajeDescuento; }

    //Setters
    public void setId(int id) { this.id = id; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public void setPorcentajeDescuento(double porcentajeDescuento) {
        this.porcentajeDescuento = porcentajeDescuento;
    }

    public double calcularDescuento(double monto) {
        return monto * (this.porcentajeDescuento / 100);
    }
}
