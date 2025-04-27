package com.function.domain.model.event;

public class UserDeletedData {
    private final long id;
    private final String nombre;
    private final String email;

    public UserDeletedData(long id, String nombre, String email) {
        this.id = id;
        this.nombre = nombre;
        this.email = email;
    }

    public long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getEmail() {
        return email;
    }
} 