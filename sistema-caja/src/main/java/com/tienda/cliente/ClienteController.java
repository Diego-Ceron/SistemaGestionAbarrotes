package com.tienda.cliente;

import java.util.List;

public class ClienteController {

    private final IClienteDAO clienteDAO;

    public ClienteController(IClienteDAO clienteDAO) {
        this.clienteDAO = clienteDAO;
    }

    public void registrarCliente(ClienteModel c) {
        clienteDAO.registrar(c);
    }

    public void actualizarCliente(ClienteModel c) {
        clienteDAO.actualizar(c);
    }

    public ClienteModel buscarCliente(int id) {
        return clienteDAO.buscar(id);
    }

    public List<ClienteModel> listarClientesFrecuentes() {
        return clienteDAO.listarFrecuentes();
    }
}