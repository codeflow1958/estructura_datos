package com.umg.estructuras.pila;

public class NodoPila<T> {
    public T dato;
    public NodoPila<T> siguiente;

    public NodoPila(T dato) {
        this.dato = dato;
        this.siguiente = null; // Al crear un nuevo nodo, inicialmente no apunta a nada
    }
}