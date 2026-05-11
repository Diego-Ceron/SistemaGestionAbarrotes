package main.java.com.tienda.metodoPago;

public class PagoTarjeta implements IMetodoPago {
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