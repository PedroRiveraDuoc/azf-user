package com.function.application.usecase;

import com.function.domain.model.User;
import com.function.domain.model.DomainEvent;
import com.function.domain.model.event.UserCreatedData;
import com.function.infraestructure.repository.UserRepository;
import java.util.logging.Logger;

/**
 * Caso de uso para la creación de usuarios y publicación de evento de creación.
 */
public class CreateUserUseCase {
    private final UserRepository repository;
    private final PublishDomainEventUseCase publishDomainEventUseCase;
    private final Logger logger = Logger.getLogger(CreateUserUseCase.class.getName());

    /**
     * Constructor que inicializa el repositorio y el publicador de eventos.
     * @param repository Repositorio de usuarios
     * @param publishDomainEventUseCase Caso de uso para publicar eventos
     */
    public CreateUserUseCase(UserRepository repository, PublishDomainEventUseCase publishDomainEventUseCase) {
        this.repository = repository;
        this.publishDomainEventUseCase = publishDomainEventUseCase;
    }

    /**
     * Ejecuta la creación de un usuario y publica el evento correspondiente.
     * @param user Usuario a crear
     * @return true si el usuario fue creado exitosamente
     */
    public boolean execute(User user) {
        logger.info("[CreateUserUseCase] Inicio de ejecución");
        boolean created = repository.create(user);
        if (created) {
            logger.info("Usuario creado exitosamente en BD. Publicando evento de creación.");
            DomainEvent event = new DomainEvent(
                "user.created",
                new UserCreatedData((long)user.getId(), user.getNombre(), user.getEmail())
            );
            publishDomainEventUseCase.execute(event);
        }
        logger.info("[CreateUserUseCase] Fin de ejecución");
        return created;
    }
}