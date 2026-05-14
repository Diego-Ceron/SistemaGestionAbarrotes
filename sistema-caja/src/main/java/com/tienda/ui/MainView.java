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
import com.tienda.promocion.PromocionDAO;
import com.tienda.promocion.PromocionModel;
import com.tienda.promocion.PromocionView;
import com.tienda.reporte.ReporteController;
import com.tienda.reporte.ReporteView;
import com.tienda.venta.VentaController;
import com.tienda.venta.VentaDAO;
import com.tienda.venta.VentaModel;
import com.tienda.venta.VentaView;

public class MainView {

    private final Scanner scanner = new Scanner(System.in);
    private final ClienteDAO clienteDAO = new ClienteDAO();
    private final ClienteView clienteView = new ClienteView();
    private final InventarioView inventarioView = new InventarioView();
    private final ProductoDAO productoDAO = new ProductoDAO();
    private final VentaView ventaView = new VentaView();
    private final VentaDAO ventaDAO = new VentaDAO();
    private final VentaController ventaController = new VentaController(ventaDAO);
    private final PromocionDAO promocionDAO = new PromocionDAO();
    private final PromocionView promocionView = new PromocionView();
    private final ReporteController reporteController = new ReporteController();
    private final ReporteView reporteView = new ReporteView();

    public void mostrarMenu() {
        boolean running = true;
        while (running) {
            System.out.println("\n=== SISTEMA DE CAJA - MENU PRINCIPAL ===");
            System.out.println("1) Clientes");
            System.out.println("2) Inventario");
            System.out.println("3) Ventas");
            System.out.println("4) Promociones");
            System.out.println("5) Reportes");
            System.out.println("0) Salir");
            System.out.print("Elija una opción: ");
            String opt = scanner.nextLine().trim();
            switch (opt) {
                case "1" -> menuClientes();
                case "2" -> menuInventario();
                case "3" -> menuVentas();
                case "4" -> menuPromociones();
                case "5" -> menuReportes();
                case "0" -> {
                    running = false;
                    System.out.println("Saliendo...");
                }
                default -> System.out.println("Opción inválida");
            }
        }
    }

    private void menuReportes() {
        reporteView.mostrarMenu(scanner, reporteController);
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

    private void menuVentas() {
        System.out.println("\n--- Ventas ---");
        System.out.println("1) Registrar venta");
        System.out.println("2) Listar ventas");
        System.out.println("0) Volver");
        System.out.print("Opción: ");
        String opt = scanner.nextLine().trim();
        switch (opt) {
            case "1" -> {
                VentaModel v = ventaView.pedirDatosVenta(scanner);
                if (v != null) {
                    ventaController.registrarVenta(v);
                    ventaView.mostrarVenta(v);
                    System.out.println("Venta registrada con ID: " + v.getId());
                }
            }
            case "2" -> ventaView.mostrarHistorial(ventaDAO.listarTodos());
            default -> System.out.println("Volviendo al menú principal...");
        }
    }

    private void menuPromociones() {
        System.out.println("\n--- Promociones ---");
        System.out.println("1) Listar promociones");
        System.out.println("2) Agregar promocion");
        System.out.println("3) Editar promocion");
        System.out.println("4) Eliminar promocion");
        System.out.println("0) Volver");
        System.out.print("Opción: ");
        String opt = scanner.nextLine().trim();
        switch (opt) {
            case "1" -> promocionView.mostrarPromociones(promocionDAO.listarTodos());
            case "2" -> {
                PromocionModel p = promocionView.pedirDatosPromocion(scanner);
                if (p != null) {
                    promocionDAO.agregar(p);
                    System.out.println("Promoción agregada con ID: " + p.getId());
                }
            }
            case "3" -> {
                int id = promocionView.pedirIdPromocion(scanner);
                if (id > 0) {
                    PromocionModel existing = promocionDAO.buscarPorProducto(id);
                    // buscarPorProducto no es ideal para buscar por id; simplificamos
                    System.out.println("Ingresa nuevos datos para la promoción:");
                    PromocionModel nuevo = promocionView.pedirDatosPromocion(scanner);
                    if (nuevo != null) {
                        nuevo.setId(id);
                        promocionDAO.actualizar(nuevo);
                        System.out.println("Promoción actualizada.");
                    }
                }
            }
            case "4" -> {
                int idDel = promocionView.pedirIdPromocion(scanner);
                if (idDel > 0) {
                    promocionDAO.eliminar(idDel);
                    System.out.println("Promoción eliminada (si existía).");
                }
            }
            default -> System.out.println("Volviendo al menú principal...");
        }
    }

    public static void main(String[] args) {
        new MainView().mostrarMenu();
    }
}
