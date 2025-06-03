package com.umg.estructuras.lista;

import java.util.logging.Level;
import java.util.logging.Logger;

public class NodoLista<T> { // <--- AÑADIR <T> AQUÍ
    private static final Logger LOGGER = Logger.getLogger(NodoLista.class.getName());

    public T dato;
    public NodoLista<T> siguiente; // <--- USAR <T> AQUÍ

    public NodoLista(T dato) {
        this.dato = dato;
        this.siguiente = null;
        LOGGER.log(Level.FINE, "NodoLista creado con dato: {0}", dato);
    }
}
