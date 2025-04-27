package com.function.application.usecase;

import com.function.domain.model.User;
import com.function.domain.model.DomainEvent;
import com.function.domain.model.event.UserUpdatedData;
import com.function.infraestructure.repository.UserRepository;
import java.util.logging.Logger;

public class UpdateUserUseCase {
    private final UserRepository repository;
    private final PublishDomainEventUseCase publishDomainEventUseCase;
    private final Logger logger = Logger.getLogger(UpdateUserUseCase.class.getName());

    public UpdateUserUseCase(UserRepository repository, PublishDomainEventUseCase publishDomainEventUseCase) {
        this.repository = repository;
        this.publishDomainEventUseCase = publishDomainEventUseCase;
    }

    public boolean execute(User user) {
        boolean updated = repository.update(user);
        if (updated) {
            logger.info("Usuario actualizado exitosamente en BD. Procediendo a publicar evento UserUpdated.");
            DomainEvent event = new DomainEvent(
                "user.updated",
                new UserUpdatedData((long)user.getId(), user.getNombre(), user.getEmail())
            );
            publishDomainEventUseCase.execute(event);
        }
        return updated;
    }
}