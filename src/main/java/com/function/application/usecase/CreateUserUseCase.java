package com.function.application.usecase;

import com.function.domain.model.User;
import com.function.domain.model.DomainEvent;
import com.function.domain.model.event.UserCreatedData;
import com.function.infraestructure.repository.UserRepository;
import java.time.OffsetDateTime;
import java.util.UUID;
import java.util.logging.Logger;

public class CreateUserUseCase {
    private final UserRepository repository;
    private final PublishDomainEventUseCase publishDomainEventUseCase;
    private final Logger logger = Logger.getLogger(CreateUserUseCase.class.getName());

    public CreateUserUseCase(UserRepository repository, PublishDomainEventUseCase publishDomainEventUseCase) {
        this.repository = repository;
        this.publishDomainEventUseCase = publishDomainEventUseCase;
    }

    public boolean execute(User user) {
        boolean created = repository.create(user);
        if (created) {
            logger.info("Usuario creado exitosamente en BD. Procediendo a publicar evento UserCreated.");
            DomainEvent event = new DomainEvent(
                "user.created",
                new UserCreatedData((long)user.getId(), user.getNombre(), user.getEmail())
            );
            publishDomainEventUseCase.execute(event);
        }
        return created;
    }
}