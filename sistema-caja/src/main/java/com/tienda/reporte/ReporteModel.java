package com.tienda.reporte;

package modelo;

public class ReporteModel {

    private int idReporte;
    private String categoria;
    private String periodo;
    private double totalVentas;
    private int cantidadVentas;
    private String fechaGeneracion;

    // Constructor vacío
    public ReporteModel() {
    }

    // Constructor completo
    public ReporteModel(int idReporte, String categoria, String periodo,
                        double totalVentas, int cantidadVentas,
                        String fechaGeneracion) {

        this.idReporte = idReporte;
        this.categoria = categoria;
        this.periodo = periodo;
        this.totalVentas = totalVentas;
        this.cantidadVentas = cantidadVentas;
        this.fechaGeneracion = fechaGeneracion;
    }

    // GETTERS Y SETTERS

    public int getIdReporte() {
        return idReporte;
    }

    public void setIdReporte(int idReporte) {
        this.idReporte = idReporte;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getPeriodo() {
        return periodo;
    }

    public void setPeriodo(String periodo) {
        this.periodo = periodo;
    }

    public double getTotalVentas() {
        return totalVentas;
    }

    public void setTotalVentas(double totalVentas) {
        this.totalVentas = totalVentas;
    }

    public int getCantidadVentas() {
        return cantidadVentas;
    }

    public void setCantidadVentas(int cantidadVentas) {
        this.cantidadVentas = cantidadVentas;
    }

    public String getFechaGeneracion() {
        return fechaGeneracion;
    }

    public void setFechaGeneracion(String fechaGeneracion) {
        this.fechaGeneracion = fechaGeneracion;
    }

    @Override
    public String toString() {
        return "ReporteModel{" +
                "idReporte=" + idReporte +
                ", categoria='" + categoria + '\'' +
                ", periodo='" + periodo + '\'' +
                ", totalVentas=" + totalVentas +
                ", cantidadVentas=" + cantidadVentas +
                ", fechaGeneracion='" + fechaGeneracion + '\'' +
                '}';
    }
}