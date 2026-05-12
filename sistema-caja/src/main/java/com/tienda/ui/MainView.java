package com.tienda.ui;

import java.util.Scanner;

import com.tienda.cliente.ClienteDAO;
import com.tienda.cliente.ClienteModel;
import com.tienda.cliente.ClienteView;
import com.tienda.inventario.InventarioView;
import com.tienda.producto.ProductoDAO;

public class MainView {

    private final Scanner scanner = new Scanner(System.in);
    private final ClienteDAO clienteDAO = new ClienteDAO();
    private final ClienteView clienteView = new ClienteView();
    private final InventarioView inventarioView = new InventarioView();

    public void mostrarMenu() {
        boolean running = true;
        while (running) {
            System.out.println("\n=== SISTEMA DE CAJA - MENU PRINCIPAL ===");
            System.out.println("1) Clientes");
            System.out.println("2) Inventario");
            System.out.println("0) Salir");
            System.out.print("Elija una opción: ");
            String opt = scanner.nextLine().trim();
            switch (opt) {
                case "1" -> menuClientes();
                case "2" -> menuInventario();
                case "0" -> {
                    running = false;
                    System.out.println("Saliendo...");
                }
                default -> System.out.println("Opción inválida");
            }
        }
    }

    private void menuClientes() {
        System.out.println("\n--- Clientes ---");
        System.out.println("1) Buscar cliente por ID");
        System.out.println("2) Listar clientes frecuentes");
        System.out.println("3) Registrar cliente");
        System.out.println("0) Volver");
        System.out.print("Opción: ");
        String opt = scanner.nextLine().trim();
        switch (opt) {
            case "1" -> {
                System.out.print("ID cliente: ");
                try {
                    int id = Integer.parseInt(scanner.nextLine().trim());
                    ClienteModel c = clienteDAO.buscar(id);
                    clienteView.mostrarCliente(c);
                } catch (NumberFormatException e) {
                    System.out.println("ID inválido");
                }
            }
            case "2" -> {
                clienteDAO.listarFrecuentes();
            }
            case "3" -> {
                ClienteModel c = new ClienteModel();
                System.out.print("Nombre: "); c.setNombre(scanner.nextLine().trim());
                System.out.print("Email: "); c.setEmail(scanner.nextLine().trim());
                System.out.print("Teléfono: "); c.setTelefono(scanner.nextLine().trim());
                System.out.print("Dirección: "); c.setDireccion(scanner.nextLine().trim());
                clienteDAO.registrar(c);
                System.out.println("Cliente registrado con ID: " + c.getId());
            }
            default -> System.out.println("Volviendo al menú principal...");
        }
    }

    private void menuInventario() {
        System.out.println("\n--- Inventario ---");
        System.out.println("1) Listar productos");
        System.out.println("2) Registrar movimiento");
        System.out.println("0) Volver");
        System.out.print("Opción: ");
        String opt = scanner.nextLine().trim();
        switch (opt) {
            case "1" -> inventarioView.mostrarProductos(new ProductoDAO().listarTodos());
            case "2" -> inventarioView.registrarMovimientoInteractivo(scanner);
            default -> System.out.println("Volviendo al menú principal...");
        }
    }

    public static void main(String[] args) {
        new MainView().mostrarMenu();
    }
}
