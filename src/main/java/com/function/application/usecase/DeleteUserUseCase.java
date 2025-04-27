package com.function.application.usecase;

import com.function.domain.model.User;
import com.function.domain.model.DomainEvent;
import com.function.domain.model.event.UserDeletedData;
import com.function.infraestructure.repository.UserRepository;
import java.util.Optional;
import java.util.logging.Logger;

public class DeleteUserUseCase {
    private final UserRepository repository;
    private final PublishDomainEventUseCase publishDomainEventUseCase;
    private final Logger logger = Logger.getLogger(DeleteUserUseCase.class.getName());

    public DeleteUserUseCase(UserRepository repository, PublishDomainEventUseCase publishDomainEventUseCase) {
        this.repository = repository;
        this.publishDomainEventUseCase = publishDomainEventUseCase;
    }

    public boolean execute(int id) {
        // Obtener usuario antes de eliminar para poblar el evento
        Optional<User> userOpt = repository.findById(id);
        boolean deleted = repository.delete(id);
        if (deleted && userOpt.isPresent()) {
            User user = userOpt.get();
            logger.info("Usuario eliminado exitosamente en BD. Procediendo a publicar evento UserDeleted.");
            DomainEvent event = new DomainEvent(
                "user.deleted",
                new UserDeletedData((long)user.getId(), user.getNombre(), user.getEmail())
            );
            publishDomainEventUseCase.execute(event);
        }
        return deleted;
    }
}