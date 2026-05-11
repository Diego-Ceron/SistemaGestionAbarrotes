package com.tienda.cliente;

public interface IClienteDAO {
    void registrar(ClienteModel c);
    ClienteModel buscar(int id);
    void actualizar(ClienteModel c);
    void listarFrecuentes();
}
