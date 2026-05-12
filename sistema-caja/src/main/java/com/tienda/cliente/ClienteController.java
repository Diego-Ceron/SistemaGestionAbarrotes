package com.tienda.cliente;

public class ClienteController {

    private IClienteDAO clienteDAO;

    // Constructor
    public ClienteController(IClienteDAO clienteDAO) {
        this.clienteDAO = clienteDAO;
    }

    // Registrar cliente
    public void registrarCliente(ClienteModel c) {
        clienteDAO.registrar(c);
    }

    // Actualizar cliente
    public void actualizarCliente(ClienteModel c) {
        clienteDAO.actualizar(c);
    }

    // Buscar cliente por ID
    public ClienteModel buscarClientes(int id) {
        return clienteDAO.buscar(id);
    }

    // Listar clientes frecuentes
    public void listarClientesFrecuentes() {
        clienteDAO.listarFrecuentes();
    }
}