package com.umg.estructuras.arbol;

import java.util.ArrayList;
import java.util.List;
import java.lang.reflect.Field;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ArbolJerarquicoTareas<T> {
    private static final Logger LOGGER = Logger.getLogger(ArbolJerarquicoTareas.class.getName());

    private NodoArbolTarea<T> raiz;



    public ArbolJerarquicoTareas() {
        this.raiz = null;
        LOGGER.log(Level.INFO, "ArbolJerarquicoTareas creado.");
    }

    /**
     * Obtiene el nodo raíz del árbol.
     * @return El nodo raíz.
     */
    public NodoArbolTarea<T> obtenerRaiz() {
        return raiz;
    }

    /**
     * Establece el nodo raíz del árbol.
     * @param raiz El nodo a establecer como raíz.
     */
    public void setRaiz(NodoArbolTarea<T> raiz) {
        this.raiz = raiz;
    }

    public void agregarTarea(T dato, Long idPadre) {
        LOGGER.log(Level.INFO, "Intentando agregar dato al árbol. Dato: {0}, ID Padre: {1}", new Object[]{dato, idPadre});
        NodoArbolTarea<T> nuevoNodo = new NodoArbolTarea<>(dato);

        if (raiz == null) {
            raiz = nuevoNodo;
            LOGGER.log(Level.INFO, "Dato '{0}' agregado como raíz del árbol.", dato);
        } else {
            if (idPadre == null) {
                // Si la raíz ya existe y se intenta agregar sin padre, se agrega como hijo de la raíz principal
                LOGGER.log(Level.WARNING, "El árbol ya tiene una raíz. Dato '{0}' agregado como hijo de la raíz existente.", dato);
                raiz.agregarHijo(nuevoNodo);
            } else {
                NodoArbolTarea<T> padre = buscarNodoPorId(raiz, idPadre);
                if (padre != null) {
                    padre.agregarHijo(nuevoNodo);
                    LOGGER.log(Level.INFO, "Dato '{0}' agregado como hijo de '{1}' (ID: {2}).", new Object[]{dato, padre.getDato(), idPadre});
                } else {
                    LOGGER.log(Level.WARNING, "No se encontró el padre con ID: {0} para agregar el dato '{1}'. Agregando como hijo de la raíz principal.", new Object[]{idPadre, dato});
                    // Si el padre no se encuentra, se agrega como hijo de la raíz principal para no perderlo.
                    raiz.agregarHijo(nuevoNodo);
                }
            }
        }
    }

    /**
     * Busca un nodo en el árbol por el ID de su dato (Tarea).
     * Utiliza reflexión para acceder al campo 'id' del objeto genérico T.
     * @param nodo El nodo actual en la búsqueda recursiva.
     * @param id El ID del dato a buscar.
     * @return El NodoArbolTarea encontrado, o null si no se encuentra.
     */
    private NodoArbolTarea<T> buscarNodoPorId(NodoArbolTarea<T> nodo, Long id) {
        if (nodo == null) {
            return null;
        }
        try {
            // Usamos reflexión para obtener el campo 'id' del objeto T
            Field idField = nodo.getDato().getClass().getDeclaredField("id");
            idField.setAccessible(true); // Permite acceder a campos privados
            Object idValue = idField.get(nodo.getDato()); // Obtiene el valor del campo 'id'

            if (idValue != null && idValue.equals(id)) {
                LOGGER.log(Level.FINE, "Nodo encontrado con ID: {0}", id);
                return nodo;
            }
        } catch (NoSuchFieldException e) {
            LOGGER.log(Level.SEVERE, "La clase {0} no tiene un campo 'id'. Asegúrese de que su clase T tenga un campo 'id'.", nodo.getDato().getClass().getName());
            return null; // O lanzar una excepción específica
        } catch (IllegalAccessException e) {
            LOGGER.log(Level.SEVERE, "No se pudo acceder al campo 'id' en la clase {0}: {1}", new Object[]{nodo.getDato().getClass().getName(), e.getMessage()});
            return null; // O lanzar una excepción específica
        }

        for (NodoArbolTarea<T> hijo : nodo.getHijos()) {
            NodoArbolTarea<T> nodoEncontrado = buscarNodoPorId(hijo, id);
            if (nodoEncontrado != null) {
                return nodoEncontrado;
            }
        }
        return null;
    }

    /**
     * Inicia la búsqueda de un nodo por ID desde la raíz del árbol.
     * @param id El ID del dato a buscar.
     * @return El NodoArbolTarea encontrado, o null si no se encuentra.
     */
    public NodoArbolTarea<T> buscarNodoPorId(Long id) {
        LOGGER.log(Level.INFO, "Buscando nodo en el árbol con ID: {0}", id);
        if (raiz == null) {
            LOGGER.log(Level.INFO, "El árbol está vacío, no se puede buscar el nodo con ID: {0}", id);
            return null;
        }
        return buscarNodoPorId(raiz, id);
    }

    /**
     * Obtiene una lista plana de todos los datos (Tareas) presentes en el árbol.
     * @return Una lista de todos los datos en el árbol.
     */
    public List<T> obtenerTareasDelArbol() {
        LOGGER.log(Level.INFO, "Obteniendo todos los datos del árbol.");
        List<T> datos = new ArrayList<>();
        obtenerTareasDelArbolRecursivo(raiz, datos);
        return datos;
    }

    /**
     * Método recursivo para recorrer el árbol y recolectar todos los datos.
     * @param nodo El nodo actual en el recorrido.
     * @param datos La lista donde se recolectan los datos.
     */
    private void obtenerTareasDelArbolRecursivo(NodoArbolTarea<T> nodo, List<T> datos) {
        if (nodo != null) {
            datos.add(nodo.getDato());
            for (NodoArbolTarea<T> hijo : nodo.getHijos()) {
                obtenerTareasDelArbolRecursivo(hijo, datos);
            }
        }
    }

    /**
     * Elimina un nodo del árbol por el ID de su dato.
     * Si el nodo tiene hijos, estos también serán eliminados del árbol junto con el nodo padre.
     * @param idDato El ID del dato (Tarea) del nodo a eliminar.
     * @return true si el nodo fue eliminado, false en caso contrario.
     */
    public boolean eliminarNodoPorId(Long idDato) {
        LOGGER.log(Level.INFO, "Intentando eliminar nodo del árbol con ID de dato: {0}", idDato);
        if (raiz == null) {
            LOGGER.log(Level.WARNING, "El árbol está vacío, no se puede eliminar el nodo con ID: {0}", idDato);
            return false;
        }

        // Caso especial: eliminar la raíz
        try {
            Field idField = raiz.getDato().getClass().getDeclaredField("id");
            idField.setAccessible(true);
            Object raizIdValue = idField.get(raiz.getDato());
            if (raizIdValue != null && raizIdValue.equals(idDato)) {
                raiz = null; // La raíz se vuelve nula
                LOGGER.log(Level.INFO, "Raíz del árbol eliminada con ID: {0}", idDato);
                return true;
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            LOGGER.log(Level.SEVERE, "Error al acceder al campo 'id' de la raíz para eliminación: {0}", e.getMessage());
            return false;
        }

        return eliminarNodoRecursivo(raiz, idDato);
    }

    /**
     * Método recursivo para buscar y eliminar un nodo en el árbol.
     * @param nodoActual El nodo actual en el recorrido.
     * @param idDato El ID del dato a eliminar.
     * @return true si el nodo fue eliminado, false en caso contrario.
     */
    private boolean eliminarNodoRecursivo(NodoArbolTarea<T> nodoActual, Long idDato) {
        if (nodoActual == null) {
            return false;
        }

        List<NodoArbolTarea<T>> hijos = nodoActual.getHijos();
        for (int i = 0; i < hijos.size(); i++) {
            NodoArbolTarea<T> hijo = hijos.get(i);
            try {
                Field idField = hijo.getDato().getClass().getDeclaredField("id");
                idField.setAccessible(true);
                Object hijoIdValue = idField.get(hijo.getDato());

                if (hijoIdValue != null && hijoIdValue.equals(idDato)) {
                    hijos.remove(i); // Elimina el hijo de la lista de hijos del nodo actual
                    LOGGER.log(Level.INFO, "Nodo con ID: {0} eliminado del árbol. Sus hijos (si los tiene) también son desconectados.", idDato);
                    return true;
                }
            } catch (NoSuchFieldException | IllegalAccessException e) {
                LOGGER.log(Level.SEVERE, "Error al acceder al campo 'id' del hijo durante la eliminación: {0}", e.getMessage());
                return false;
            }

            // Si no es el hijo directo, busca recursivamente en los sub-hijos
            if (eliminarNodoRecursivo(hijo, idDato)) {
                return true;
            }
        }
        return false;
    }
}
