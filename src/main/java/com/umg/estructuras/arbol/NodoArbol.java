package com.umg.estructuras.arbol;

import com.umg.gestiontareas.modelo.Tarea;
import java.util.ArrayList;
import java.util.List;

public class NodoArbolTarea {
    private Tarea tarea;
    private List<NodoArbolTarea> hijos;

    public NodoArbolTarea(Tarea tarea) {
        this.tarea = tarea;
        this.hijos = new ArrayList<>();
    }

    public Tarea getTarea() {
        return tarea;
    }

    public void setTarea(Tarea tarea) {
        this.tarea = tarea;
    }

    public List<NodoArbolTarea> getHijos() {
        return hijos;
    }

    public void agregarHijo(NodoArbolTarea hijo) {
        this.hijos.add(hijo);
    }
}
