package com.tienda.cliente;

import java.util.List;

public class ClienteView {

    // Muestra los datos de un cliente individual
    public void mostrarCliente(ClienteModel c) {
        if (c == null) {
            System.out.println("Cliente no encontrado.");
            return;
        }
        System.out.println("========== DATOS DEL CLIENTE ==========");
        System.out.printf("ID       : %d%n", c.getId());
        System.out.printf("Nombre   : %s%n", c.getNombre());
        System.out.printf("Email    : %s%n", c.getEmail());
        System.out.printf("Teléfono : %s%n", c.getTelefono());
        System.out.printf("Dirección: %s%n", c.getDireccion());
        System.out.println("=======================================");
    }

    // Muestra la lista de clientes frecuentes
    public void mostrarClientesFrecuentes(List<ClienteModel> clientes) {
        if (clientes == null || clientes.isEmpty()) {
            System.out.println("No hay clientes frecuentes registrados.");
            return;
        }
        System.out.println("======== CLIENTES FRECUENTES ========");
        System.out.printf("%-5s %-25s %-20s %-15s%n", "ID", "Nombre", "Email", "Teléfono");
        System.out.println("-------------------------------------");
        for (ClienteModel c : clientes) {
            System.out.printf("%-5d %-25s %-20s %-15s%n",
                    c.getId(),
                    c.getNombre(),
                    c.getEmail(),
                    c.getTelefono());
        }
        System.out.println("=====================================");
    }

    // Muestra el historial de compras de un cliente
    // Recibe el nombre del cliente y una lista de descripciones de compras
    public void mostrarHistorialCompras(ClienteModel cliente, List<String> historial) {
        if (cliente == null) {
            System.out.println("Cliente no encontrado.");
            return;
        }
        System.out.println("===== HISTORIAL DE COMPRAS: " + cliente.getNombre() + " =====");
        if (historial == null || historial.isEmpty()) {
            System.out.println("Este cliente no tiene compras registradas.");
        } else {
            for (int i = 0; i < historial.size(); i++) {
                System.out.printf("%d. %s%n", i + 1, historial.get(i));
            }
        }
        System.out.println("==============================================");
    }

    // Mensaje de éxito o error genérico
    public void mostrarMensaje(String mensaje) {
        System.out.println("[INFO] " + mensaje);
    }
}