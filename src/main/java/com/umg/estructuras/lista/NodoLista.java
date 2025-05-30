package com.umg.estructuras.lista;

import java.util.logging.Level;
import java.util.logging.Logger;

public class NodoLista<T> {
    private static final Logger LOGGER = Logger.getLogger(NodoLista.class.getName());

    public T dato;
    public NodoLista<T> siguiente;

    public NodoLista(T dato) {
        this.dato = dato;
        this.siguiente = null; // Al crear un nuevo nodo, inicialmente no apunta a nada
        LOGGER.log(Level.FINE, "NodoLista creado con dato: {0}", dato);
    }
}
