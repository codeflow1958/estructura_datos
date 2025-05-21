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

    public NodoArbolTarea<T> obtenerRaiz() {
        return raiz;
    }

    public void setRaiz(NodoArbolTarea<T> raiz) {
        this.raiz = raiz;
    }

    public void agregarTarea(T dato, Long idPadre) {
        LOGGER.log(Level.INFO, "Intentando agregar tarea al árbol. Dato: {0}, ID Padre: {1}", new Object[]{dato, idPadre});
        NodoArbolTarea<T> nuevoNodo = new NodoArbolTarea<>(dato);
        if (raiz == null) {
            raiz = nuevoNodo;
            LOGGER.log(Level.INFO, "Tarea '{0}' agregada como raíz del árbol.", dato);
        } else {
            NodoArbolTarea<T> padre = buscarNodoPorId(raiz, idPadre);
            if (padre != null) {
                padre.agregarHijo(nuevoNodo);
                LOGGER.log(Level.INFO, "Tarea '{0}' agregada como hijo de '{1}' (ID: {2}).", new Object[]{dato, padre.getDato(), idPadre});
            } else {
                LOGGER.log(Level.WARNING, "No se encontró el padre con ID: {0} para agregar la tarea '{1}'. Agregando como raíz.", new Object[]{idPadre, dato});
                // Si el padre no se encuentra, podemos decidir agregarla como raíz o lanzar una excepción.
                // Por ahora, la agregamos como raíz si no se encuentra el padre.
                raiz.agregarHijo(nuevoNodo); // O podrías simplemente agregarla a la raíz si no quieres que sea la raíz principal.
            }
        }
    }

    // Método auxiliar para buscar un nodo por su ID (usando reflexión)
    private NodoArbolTarea<T> buscarNodoPorId(NodoArbolTarea<T> nodo, Long id) {
        if (nodo == null) {
            return null;
        }
        try {
            Field idField = nodo.getDato().getClass().getDeclaredField("id");
            idField.setAccessible(true);
            Object idValue = idField.get(nodo.getDato());
            if (idValue != null && idValue.equals(id)) {
                LOGGER.log(Level.FINE, "Nodo encontrado con ID: {0}", id);
                return nodo;
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            LOGGER.log(Level.SEVERE, "Error al acceder al campo 'id' en la clase {0}: {1}", new Object[]{nodo.getDato().getClass().getName(), e.getMessage()});
            // Considera lanzar una excepción o manejar este caso de forma más robusta
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
        LOGGER.log(Level.INFO, "Buscando nodo en el árbol con ID: {0}", id);
        return buscarNodoPorId(raiz, id);
    }

    public List<T> obtenerTareasDelArbol() {
        LOGGER.log(Level.INFO, "Obteniendo todas las tareas del árbol.");
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

    /**
     * Elimina un nodo del árbol por el ID de su dato.
     * Si el nodo tiene hijos, estos también serán eliminados del árbol.
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
                raiz = null; // Simplemente hacemos la raíz nula
                LOGGER.log(Level.INFO, "Raíz del árbol eliminada con ID: {0}", idDato);
                return true;
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            LOGGER.log(Level.SEVERE, "Error al acceder al campo 'id' de la raíz: {0}", e.getMessage());
            return false;
        }

        return eliminarNodoRecursivo(raiz, idDato);
    }

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
                    hijos.remove(i); // Elimina el hijo de la lista
                    LOGGER.log(Level.INFO, "Nodo con ID: {0} eliminado del árbol. Sus hijos también son eliminados.", idDato);
                    return true;
                }
            } catch (NoSuchFieldException | IllegalAccessException e) {
                LOGGER.log(Level.SEVERE, "Error al acceder al campo 'id' del hijo: {0}", e.getMessage());
                return false; // O manejar de otra forma
            }

            // Si no es el hijo directo, busca recursivamente en los sub-hijos
            if (eliminarNodoRecursivo(hijo, idDato)) {
                return true;
            }
        }
        return false;
    }
}
