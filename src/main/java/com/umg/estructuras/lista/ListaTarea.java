package com.umg.estructuras.lista;

import java.util.logging.Level;
import java.util.logging.Logger;

public class ListaTarea<T> {
    private static final Logger LOGGER = Logger.getLogger(ListaTarea.class.getName());

    private NodoLista<T> cabeza; // El primer nodo de la lista
    private int tamano;

    public ListaTarea() {
        this.cabeza = null;
        this.tamano = 0;
        LOGGER.log(Level.INFO, "ListaTarea personalizada creada.");
    }

    /**
     * Agrega un elemento al final de la lista.
     * @param dato El dato a agregar.
     */
    public void agregar(T dato) {
        LOGGER.log(Level.INFO, "Intentando agregar dato a la lista: {0}", dato);
        NodoLista<T> nuevoNodo = new NodoLista<>(dato);
        if (cabeza == null) {
            cabeza = nuevoNodo;
        } else {
            NodoLista<T> actual = cabeza;
            while (actual.siguiente != null) {
                actual = actual.siguiente;
            }
            actual.siguiente = nuevoNodo;
        }
        tamano++;
        LOGGER.log(Level.INFO, "Dato agregado exitosamente. Tamaño actual de la lista: {0}", tamano);
    }

    /**
     * Elimina la primera ocurrencia de un dato de la lista.
     * @param dato El dato a eliminar.
     * @return true si el dato fue eliminado, false en caso contrario.
     */
    public boolean eliminar(T dato) {
        LOGGER.log(Level.INFO, "Intentando eliminar dato de la lista: {0}", dato);
        if (cabeza == null) {
            LOGGER.log(Level.WARNING, "La lista está vacía, no se puede eliminar el dato: {0}", dato);
            return false;
        }

        if (cabeza.dato.equals(dato)) {
            cabeza = cabeza.siguiente;
            tamano--;
            LOGGER.log(Level.INFO, "Dato '{0}' eliminado exitosamente del inicio de la lista. Tamaño: {1}", new Object[]{dato, tamano});
            return true;
        }

        NodoLista<T> actual = cabeza;
        while (actual.siguiente != null && !actual.siguiente.dato.equals(dato)) {
            actual = actual.siguiente;
        }

        if (actual.siguiente != null) {
            actual.siguiente = actual.siguiente.siguiente;
            tamano--;
            LOGGER.log(Level.INFO, "Dato '{0}' eliminado exitosamente de la lista. Tamaño: {1}", new Object[]{dato, tamano});
            return true;
        }
        LOGGER.log(Level.WARNING, "Dato '{0}' no encontrado en la lista para eliminar.", dato);
        return false;
    }

    /**
     * Busca un dato en la lista.
     * @param dato El dato a buscar.
     * @return true si el dato se encuentra en la lista, false en caso contrario.
     */
    public boolean buscar(T dato) {
        LOGGER.log(Level.INFO, "Intentando buscar dato en la lista: {0}", dato);
        NodoLista<T> actual = cabeza;
        while (actual != null) {
            if (actual.dato.equals(dato)) {
                LOGGER.log(Level.INFO, "Dato '{0}' encontrado en la lista.", dato);
                return true;
            }
            actual = actual.siguiente;
        }
        LOGGER.log(Level.WARNING, "Dato '{0}' no encontrado en la lista.", dato);
        return false;
    }

    /**
     * Obtiene el dato en una posición específica de la lista (basado en 0).
     * @param indice El índice del dato a obtener.
     * @return El dato en la posición especificada, o null si el índice está fuera de rango.
     */
    public T obtener(int indice) {
        LOGGER.log(Level.INFO, "Intentando obtener dato en el índice: {0}", indice);
        if (indice < 0 || indice >= tamano) {
            LOGGER.log(Level.SEVERE, "Índice fuera de rango: {0}. Tamaño de la lista: {1}", new Object[]{indice, tamano});
            return null; // O lanzar una excepción IndexOutOfBoundsException
        }
        NodoLista<T> actual = cabeza;
        for (int i = 0; i < indice; i++) {
            actual = actual.siguiente;
        }
        LOGGER.log(Level.INFO, "Dato obtenido en el índice {0}: {1}", new Object[]{indice, actual.dato});
        return actual.dato;
    }

    /**
     * Verifica si la lista está vacía.
     * @return true si la lista no contiene elementos, false en caso contrario.
     */
    public boolean estaVacia() {
        boolean resultado = cabeza == null;
        LOGGER.log(Level.FINE, "Verificando si la lista está vacía: {0}", resultado);
        return resultado;
    }

    /**
     * Devuelve el número de elementos en la lista.
     * @return El tamaño de la lista.
     */
    public int tamano() {
        LOGGER.log(Level.FINE, "Obteniendo tamaño de la lista: {0}", tamano);
        return tamano;
    }
}
