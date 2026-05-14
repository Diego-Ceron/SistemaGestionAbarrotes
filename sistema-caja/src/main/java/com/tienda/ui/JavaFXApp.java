package com.tienda.ui;

import java.util.List;
import java.util.Optional;

import com.tienda.cliente.ClienteDAO;
import com.tienda.cliente.ClienteModel;
import com.tienda.producto.ProductoDAO;
import com.tienda.producto.ProductoModel;
import com.tienda.promocion.PromocionDAO;
import com.tienda.promocion.PromocionModel;
import com.tienda.venta.VentaController;
import com.tienda.venta.VentaDAO;
import com.tienda.venta.VentaModel;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class JavaFXApp extends Application {

    private final ProductoDAO productoDAO = new ProductoDAO();
    private final ClienteDAO clienteDAO = new ClienteDAO();
    private final PromocionDAO promocionDAO = new PromocionDAO();
    private final com.tienda.reporte.ReporteDAO reporteDAO = new com.tienda.reporte.ReporteDAO();
    private final VentaController ventaController = new VentaController(new VentaDAO());

    @Override
    public void start(Stage primaryStage) {
        TabPane tabPane = new TabPane();
        tabPane.getTabs().addAll(
                new Tab("Clientes", createClientesPane()),
                new Tab("Inventario", createInventarioPane()),
                new Tab("Ventas", createVentasPane()),
                new Tab("Promociones", createPromocionesPane()),
                new Tab("Reportes", createReportesPane())
        );

        tabPane.getTabs().forEach(tab -> tab.setClosable(false));

        Label title = new Label("Sistema de Gestión de Abarrotes");
        title.getStyleClass().add("title");
        Label subtitle = new Label("Control completo de clientes, inventario, ventas y reportes");
        subtitle.getStyleClass().add("subtitle");
        Separator headerSeparator = new Separator();

        VBox headerBox = new VBox(6, title, subtitle, headerSeparator);
        headerBox.setPadding(new Insets(0, 0, 14, 0));

        BorderPane root = new BorderPane(tabPane);
        root.getStyleClass().add("root-pane");
        root.setPadding(new Insets(16));
        root.setTop(headerBox);

        Scene scene = new Scene(root, 1100, 760);
        scene.getStylesheets().add(getClass().getResource("/app.css").toExternalForm());
        primaryStage.setTitle("Sistema de Gestión de Abarrotes");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private BorderPane createClientesPane() {
        TableView<ClienteModel> table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.getColumns().addAll(
                createColumn("ID", "id", 60),
                createColumn("Nombre", "nombre", 200),
                createColumn("Dirección", "direccion", 250),
                createColumn("Teléfono", "telefono", 140),
                createColumn("Email", "email", 200)
        );

        Button refresh = new Button("Refrescar");
        refresh.setOnAction(event -> table.getItems().setAll(clienteDAO.listarTodos()));

        Button add = new Button("Agregar cliente");
        add.setOnAction(e -> {
            TextInputDialog d = new TextInputDialog();
            d.setHeaderText("Ingrese nombre:");
            d.showAndWait().ifPresent(nombre -> {
                TextInputDialog d2 = new TextInputDialog(); d2.setHeaderText("Ingrese email:");
                d2.showAndWait().ifPresent(email -> {
                    TextInputDialog d3 = new TextInputDialog(); d3.setHeaderText("Ingrese teléfono:");
                    d3.showAndWait().ifPresent(tel -> {
                        TextInputDialog d4 = new TextInputDialog(); d4.setHeaderText("Ingrese dirección:");
                        d4.showAndWait().ifPresent(dir -> {
                            com.tienda.cliente.ClienteModel c = new com.tienda.cliente.ClienteModel();
                            c.setNombre(nombre); c.setEmail(email); c.setTelefono(tel); c.setDireccion(dir);
                            clienteDAO.registrar(c);
                            refresh.fire();
                        });
                    });
                });
            });
        });

        Button edit = new Button("Editar cliente");
        edit.setOnAction(e -> {
            TextInputDialog idDialog = new TextInputDialog(); idDialog.setHeaderText("ID cliente a editar:");
            idDialog.showAndWait().ifPresent(idStr -> {
                try {
                    int id = Integer.parseInt(idStr.trim());
                    com.tienda.cliente.ClienteModel c = clienteDAO.buscar(id);
                    if (c == null) { new Alert(AlertType.INFORMATION, "Cliente no encontrado").show(); return; }
                    TextInputDialog dn = new TextInputDialog(c.getNombre()); dn.setHeaderText("Nombre:");
                    dn.showAndWait().ifPresent(nombre -> { c.setNombre(nombre);
                        TextInputDialog de = new TextInputDialog(c.getEmail()); de.setHeaderText("Email:");
                        de.showAndWait().ifPresent(email -> { c.setEmail(email);
                            TextInputDialog dt = new TextInputDialog(c.getTelefono()); dt.setHeaderText("Teléfono:");
                            dt.showAndWait().ifPresent(tel -> { c.setTelefono(tel);
                                TextInputDialog dd = new TextInputDialog(c.getDireccion()); dd.setHeaderText("Dirección:");
                                dd.showAndWait().ifPresent(dir -> { c.setDireccion(dir); clienteDAO.actualizar(c); refresh.fire(); });
                            });
                        });
                    });
                } catch (NumberFormatException ex) { new Alert(AlertType.ERROR, "ID inválido").show(); }
            });
        });

        Button del = new Button("Eliminar cliente");
        del.setOnAction(e -> {
            TextInputDialog idDialog = new TextInputDialog(); idDialog.setHeaderText("ID cliente a eliminar:");
            idDialog.showAndWait().ifPresent(idStr -> {
                try { int id = Integer.parseInt(idStr.trim()); clienteDAO.eliminar(id); refresh.fire(); }
                catch (NumberFormatException ex) { new Alert(AlertType.ERROR, "ID inválido").show(); }
            });
        });

        HBox btns = new HBox(8, refresh, add, edit, del);
        btns.setAlignment(Pos.CENTER_LEFT);

        VBox main = new VBox(10, btns, table);
        main.setPadding(new Insets(18));
        main.getStyleClass().add("card");
        main.setFillWidth(true);
        VBox.setVgrow(table, Priority.ALWAYS);
        refresh.fire();

        return new BorderPane(main);
    }

    private BorderPane createInventarioPane() {
        TableView<ProductoModel> table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.getColumns().addAll(
                createColumn("ID", "id", 60),
                createColumn("Nombre", "nombre", 220),
                createColumn("Precio", "precio", 100),
                createColumn("Cantidad", "cantidad", 90),
                createColumn("Código", "codigo", 120),
                createColumn("Proveedor", "proveedor", 160),
                createColumn("Categoría", "categoria", 150)
        );

        Button refresh = new Button("Refrescar inventario");
        refresh.setOnAction(event -> table.getItems().setAll(productoDAO.listarTodos()));

        Button add = new Button("Agregar producto");
        add.setOnAction(e -> {
            TextInputDialog dn = new TextInputDialog(); dn.setHeaderText("Nombre:");
            dn.showAndWait().ifPresent(nombre -> {
                try {
                    TextInputDialog dp = new TextInputDialog(); dp.setHeaderText("Precio:");
                    dp.showAndWait().ifPresent(pre -> {
                        TextInputDialog dc = new TextInputDialog(); dc.setHeaderText("Cantidad:");
                        dc.showAndWait().ifPresent(cant -> {
                            com.tienda.producto.ProductoModel p = new com.tienda.producto.ProductoModel();
                            p.setNombre(nombre);
                            try { p.setPrecio(Double.parseDouble(pre.trim())); } catch (Exception ex) {}
                            try { p.setCantidad(Integer.parseInt(cant.trim())); } catch (Exception ex) {}
                            TextInputDialog dCodigo = new TextInputDialog(); dCodigo.setHeaderText("Código (opcional):");
                            dCodigo.showAndWait().ifPresent(code -> p.setCodigo(code.trim()));
                            TextInputDialog dProv = new TextInputDialog(); dProv.setHeaderText("Proveedor (opcional):");
                            dProv.showAndWait().ifPresent(prov -> p.setProveedor(prov.trim()));
                            TextInputDialog dCat = new TextInputDialog(); dCat.setHeaderText("Categoría (opcional):");
                            dCat.showAndWait().ifPresent(cat -> p.setCategoria(cat.trim()));
                            productoDAO.agregar(p); refresh.fire();
                        });
                    });
                } catch (Exception ex) { new Alert(AlertType.ERROR, "Entrada inválida").show(); }
            });
        });

        Button edit = new Button("Editar producto");
        edit.setOnAction(e -> {
            TextInputDialog idD = new TextInputDialog(); idD.setHeaderText("ID producto a editar:");
            idD.showAndWait().ifPresent(idStr -> {
                try {
                    int id = Integer.parseInt(idStr.trim());
                    com.tienda.producto.ProductoModel p = productoDAO.listarTodos().stream().filter(x -> x.getId() == id).findFirst().orElse(null);
                    if (p == null) { new Alert(AlertType.INFORMATION, "Producto no encontrado").show(); return; }
                    TextInputDialog nn = new TextInputDialog(p.getNombre()); nn.setHeaderText("Nombre:");
                    nn.showAndWait().ifPresent(nombre -> { p.setNombre(nombre);
                        TextInputDialog pp = new TextInputDialog(String.valueOf(p.getPrecio())); pp.setHeaderText("Precio:");
                        pp.showAndWait().ifPresent(pre -> { try { p.setPrecio(Double.parseDouble(pre.trim())); } catch (Exception ex) {};
                            TextInputDialog pc = new TextInputDialog(p.getCodigo() == null ? "" : p.getCodigo()); pc.setHeaderText("Código (opcional):");
                            pc.showAndWait().ifPresent(code -> p.setCodigo(code.trim()));
                            TextInputDialog prov = new TextInputDialog(p.getProveedor() == null ? "" : p.getProveedor()); prov.setHeaderText("Proveedor (opcional):");
                            prov.showAndWait().ifPresent(pr -> p.setProveedor(pr.trim()));
                            TextInputDialog cat = new TextInputDialog(p.getCategoria() == null ? "" : p.getCategoria()); cat.setHeaderText("Categoría (opcional):");
                            cat.showAndWait().ifPresent(ca -> p.setCategoria(ca.trim()));
                            productoDAO.actualizar(p); refresh.fire(); });
                    });
                } catch (NumberFormatException ex) { new Alert(AlertType.ERROR, "ID inválido").show(); }
            });
        });

        Button del = new Button("Eliminar producto");
        del.setOnAction(e -> {
            TextInputDialog idD = new TextInputDialog(); idD.setHeaderText("ID producto a eliminar:");
            idD.showAndWait().ifPresent(idStr -> { try { productoDAO.eliminar(Integer.parseInt(idStr.trim())); refresh.fire(); } catch (Exception ex) { new Alert(AlertType.ERROR, "ID inválido").show(); } });
        });

        Button mov = new Button("Registrar movimiento");
        mov.setOnAction(e -> {
            TextInputDialog pid = new TextInputDialog(); pid.setHeaderText("ID producto:");
            pid.showAndWait().ifPresent(pidStr -> {
                TextInputDialog tt = new TextInputDialog(); tt.setHeaderText("Tipo (ENTRADA/SALIDA):");
                tt.showAndWait().ifPresent(tipo -> {
                    // Normalizar tipo: eliminar AJUSTE (tratar como ENTRADA)
                    String tipoNorm = tipo.trim().equalsIgnoreCase("AJUSTE") ? "ENTRADA" : tipo.trim().toUpperCase();
                    if (!tipoNorm.equalsIgnoreCase("ENTRADA") && !tipoNorm.equalsIgnoreCase("SALIDA")) {
                        new Alert(AlertType.ERROR, "Tipo inválido. Use ENTRADA o SALIDA.").show();
                        return;
                    }
                    TextInputDialog cq = new TextInputDialog(); cq.setHeaderText("Cantidad:");
                    cq.showAndWait().ifPresent(cqStr -> {
                        try {
                            com.tienda.inventario.MovimientoInventarioModel m = new com.tienda.inventario.MovimientoInventarioModel();
                            m.setProductoId(Integer.parseInt(pidStr.trim()));
                            m.setTipo(tipoNorm);
                            m.setCantidad(Integer.parseInt(cqStr.trim()));
                            m.setFecha(java.time.LocalDateTime.now().toString());
                            new com.tienda.inventario.MovimientoDAO().registrar(m);
                            new Alert(AlertType.INFORMATION, "Movimiento registrado").show();
                        } catch (Exception ex) { new Alert(AlertType.ERROR, "Entrada inválida").show(); }
                    });
                });
            });
        });

        HBox btns = new HBox(8, refresh, add, edit, del, mov);
        btns.setAlignment(Pos.CENTER_LEFT);

        // Añadir botón de búsqueda que usa BuscarProductoService
        Button search = new Button("Buscar producto");
        search.setOnAction(ev -> {
            TextInputDialog tipoD = new TextInputDialog(); tipoD.setHeaderText("Buscar por: id/codigo/categoria");
            tipoD.showAndWait().ifPresent(tipo -> {
                String t = tipo.trim().toLowerCase();
                com.tienda.producto.BuscarProductoService buscador = productoDAO.getBuscador();
                if (t.equals("id")) {
                    TextInputDialog idD = new TextInputDialog(); idD.setHeaderText("ID:");
                    idD.showAndWait().ifPresent(idStr -> {
                        try {
                            int id = Integer.parseInt(idStr.trim());
                            table.getItems().setAll(buscador.buscarPorId(id));
                        } catch (NumberFormatException ex) { new Alert(AlertType.ERROR, "ID inválido").show(); }
                    });
                } else if (t.equals("codigo")) {
                    TextInputDialog cD = new TextInputDialog(); cD.setHeaderText("Código:");
                    cD.showAndWait().ifPresent(code -> table.getItems().setAll(buscador.buscarPorCodigo(code.trim())));
                } else if (t.equals("categoria")) {
                    TextInputDialog cD = new TextInputDialog(); cD.setHeaderText("Categoría (parcial):");
                    cD.showAndWait().ifPresent(cat -> table.getItems().setAll(buscador.buscarPorCategoria(cat.trim())));
                } else {
                    new Alert(AlertType.INFORMATION, "Opción inválida. Use id, codigo o categoria.").show();
                }
            });
        });

        VBox main = new VBox(10, btns, search, table);
        main.setPadding(new Insets(18));
        main.getStyleClass().add("card");
        main.setFillWidth(true);
        VBox.setVgrow(table, Priority.ALWAYS);
        refresh.fire();

        return new BorderPane(main);
    }

    private BorderPane createVentasPane() {
        TableView<ProductoModel> cartTable = new TableView<>();
        cartTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        cartTable.getColumns().addAll(
                createColumn("ID", "id", 60),
                createColumn("Producto", "nombre", 260),
                createColumn("Cantidad", "cantidad", 110),
                createColumn("Precio unitario", "precio", 120)
        );

        TableColumn<ProductoModel, String> subtotalColumn = new TableColumn<>("Subtotal");
        subtotalColumn.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(
                String.format("%.2f", cell.getValue().getPrecio() * cell.getValue().getCantidad())
        ));
        subtotalColumn.setPrefWidth(120);
        cartTable.getColumns().add(subtotalColumn);

        ObservableList<ProductoModel> cartItems = FXCollections.observableArrayList();
        cartTable.setItems(cartItems);

        TextField productIdField = new TextField();
        TextField quantityField = new TextField();
        ComboBox<String> paymentMethod = new ComboBox<>(FXCollections.observableArrayList("Efectivo", "Tarjeta"));
        paymentMethod.setValue("Efectivo");
        TextField paymentDataField = new TextField();
        paymentDataField.setPromptText("Monto recibido");
        TextField phoneField = new TextField();
        phoneField.setPromptText("Teléfono del cliente (opcional)");
        Label totalLabel = new Label("Total: $0.00");
        TextArea ticketArea = new TextArea();
        ticketArea.setEditable(false);
        ticketArea.setWrapText(true);

        productIdField.setPromptText("ID producto");
        quantityField.setPromptText("Cantidad");

        paymentMethod.setOnAction(event -> {
            if (paymentMethod.getValue().equals("Efectivo")) {
                paymentDataField.setPromptText("Monto recibido");
            } else {
                paymentDataField.setPromptText("Número de tarjeta");
            }
        });

        Button addButton = new Button("Agregar al carrito");
        addButton.setOnAction(event -> {
            try {
                int productId = Integer.parseInt(productIdField.getText().trim());
                int quantity = Integer.parseInt(quantityField.getText().trim());
                Optional<ProductoModel> found = productoDAO.listarTodos().stream()
                        .filter(p -> p.getId() == productId)
                        .findFirst();
                if (found.isEmpty()) {
                    ticketArea.setText("Producto no encontrado.");
                    return;
                }
                if (quantity <= 0) {
                    ticketArea.setText("Cantidad debe ser mayor que cero.");
                    return;
                }
                ProductoModel producto = found.get();
                if (quantity > producto.getCantidad()) {
                    ticketArea.setText("Stock insuficiente. Disponible: " + producto.getCantidad());
                    return;
                }
                ProductoModel item = new ProductoModel();
                item.setId(producto.getId());
                item.setNombre(producto.getNombre());
                item.setPrecio(producto.getPrecio());
                item.setCantidad(quantity);
                item.setCodigo(producto.getCodigo());
                item.setCategoria(producto.getCategoria());
                cartItems.add(item);
                productIdField.clear();
                quantityField.clear();
                updateTotalLabel(cartItems, totalLabel);
            } catch (NumberFormatException e) {
                ticketArea.setText("ID y cantidad deben ser números válidos.");
            }
        });

        Button removeByIdButton = new Button("Eliminar artículo por ID");
        removeByIdButton.setOnAction(ev -> {
            TextInputDialog idD = new TextInputDialog();
            idD.setHeaderText("ID producto a eliminar (del carrito):");
            idD.showAndWait().ifPresent(idStr -> {
                try {
                    int id = Integer.parseInt(idStr.trim());
                    boolean removed = cartItems.removeIf(p -> p.getId() == id);
                    if (!removed) {
                        ticketArea.setText("No se encontró artículo con ID " + id + " en el carrito.");
                    } else {
                        updateTotalLabel(cartItems, totalLabel);
                        ticketArea.setText("Artículo(s) con ID " + id + " eliminados del carrito.");
                    }
                } catch (NumberFormatException ex) {
                    new Alert(AlertType.ERROR, "ID inválido").show();
                }
            });
        });

        Button sellButton = new Button("Registrar venta");
        sellButton.setOnAction(event -> {
            if (cartItems.isEmpty()) {
                ticketArea.setText("Agregue productos al carrito antes de completar la venta.");
                return;
            }
            double subtotal = cartItems.stream().mapToDouble(p -> p.getPrecio() * p.getCantidad()).sum();
            String metodo = paymentMethod.getValue();

            // Obtener cliente por teléfono
            String telefono = phoneField.getText().trim();
            ClienteModel cliente = null;
            if (!telefono.isEmpty()) {
                cliente = clienteDAO.buscarPorTelefono(telefono);
                if (cliente == null) {
                    // Crear nuevo cliente
                    cliente = new ClienteModel();
                    cliente.setNombre("Cliente " + telefono);
                    cliente.setTelefono(telefono);
                    clienteDAO.registrar(cliente);
                    cliente = clienteDAO.buscarPorTelefono(telefono); // Obtener id
                }
            }

            // Calcular descuento si cliente tiene >=1 compras en el mes
            double descuento = 0.0;
            if (cliente != null) {
                int ventasMes = clienteDAO.contarVentasMes(cliente.getId());
                if (ventasMes >= 1) {
                    descuento = subtotal * 0.05;
                }
            }
            double total = subtotal - descuento;

            VentaModel venta = new VentaModel();
            venta.setProductos(cartItems.toArray(new ProductoModel[0]));
            venta.setCliente(cliente != null ? cliente.getNombre() : "Cliente ocasional");
            venta.setClienteId(cliente != null ? cliente.getId() : null);
            venta.setTotal(total);
            venta.setMetodoPago(metodo);
            venta.setDescuento(descuento);

            if (metodo.equals("Efectivo")) {
                try {
                    double montoRecibido = Double.parseDouble(paymentDataField.getText().trim().replace(',', '.'));
                    if (montoRecibido < total) {
                        ticketArea.setText("El monto recibido es insuficiente.");
                        return;
                    }
                    venta.setMontoPagado(montoRecibido);
                    venta.setCambio(montoRecibido - total);
                } catch (NumberFormatException e) {
                    ticketArea.setText("Ingrese un monto válido para efectivo.");
                    return;
                }
            } else {
                String tarjeta = paymentDataField.getText().trim();
                if (tarjeta.isEmpty()) {
                    ticketArea.setText("Ingrese el número de tarjeta.");
                    return;
                }
                venta.setNumeroTarjeta(tarjeta);
                venta.setMontoPagado(total);
                venta.setCambio(0.0);
            }

            ventaController.registrarVenta(venta);
            ticketArea.setText(buildTicket(cartItems, venta, descuento));
            cartItems.clear();
            updateTotalLabel(cartItems, totalLabel);
            phoneField.clear();
            paymentDataField.clear();
        });

        GridPane form = new GridPane();
        form.setHgap(12);
        form.setVgap(12);
        form.add(new Label("ID producto:"), 0, 0);
        form.add(productIdField, 1, 0);
        form.add(new Label("Cantidad:"), 2, 0);
        form.add(quantityField, 3, 0);
        form.add(addButton, 4, 0);
        form.add(new Label("Pago:"), 0, 1);
        form.add(paymentMethod, 1, 1);
        form.add(paymentDataField, 2, 1, 2, 1);
        form.add(new Label("Teléfono:"), 0, 2);
        form.add(phoneField, 1, 2, 2, 1);
        form.add(sellButton, 4, 1);
        form.add(removeByIdButton, 4, 2);

        VBox leftPane = new VBox(14, form, totalLabel, cartTable);
        leftPane.setPadding(new Insets(12));
        leftPane.getStyleClass().add("card");
        leftPane.setFillWidth(true);
        VBox.setVgrow(cartTable, Priority.ALWAYS);

        VBox ticketPane = new VBox(12, new Label("Resumen de la venta"), ticketArea);
        ticketPane.setPadding(new Insets(12));
        ticketPane.getStyleClass().add("card");
        ticketArea.setPrefRowCount(18);
        ticketArea.setPromptText("Aquí se mostrará el ticket de venta...");
        VBox.setVgrow(ticketArea, Priority.ALWAYS);

        HBox mainContent = new HBox(14, leftPane, ticketPane);
        mainContent.setFillHeight(true);
        HBox.setHgrow(leftPane, Priority.ALWAYS);
        HBox.setHgrow(ticketPane, Priority.ALWAYS);

        return new BorderPane(mainContent);
    }

    private String buildTicket(List<ProductoModel> items, VentaModel venta, double descuento) {
        StringBuilder builder = new StringBuilder();
        builder.append("===== TICKET =====\n");
        builder.append("Cliente: ").append(venta.getCliente()).append("\n");
        double subtotal = items.stream().mapToDouble(p -> p.getPrecio() * p.getCantidad()).sum();
        items.forEach(p -> builder.append(p.getNombre())
                .append(" x")
                .append(p.getCantidad())
                .append(" = $")
                .append(String.format("%.2f", p.getPrecio() * p.getCantidad()))
                .append("\n"));
        builder.append("Subtotal: $").append(String.format("%.2f", subtotal)).append("\n");
        if (descuento > 0) {
            builder.append("Descuento cliente frecuente: -$").append(String.format("%.2f", descuento)).append("\n");
        }
        builder.append("Total: $").append(String.format("%.2f", venta.getTotal())).append("\n");
        builder.append("Método de pago: ").append(venta.getMetodoPago()).append("\n");
        if (venta.getMetodoPago().equals("Efectivo")) {
            builder.append("Monto recibido: $").append(String.format("%.2f", venta.getMontoPagado())).append("\n");
            builder.append("Cambio: $").append(String.format("%.2f", venta.getCambio())).append("\n");
        } else {
            builder.append("Tarjeta: ").append(venta.getNumeroTarjeta()).append("\n");
        }
        builder.append("==================");
        return builder.toString();
    }

    private BorderPane createPromocionesPane() {
        TableView<PromocionModel> table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.getColumns().addAll(
                createColumn("ID", "id", 80),
                createColumn("Descripción", "descripcion", 320),
                createColumn("Descuento (%)", "porcentajeDescuento", 140)
        );

        Button refresh = new Button("Refrescar promociones");
        refresh.setOnAction(event -> table.getItems().setAll(promocionDAO.listarTodos()));

        Button add = new Button("Agregar promocion");
        add.setOnAction(e -> {
            TextInputDialog d = new TextInputDialog(); d.setHeaderText("Descripción:");
            d.showAndWait().ifPresent(desc -> {
                TextInputDialog p = new TextInputDialog(); p.setHeaderText("Porcentaje (ej. 10):");
                p.showAndWait().ifPresent(por -> { try { com.tienda.promocion.PromocionModel pm = new com.tienda.promocion.PromocionModel(); pm.setDescripcion(desc); pm.setPorcentajeDescuento(Double.parseDouble(por.trim())); promocionDAO.agregar(pm); refresh.fire(); } catch (Exception ex) { new Alert(AlertType.ERROR, "Porcentaje inválido").show(); } });
            });
        });

        Button edit = new Button("Editar promocion");
        edit.setOnAction(e -> {
            TextInputDialog idD = new TextInputDialog(); idD.setHeaderText("ID promocion a editar:");
            idD.showAndWait().ifPresent(idStr -> {
                try { int id = Integer.parseInt(idStr.trim()); TextInputDialog d = new TextInputDialog(); d.setHeaderText("Nueva descripción:"); d.showAndWait().ifPresent(desc -> { TextInputDialog p = new TextInputDialog(); p.setHeaderText("Nuevo porcentaje:"); p.showAndWait().ifPresent(por -> { try { com.tienda.promocion.PromocionModel pm = new com.tienda.promocion.PromocionModel(); pm.setId(id); pm.setDescripcion(desc); pm.setPorcentajeDescuento(Double.parseDouble(por.trim())); promocionDAO.actualizar(pm); refresh.fire(); } catch (Exception ex) { new Alert(AlertType.ERROR, "Porcentaje inválido").show(); } }); }); } catch (NumberFormatException ex) { new Alert(AlertType.ERROR, "ID inválido").show(); } });
        });

        Button del = new Button("Eliminar promocion");
        del.setOnAction(e -> { TextInputDialog idD = new TextInputDialog(); idD.setHeaderText("ID promocion a eliminar:"); idD.showAndWait().ifPresent(idStr -> { try { promocionDAO.eliminar(Integer.parseInt(idStr.trim())); refresh.fire(); } catch (Exception ex) { new Alert(AlertType.ERROR, "ID inválido").show(); } }); });

        HBox btns = new HBox(8, refresh, add, edit, del);
        btns.setAlignment(Pos.CENTER_LEFT);

        VBox main = new VBox(10, btns, table);
        main.setPadding(new Insets(18));
        main.getStyleClass().add("card");
        main.setFillWidth(true);
        VBox.setVgrow(table, Priority.ALWAYS);
        refresh.fire();

        return new BorderPane(main);
    }

    private BorderPane createReportesPane() {
        TableView<com.tienda.reporte.ReporteModel> table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.getColumns().addAll(
            createColumn("Período", "periodo", 200),
            createColumn("Cantidad Ventas", "cantidadVentas", 150),
            createColumn("Total", "totalVentas", 150)
        );

        ComboBox<String> periodCombo = new ComboBox<>(FXCollections.observableArrayList("Diario", "Semanal", "Mensual", "Anual"));
        periodCombo.setValue("Diario");

        Button generate = new Button("Generar Reporte");
        generate.setOnAction(event -> {
            String periodo = periodCombo.getValue().toLowerCase();
            List<com.tienda.reporte.ReporteModel> reporte = reporteDAO.reporteVentas(periodo);
            table.getItems().setAll(reporte);
        });

        Button invBtn = new Button("Reporte Inventario");
        invBtn.setOnAction(ev -> {
            List<com.tienda.reporte.ReporteModel> inv = reporteDAO.reporteInventario();
            table.getItems().setAll(inv);
        });

        Button prodById = new Button("Producto por ID");
        prodById.setOnAction(ev -> {
            TextInputDialog idD = new TextInputDialog(); idD.setHeaderText("ID producto:");
            idD.showAndWait().ifPresent(idStr -> { try { int id = Integer.parseInt(idStr.trim()); com.tienda.reporte.ReporteModel r = new com.tienda.reporte.ReporteDAO().productoVendidoPorId(id); if (r != null) table.getItems().setAll(r); else new Alert(AlertType.INFORMATION, "Sin resultados").show(); } catch (Exception ex) { new Alert(AlertType.ERROR, "ID inválido").show(); } });
        });

        Button byCat = new Button("Productos por categoría");
        byCat.setOnAction(ev -> {
            TextInputDialog catD = new TextInputDialog(); catD.setHeaderText("Categoría (parcial):");
            catD.showAndWait().ifPresent(cat -> table.getItems().setAll(new com.tienda.reporte.ReporteDAO().productosVendidosPorCategoria(cat)));
        });

        HBox controls = new HBox(10, new Label("Período:"), periodCombo, generate, invBtn, prodById, byCat);
        controls.setPadding(new Insets(0, 0, 10, 0));

        VBox main = new VBox(10, controls, table);
        main.setPadding(new Insets(18));
        main.getStyleClass().add("card");
        main.setFillWidth(true);
        VBox.setVgrow(table, Priority.ALWAYS);
        generate.fire();

        return new BorderPane(main);
    }

    private String buildReport() {
        List<VentaModel> ventas = new VentaDAO().listarTodos();
        double totalVentas = ventas.stream().mapToDouble(VentaModel::getTotal).sum();
        return String.format("Ventas registradas: %d\nTotal facturado: $%.2f\n", ventas.size(), totalVentas);
    }

    private <T> TableColumn<T, Object> createColumn(String title, String property, int width) {
        TableColumn<T, Object> column = new TableColumn<>(title);
        column.setCellValueFactory(new PropertyValueFactory<>(property));
        column.setPrefWidth(width);
        return column;
    }

    private void updateTotalLabel(ObservableList<ProductoModel> cartItems, Label label) {
        double total = cartItems.stream().mapToDouble(p -> p.getPrecio() * p.getCantidad()).sum();
        label.setText("Total: $" + String.format("%.2f", total));
    }
}
