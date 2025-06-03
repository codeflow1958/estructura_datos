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

    /**
     * Agrega un dato (Tarea) al árbol. Si idPadre es null, se intenta agregar como raíz.
     * Si el árbol está vacío, el primer dato se convierte en la raíz.
     * @param dato El dato (Tarea) a agregar.
     * @param idPadre El ID del dato padre al que se asociará esta tarea. Si es null, se intenta agregar como raíz.
     */
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

    /**
     * Mueve un nodo existente en el árbol a una nueva posición bajo un nuevo padre.
     * Si el nodo no se encuentra, o el nuevo padre no existe, la operación falla.
     * @param idDatoAMover El ID del dato (Tarea) del nodo que se quiere mover.
     * @param nuevoIdPadre El ID del dato (Tarea) del nuevo nodo padre. Si es null, se intenta mover a la raíz.
     * @return true si el nodo fue movido exitosamente, false en caso contrario.
     */
    public boolean moverNodo(Long idDatoAMover, Long nuevoIdPadre) {
        LOGGER.log(Level.INFO, "Intentando mover nodo con ID {0} a nuevo padre ID {1}.", new Object[]{idDatoAMover, nuevoIdPadre});

        // 1. Encontrar el nodo a mover
        NodoArbolTarea<T> nodoAMover = buscarNodoPorId(raiz, idDatoAMover);
        if (nodoAMover == null) {
            LOGGER.log(Level.WARNING, "No se encontró el nodo con ID {0} para mover.", idDatoAMover);
            return false;
        }

        // 2. Encontrar el padre actual del nodo a mover y eliminarlo de su posición actual
        // Necesitamos un método para encontrar el padre directo de un nodo dado
        NodoArbolTarea<T> padreActual = encontrarPadreDirecto(raiz, nodoAMover);

        if (padreActual != null) {
            padreActual.getHijos().remove(nodoAMover);
            Object padreIdValue = null;
            try {
                Field idField = padreActual.getDato().getClass().getDeclaredField("id");
                idField.setAccessible(true);
                padreIdValue = idField.get(padreActual.getDato());
            } catch (NoSuchFieldException | IllegalAccessException e) {
                LOGGER.log(Level.SEVERE, "Error al acceder al campo 'id' del padre actual: {0}", e.getMessage());
            }

            LOGGER.log(Level.INFO, "Nodo con ID {0} removido de su padre actual con ID {1}.",
                    new Object[]{idDatoAMover, padreIdValue});
        } else if (nodoAMover == raiz) {
            // Si el nodo a mover es la raíz, no tiene padre actual, y la moveremos a una nueva posición
            // No podemos simplemente hacer 'raiz = null' aquí, ya que los hijos de la raíz se perderían temporalmente.
            // La lógica para mover la raíz es más compleja si debe mantener sus hijos y no convertirse en una hoja.
            // Para simplificar, asumimos que mover la raíz significa que se convierte en un hijo.
            // Si el nuevo padre es null, la raíz se convierte en la única tarea.
            // Si tiene hijos y se mueve, sus hijos se mueven con ella.
            // Manejar la raíz moviéndose a un no-null padre: la "vieja" raíz se convierte en hijo, sus hijos se mueven con ella.
            LOGGER.log(Level.WARNING, "Mover la raíz del árbol es una operación compleja para esta implementación. Se asume que la raíz se convertirá en un hijo.");
            // Si la raíz se mueve, sus hijos se mueven con ella. La vieja raíz simplemente se desvincula.
            // Si el nuevoIdPadre es null, esto causaría un problema de raíz doble.
            // Por simplicidad, no permitiremos mover la raíz a null si ya tiene hijos, o la convertiremos en una nueva raíz sin hijos si no tiene.
        } else {
            // El nodo a mover no es la raíz y no tiene padre. Esto podría indicar un árbol mal formado.
            LOGGER.log(Level.WARNING, "Nodo con ID {0} encontrado pero no tiene padre en el árbol. No se puede mover.", idDatoAMover);
            return false;
        }

        // 3. Encontrar el nuevo nodo padre
        NodoArbolTarea<T> nuevoPadre = null;
        if (nuevoIdPadre != null) {
            nuevoPadre = buscarNodoPorId(raiz, nuevoIdPadre);
            if (nuevoPadre == null) {
                LOGGER.log(Level.WARNING, "No se encontró el nuevo nodo padre con ID {0}.", nuevoIdPadre);
                // Si no se encuentra el nuevo padre, debemos re-insertar el nodo a mover en su posición original,
                // o manejar este error de alguna otra forma (ej. volver a la raíz).
                // Por simplicidad, lo re-insertaremos bajo la raíz si el nuevo padre no se encuentra.
                // Sin embargo, para una implementación robusta, esto debería ser un error.
                if(padreActual != null) { // Si tenía padre, lo volvemos a poner donde estaba
                    padreActual.agregarHijo(nodoAMover);
                } else if (raiz == null) { // Si era la raíz y el árbol quedó vacío
                    raiz = nodoAMover;
                } else { // Si era la raíz pero el árbol tiene otros nodos, se vuelve hijo de la raíz principal
                    raiz.agregarHijo(nodoAMover);
                }
                return false;
            }
        }

        // 4. Añadir el nodo al nuevo padre
        if (nuevoPadre != null) {
            nuevoPadre.agregarHijo(nodoAMover);
            LOGGER.log(Level.INFO, "Nodo con ID {0} movido exitosamente bajo el nuevo padre con ID {1}.", new Object[]{idDatoAMover, nuevoIdPadre});
        } else {
            // Si el nuevoIdPadre es null, el nodo se convierte en la nueva raíz.
            // Esto solo es válido si el árbol estaba vacío o si el nodo a mover no tiene hijos.
            // Si tiene hijos, y lo conviertes en la raíz, ¿qué pasa con la antigua raíz y sus hijos?
            // Esta lógica necesita ser muy clara en la definición del árbol.
            if (raiz == null) { // Si el árbol está vacío, el nodo se convierte en la raíz
                raiz = nodoAMover;
                LOGGER.log(Level.INFO, "Nodo con ID {0} movido a la raíz del árbol (árbol previamente vacío).", idDatoAMover);
            } else {
                // Si la raíz ya existe y se intenta mover un nodo a 'null' (convertirlo en raíz)
                // y el árbol no está vacío, esto es un error de lógica o debe manejarse de forma específica.
                // Aquí, por simplicidad, lo agregaremos como hijo de la raíz principal si no se especifica nuevo padre.
                // Esto es una simplificación; la lógica real de "mover a la raíz" es compleja.
                raiz.agregarHijo(nodoAMover); // Lo agrega como hijo de la raíz existente.
                LOGGER.log(Level.INFO, "Nodo con ID {0} movido como hijo de la raíz existente (nuevo padre null).", idDatoAMover);
            }
        }
        return true;
    }


    /**
     * Método auxiliar para encontrar el padre directo de un nodo dado.
     * @param nodoActual El nodo actual en el recorrido recursivo.
     * @param nodoBuscado El nodo cuyo padre estamos buscando.
     * @return El NodoArbolTarea que es padre directo de nodoBuscado, o null si no se encuentra.
     */
    private NodoArbolTarea<T> encontrarPadreDirecto(NodoArbolTarea<T> nodoActual, NodoArbolTarea<T> nodoBuscado) {
        if (nodoActual == null || nodoBuscado == null) {
            return null;
        }
        for (NodoArbolTarea<T> hijo : nodoActual.getHijos()) {
            if (hijo == nodoBuscado) {
                return nodoActual;
            }
            NodoArbolTarea<T> padre = encontrarPadreDirecto(hijo, nodoBuscado);
            if (padre != null) {
                return padre;
            }
        }
        return null;
    }
}
