package com.function.application.usecase;

import com.function.domain.model.User;
import com.function.infraestructure.repository.UserRepository;

import java.util.List;
import java.util.Optional;

public class GetUserUseCase {
    private final UserRepository repository;

    public GetUserUseCase(UserRepository repository) {
        this.repository = repository;
    }

    public List<User> getAll() {
        return repository.findAll();
    }

    public Optional<User> getById(int id) {
        return repository.findById(id);
    }
}