package main.java.com.tienda.metodoPago;

import main.java.com.tienda.metodoPago.interfaz.MetodoPago;

public class PagoTarjeta implements MetodoPago {
    private String numTarjeta;

    public String getNumTarjeta() { return numTarjeta; }
    public void setNumTarjeta(String numTarjeta) { this.numTarjeta = numTarjeta; }

    @Override
    public boolean procesar(double monto) {
        if (numTarjeta == null || numTarjeta.isEmpty()) {
            System.out.println("Número de tarjeta inválido");
            return false;
        }

        System.out.println("Pago con tarjeta procesado: $" + monto);
        return true;
    }
}