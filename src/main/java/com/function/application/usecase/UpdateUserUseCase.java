package com.function.application.usecase;

import com.function.domain.model.User;
import com.function.infraestructure.repository.UserRepository;

public class UpdateUserUseCase {
    private final UserRepository repository;

    public UpdateUserUseCase(UserRepository repository) {
        this.repository = repository;
    }

    public boolean execute(User user) {
        return repository.update(user);
    }
}