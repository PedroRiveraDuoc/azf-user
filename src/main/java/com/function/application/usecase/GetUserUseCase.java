package com.function.application.usecase;

import com.function.domain.model.User;
import com.function.infraestructure.repository.UserRepository;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

public class GetUserUseCase {
    private final UserRepository repository;
    private final PublishDomainEventUseCase publishDomainEventUseCase;
    private final Logger logger = Logger.getLogger(GetUserUseCase.class.getName());

    public GetUserUseCase(UserRepository repository, PublishDomainEventUseCase publishDomainEventUseCase) {
        this.repository = repository;
        this.publishDomainEventUseCase = publishDomainEventUseCase;
    }

    public List<User> findAll() {
        logger.info("Obteniendo todos los usuarios");
        return repository.findAll();
    }

    public Optional<User> findById(int id) {
        logger.info("Buscando usuario con ID: " + id);
        return repository.findById(id);
    }
}