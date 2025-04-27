package com.function.application.usecase;

import com.function.domain.model.User;
import com.function.domain.model.DomainEvent;
import com.function.domain.model.event.UserUpdatedData;
import com.function.infraestructure.repository.UserRepository;
import java.util.logging.Logger;

/**
 * Caso de uso para la actualización de usuarios y publicación de evento de actualización.
 */
public class UpdateUserUseCase {
    private final UserRepository repository;
    private final PublishDomainEventUseCase publishDomainEventUseCase;
    private final Logger logger = Logger.getLogger(UpdateUserUseCase.class.getName());

    /**
     * Constructor que inicializa el repositorio y el publicador de eventos.
     * @param repository Repositorio de usuarios
     * @param publishDomainEventUseCase Caso de uso para publicar eventos
     */
    public UpdateUserUseCase(UserRepository repository, PublishDomainEventUseCase publishDomainEventUseCase) {
        this.repository = repository;
        this.publishDomainEventUseCase = publishDomainEventUseCase;
    }

    /**
     * Ejecuta la actualización de un usuario y publica el evento correspondiente.
     * @param user Usuario a actualizar
     * @return true si el usuario fue actualizado exitosamente
     */
    public boolean execute(User user) {
        logger.info("[UpdateUserUseCase] Inicio de ejecución");
        boolean updated = repository.update(user);
        if (updated) {
            logger.info("Usuario actualizado exitosamente en BD. Publicando evento de actualización.");
            DomainEvent event = new DomainEvent(
                "user.updated",
                new UserUpdatedData((long)user.getId(), user.getNombre(), user.getEmail())
            );
            publishDomainEventUseCase.execute(event);
        }
        logger.info("[UpdateUserUseCase] Fin de ejecución");
        return updated;
    }
}