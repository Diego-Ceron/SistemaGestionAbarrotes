package main.java.com.tienda.metodoPago;

public class PagoEfectivo implements IMetodoPago {
    private double montoRecibido;

    public double getMontoRecibido() { return montoRecibido; }
    public void setMontoRecibido(double monto) { this.montoRecibido = monto; }

    @Override
    public boolean procesar(double monto) {
        if (montoRecibido < monto) {
            System.out.println("Efectivo insuficiente");
            return false;
        }
        System.out.println("Pago en efectivo procesado: $" + monto);
        System.out.println("   Cambio: $" + calcularCambio(monto));
        return true;
    }

    public double calcularCambio(double monto) {
        return montoRecibido - monto;
    }
}