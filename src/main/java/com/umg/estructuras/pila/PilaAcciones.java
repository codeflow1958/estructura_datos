package com.umg.estructuras.pila;

import java.util.logging.Level;
import java.util.logging.Logger;

public class PilaAcciones<T> {
    private static final Logger LOGGER = Logger.getLogger(PilaAcciones.class.getName());
    private NodoPila<T> cima;

    public PilaAcciones() {
        this.cima = null;
        LOGGER.log(Level.INFO, "Pila de acciones creada.");
    }

    public void push(T dato) {
        LOGGER.log(Level.INFO, "Intentando hacer push del dato: {0}", dato);
        NodoPila<T> nuevoNodo = new NodoPila<>(dato);
        nuevoNodo.siguiente = cima;
        cima = nuevoNodo;
        LOGGER.log(Level.INFO, "Push exitoso. Nueva cima: {0}", cima.dato);
    }

    public T pop() {
        LOGGER.log(Level.INFO, "Intentando hacer pop.");
        if (isEmpty()) {
            LOGGER.log(Level.WARNING, "La pila está vacía, no se puede hacer pop.");
            System.out.println("La pila está vacía, no se puede hacer pop.");
            return null;
        }
        T datoCima = cima.dato;
        cima = cima.siguiente;
        LOGGER.log(Level.INFO, "Pop exitoso. Dato eliminado: {0}. Nueva cima: {1}", new Object[]{datoCima, cima != null ? cima.dato : null});
        return datoCima;
    }

    public T peek() {
        LOGGER.log(Level.INFO, "Intentando ver la cima (peek).");
        if (isEmpty()) {
            LOGGER.log(Level.WARNING, "La pila está vacía, no se puede ver la cima.");
            System.out.println("La pila está vacía, no se puede ver la cima.");
            return null;
        }
        LOGGER.log(Level.INFO, "Peek exitoso. Cima: {0}", cima.dato);
        return cima.dato;
    }

    public boolean isEmpty() {
        LOGGER.log(Level.FINE, "Verificando si la pila está vacía.");
        boolean resultado = cima == null;
        LOGGER.log(Level.FINE, "La pila está vacía: {0}", resultado);
        return resultado;
    }

    public int size() {
        LOGGER.log(Level.FINE, "Calculando el tamaño de la pila.");
        int contador = 0;
        NodoPila<T> actual = cima;
        while (actual != null) {
            contador++;
            actual = actual.siguiente;
        }
        LOGGER.log(Level.FINE, "Tamaño de la pila: {0}", contador);
        return contador;
    }
}