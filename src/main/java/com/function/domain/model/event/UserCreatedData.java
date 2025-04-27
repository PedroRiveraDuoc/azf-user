package com.function.domain.model.event;

public class UserCreatedData {
    private final long id;
    private final String nombre;
    private final String email;

    public UserCreatedData(long id, String nombre, String email) {
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