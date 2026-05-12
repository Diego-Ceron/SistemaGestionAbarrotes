package com.tienda.ui;

import java.util.Scanner;

import com.tienda.cliente.ClienteDAO;
import com.tienda.cliente.ClienteModel;
import com.tienda.cliente.ClienteView;
import com.tienda.inventario.InventarioView;
import com.tienda.inventario.MovimientoDAO;
import com.tienda.inventario.MovimientoInventarioModel;
import com.tienda.producto.ProductoDAO;
import com.tienda.producto.ProductoModel;

public class MainView {

    private final Scanner scanner = new Scanner(System.in);
    private final ClienteDAO clienteDAO = new ClienteDAO();
    private final ClienteView clienteView = new ClienteView();
    private final InventarioView inventarioView = new InventarioView();
    private final ProductoDAO productoDAO = new ProductoDAO();

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
                clienteView.mostrarClientesFrecuentes(clienteDAO.listarFrecuentes());
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
        System.out.println("2) Agregar producto");
        System.out.println("3) Editar producto");
        System.out.println("4) Eliminar producto");
        System.out.println("5) Buscar producto");
        System.out.println("6) Registrar movimiento");
        System.out.println("0) Volver");
        System.out.print("Opción: ");
        String opt = scanner.nextLine().trim();
        switch (opt) {
            case "1" -> inventarioView.mostrarProductos(productoDAO.listarTodos());
            case "2" -> {
                ProductoModel p = inventarioView.pedirDatosProducto(scanner);
                if (p != null) {
                    productoDAO.agregar(p);
                    System.out.println("Producto agregado con ID: " + p.getId());
                }
            }
            case "3" -> {
                int id = inventarioView.pedirIdProducto(scanner);
                if (id > 0) {
                    ProductoModel encontrado = productoDAO.listarTodos().stream()
                            .filter(x -> x.getId() == id).findFirst().orElse(null);
                    if (encontrado == null) {
                        System.out.println("Producto no encontrado.");
                    } else {
                        System.out.println("Ingrese nuevos datos (dejar vacío para mantener valor actual):");
                        ProductoModel nuevo = inventarioView.pedirDatosProducto(scanner);
                        if (nuevo != null) {
                            nuevo.setId(id);
                            productoDAO.actualizar(nuevo);
                            System.out.println("Producto actualizado.");
                        }
                    }
                }
            }
            case "4" -> {
                int idDel = inventarioView.pedirIdProducto(scanner);
                if (idDel > 0) {
                    productoDAO.eliminar(idDel);
                    System.out.println("Producto eliminado (si existía).");
                }
            }
            case "5" -> {
                System.out.println("Buscar por: 1) Código 2) Nombre 3) Categoría");
                System.out.print("Opción: ");
                String sopt = scanner.nextLine().trim();
                switch (sopt) {
                    case "1" -> {
                        System.out.print("Código: ");
                        String codigo = scanner.nextLine().trim();
                        inventarioView.mostrarProductos(productoDAO.getBuscador().buscarPorCodigo(codigo));
                    }
                    case "2" -> {
                        System.out.print("Nombre (parcial): ");
                        String nombre = scanner.nextLine().trim();
                        inventarioView.mostrarProductos(productoDAO.getBuscador().buscarPorNombre(nombre));
                    }
                    case "3" -> {
                        System.out.print("Categoría (parcial): ");
                        String cat = scanner.nextLine().trim();
                        inventarioView.mostrarProductos(productoDAO.getBuscador().buscarPorCategoria(cat));
                    }
                    default -> System.out.println("Opción de búsqueda inválida.");
                }
            }
            case "6" -> {
                MovimientoInventarioModel m = inventarioView.pedirDatosMovimiento(scanner);
                if (m != null) {
                    new MovimientoDAO().registrar(m);
                    System.out.println("Movimiento registrado.");
                }
            }
            default -> System.out.println("Volviendo al menú principal...");
        }
    }

    public static void main(String[] args) {
        new MainView().mostrarMenu();
    }
}
