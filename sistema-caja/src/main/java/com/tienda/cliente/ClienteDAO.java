package com.tienda.cliente;

public class ClienteDAO implements IClienteDAO {
    @Override
    public void registrar(ClienteModel c) {
        System.out.println("Cliente registrado: " + c.getNombre());
    }

    @Override
    public ClienteModel buscar(int id) {
        System.out.println("Buscando cliente con ID: " + id);
        return new ClienteModel(); // Simulación de búsqueda
    }

    @Override
    public void actualizar(ClienteModel c) {
        System.out.println("Cliente actualizado: " + c.getNombre());
    }

    @Override
    public void listarFrecuentes() {
        System.out.println("Listado de clientes frecuentes:");
        // Simulación de listado
        System.out.println("- Cliente A");
        System.out.println("- Cliente B");
    }
    
}
