package com.umg.estructuras.cola;

import java.util.logging.Level;
import java.util.logging.Logger;

public class ColaTareasProgramadas<T> {
    private static final Logger LOGGER = Logger.getLogger(ColaTareasProgramadas.class.getName());

    private NodoCola<T> frente; // El primer elemento de la cola
    private NodoCola<T> fin;    // El último elemento de la cola
    private int tamano;

    public ColaTareasProgramadas() {
        this.frente = null;
        this.fin = null;
        this.tamano = 0;
        LOGGER.log(Level.INFO, "Cola de tareas programadas creada.");
    }

    /**
     * Agrega un elemento al final de la cola (enqueue).
     * @param dato El dato a agregar.
     */
    public void enqueue(T dato) {
        LOGGER.log(Level.INFO, "Intentando agregar (enqueue) dato: {0}", dato);
        NodoCola<T> nuevoNodo = new NodoCola<>(dato);
        if (isEmpty()) {
            frente = nuevoNodo;
        } else {
            fin.siguiente = nuevoNodo;
        }
        fin = nuevoNodo;
        tamano++;
        LOGGER.log(Level.INFO, "Dato agregado exitosamente. Tamaño actual de la cola: {0}", tamano);
    }

    /**
     * Elimina y devuelve el elemento del frente de la cola (dequeue).
     * @return El dato eliminado del frente, o null si la cola está vacía.
     */
    public T dequeue() {
        LOGGER.log(Level.INFO, "Intentando sacar (dequeue) un dato.");
        if (isEmpty()) {
            LOGGER.log(Level.WARNING, "La cola está vacía, no se puede hacer dequeue.");
            // Podrías lanzar una excepción aquí en un entorno de producción, como new IllegalStateException("La cola está vacía");
            return null;
        }
        T datoFrente = frente.dato;
        frente = frente.siguiente; // Mueve el frente al siguiente nodo

        if (frente == null) { // Si la cola quedó vacía después de dequeue
            fin = null;
        }
        tamano--;
        LOGGER.log(Level.INFO, "Dato sacado exitosamente: {0}. Tamaño actual de la cola: {1}", new Object[]{datoFrente, tamano});
        return datoFrente;
    }

    /**
     * Devuelve el elemento del frente de la cola sin eliminarlo (peek).
     * @return El dato del frente, o null si la cola está vacía.
     */
    public T peek() {
        LOGGER.log(Level.FINE, "Intentando ver el frente (peek).");
        if (isEmpty()) {
            LOGGER.log(Level.WARNING, "La cola está vacía, no se puede ver el frente.");
            return null;
        }
        LOGGER.log(Level.FINE, "Frente de la cola: {0}", frente.dato);
        return frente.dato;
    }

    /**
     * Verifica si la cola está vacía.
     * @return true si la cola está vacía, false en caso contrario.
     */
    public boolean isEmpty() {
        LOGGER.log(Level.FINE, "Verificando si la cola está vacía.");
        boolean resultado = frente == null;
        LOGGER.log(Level.FINE, "La cola está vacía: {0}", resultado);
        return resultado;
    }

    /**
     * Devuelve el número de elementos en la cola.
     * @return El tamaño de la cola.
     */
    public int size() {
        LOGGER.log(Level.FINE, "Calculando el tamaño de la cola.");
        LOGGER.log(Level.FINE, "Tamaño de la cola: {0}", tamano);
        return tamano;
    }
}
