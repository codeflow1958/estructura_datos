package com.umg.estructuras.arbol;

import java.util.ArrayList;
import java.util.List;
import java.lang.reflect.Field; //Importamos la clase Field

public class ArbolJerarquicoTareas<T> {
    private NodoArbolTarea<T> raiz;

    public ArbolJerarquicoTareas() {
        this.raiz = null;
    }

    public void agregarTarea(T dato, Long idPadre) {
        NodoArbolTarea<T> nuevoNodo = new NodoArbolTarea<>(dato);
        if (raiz == null) {
            raiz = nuevoNodo;
        } else {
            NodoArbolTarea<T> padre = buscarNodoPorId(raiz, idPadre);
            if (padre != null) {
                padre.agregarHijo(nuevoNodo);
            } else {
                // Manejar el caso donde no se encuentra el padre (lanzar excepción, log, etc.)
                System.out.println("No se encontró el padre con ID: " + idPadre + " para agregar la tarea.");
            }
        }
    }

    private NodoArbolTarea<T> buscarNodoPorId(NodoArbolTarea<T> nodo, Long id) {
        try {
            Field idField = nodo.getDato().getClass().getDeclaredField("id");  // Obtiene el campo 'id' de forma genérica
            idField.setAccessible(true); // Permite acceder a campos privados
            Object idValue = idField.get(nodo.getDato());  // Obtiene el valor del campo 'id'
            if (idValue.equals(id)) {
                return nodo;
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            // Manejar la excepción si la clase T no tiene un campo 'id' o no se puede acceder a él.
            System.out.println("La clase " + nodo.getDato().getClass().getName() + " no tiene un campo 'id' o no se puede acceder a él: " + e.getMessage());
            return null;
        }

        for (NodoArbolTarea<T> hijo : nodo.getHijos()) {
            NodoArbolTarea<T> nodoEncontrado = buscarNodoPorId(hijo, id);
            if (nodoEncontrado != null) {
                return nodoEncontrado;
            }
        }
        return null;
    }

    public NodoArbolTarea<T> buscarNodoPorId(Long id) {
        return buscarNodoPorId(raiz, id);
    }

    public List<T> obtenerTareasDelArbol() {
        List<T> datos = new ArrayList<>();
        obtenerTareasDelArbol(raiz, datos);
        return datos;
    }

    private void obtenerTareasDelArbol(NodoArbolTarea<T> nodo, List<T> datos) {
        if (nodo != null) {
            datos.add(nodo.getDato());
            for (NodoArbolTarea<T> hijo : nodo.getHijos()) {
                obtenerTareasDelArbol(hijo, datos);
            }
        }
    }
}
