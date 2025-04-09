package com.function.application.usecase;

import com.function.domain.model.User;
import com.function.infraestructure.repository.UserRepository;

public class CreateUserUseCase {
    private final UserRepository repository;

    public CreateUserUseCase(UserRepository repository) {
        this.repository = repository;
    }

    public boolean execute(User user) {
        return repository.create(user);
    }
}