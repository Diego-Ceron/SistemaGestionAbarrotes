package com.tienda.cliente;

import java.util.List;
import java.util.Scanner;

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
    // Pide datos para crear o actualizar cliente
    public ClienteModel pedirDatosCliente(Scanner scanner) {
        ClienteModel c = new ClienteModel();
        System.out.print("Nombre   : "); c.setNombre(scanner.nextLine().trim());
        System.out.print("Email    : "); c.setEmail(scanner.nextLine().trim());
        System.out.print("Teléfono : "); c.setTelefono(scanner.nextLine().trim());
        System.out.print("Dirección: "); c.setDireccion(scanner.nextLine().trim());
        return c;
    }

    public int pedirIdCliente(Scanner scanner) {
        System.out.print("ID del cliente: ");
        try { return Integer.parseInt(scanner.nextLine().trim()); } catch (NumberFormatException e) {
            System.out.println("ID inválido."); return -1;
        }
    }

    // Menú interactivo para CRUD de clientes
    public void mostrarMenu(Scanner scanner, ClienteController controller) {
        boolean running = true;
        while (running) {
            System.out.println("\n--- Clientes ---");
            System.out.println("1) Listar todos");
            System.out.println("2) Ver cliente por ID");
            System.out.println("3) Registrar cliente");
            System.out.println("4) Actualizar cliente");
            System.out.println("5) Eliminar cliente");
            System.out.println("0) Volver");
            System.out.print("Opción: ");
            String opt = scanner.nextLine().trim();
            switch (opt) {
                case "1" -> {
                    List<ClienteModel> lista = controller.listarTodos();
                    if (lista == null || lista.isEmpty()) System.out.println("No hay clientes.");
                    else lista.forEach(this::mostrarCliente);
                }
                case "2" -> {
                    int id = pedirIdCliente(scanner);
                    if (id > 0) mostrarCliente(controller.buscarCliente(id));
                }
                case "3" -> {
                    ClienteModel c = pedirDatosCliente(scanner);
                    controller.registrarCliente(c);
                    mostrarMensaje("Cliente registrado con ID: " + c.getId());
                }
                case "4" -> {
                    int id = pedirIdCliente(scanner);
                    if (id > 0) {
                        ClienteModel existente = controller.buscarCliente(id);
                        if (existente == null) { System.out.println("Cliente no encontrado."); break; }
                        System.out.println("Ingrese nuevos datos (dejar vacío para mantener)");
                        System.out.print("Nombre (actual: " + existente.getNombre() + "): ");
                        String nombre = scanner.nextLine().trim(); if (!nombre.isEmpty()) existente.setNombre(nombre);
                        System.out.print("Email (actual: " + existente.getEmail() + "): ");
                        String email = scanner.nextLine().trim(); if (!email.isEmpty()) existente.setEmail(email);
                        System.out.print("Teléfono (actual: " + existente.getTelefono() + "): ");
                        String tel = scanner.nextLine().trim(); if (!tel.isEmpty()) existente.setTelefono(tel);
                        System.out.print("Dirección (actual: " + existente.getDireccion() + "): ");
                        String dir = scanner.nextLine().trim(); if (!dir.isEmpty()) existente.setDireccion(dir);
                        controller.actualizarCliente(existente);
                        mostrarMensaje("Cliente actualizado.");
                    }
                }
                case "5" -> {
                    int id = pedirIdCliente(scanner);
                    if (id > 0) {
                        controller.eliminarCliente(id);
                        mostrarMensaje("Cliente eliminado (si existía).");
                    }
                }
                case "0" -> running = false;
                default -> System.out.println("Opción inválida");
            }
        }
    }
}