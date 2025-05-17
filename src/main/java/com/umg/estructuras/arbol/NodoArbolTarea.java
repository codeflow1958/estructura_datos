package com.umg.estructuras.arbol;

import java.util.ArrayList;
import java.util.List;

public class NodoArbolTarea<T> {
    private T dato;
    private List<NodoArbolTarea<T>> hijos;

    public NodoArbolTarea(T dato) {
        this.dato = dato;
        this.hijos = new ArrayList<>();
    }

    public T getDato() {
        return dato;
    }

    public void setDato(T dato) {
        this.dato = dato;
    }

    public List<NodoArbolTarea<T>> getHijos() {
        return hijos;
    }

    public void agregarHijo(NodoArbolTarea<T> hijo) {
        this.hijos.add(hijo);
    }
}
