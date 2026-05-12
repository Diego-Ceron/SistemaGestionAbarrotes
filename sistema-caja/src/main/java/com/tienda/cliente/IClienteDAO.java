package com.tienda.cliente;

import java.util.List;

public interface IClienteDAO {
    void registrar(ClienteModel c);
    ClienteModel buscar(int id);
    void actualizar(ClienteModel c);
    List<ClienteModel> listarFrecuentes();
}