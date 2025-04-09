package com.function.application.usecase;

import com.function.infraestructure.repository.UserRepository;

public class DeleteUserUseCase {
    private final UserRepository repository;

    public DeleteUserUseCase(UserRepository repository) {
        this.repository = repository;
    }

    public boolean execute(int id) {
        return repository.delete(id);
    }
}