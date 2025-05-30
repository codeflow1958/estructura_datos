package com.umg.estructuras.cola;

import java.util.logging.Level;
import java.util.logging.Logger;

public class NodoCola<T> {
    private static final Logger LOGGER = Logger.getLogger(NodoCola.class.getName());

    public T dato;
    public NodoCola<T> siguiente;

    public NodoCola(T dato) {
        this.dato = dato;
        this.siguiente = null;
        LOGGER.log(Level.FINE, "NodoCola creado con dato: {0}", dato);
    }
}

