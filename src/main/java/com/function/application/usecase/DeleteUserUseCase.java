package com.function.application.usecase;

import com.function.domain.model.User;
import com.function.domain.model.DomainEvent;
import com.function.domain.model.event.UserDeletedData;
import com.function.infraestructure.repository.UserRepository;
import java.util.Optional;
import java.util.logging.Logger;

/**
 * Caso de uso para la eliminación de usuarios y publicación de evento de eliminación.
 */
public class DeleteUserUseCase {
    private final UserRepository repository;
    private final PublishDomainEventUseCase publishDomainEventUseCase;
    private final Logger logger = Logger.getLogger(DeleteUserUseCase.class.getName());

    /**
     * Constructor que inicializa el repositorio y el publicador de eventos.
     * @param repository Repositorio de usuarios
     * @param publishDomainEventUseCase Caso de uso para publicar eventos
     */
    public DeleteUserUseCase(UserRepository repository, PublishDomainEventUseCase publishDomainEventUseCase) {
        this.repository = repository;
        this.publishDomainEventUseCase = publishDomainEventUseCase;
    }

    /**
     * Ejecuta la eliminación de un usuario y publica el evento correspondiente.
     * @param id ID del usuario a eliminar
     * @return true si el usuario fue eliminado exitosamente
     */
    public boolean execute(int id) {
        logger.info("[DeleteUserUseCase] Inicio de ejecución");
        // Obtener usuario antes de eliminar para poblar el evento
        Optional<User> userOpt = repository.findById(id);
        boolean deleted = repository.delete(id);
        if (deleted && userOpt.isPresent()) {
            User user = userOpt.get();
            logger.info("Usuario eliminado exitosamente en BD. Publicando evento de eliminación.");
            DomainEvent event = new DomainEvent(
                "user.deleted",
                new UserDeletedData((long)user.getId(), user.getNombre(), user.getEmail())
            );
            publishDomainEventUseCase.execute(event);
        }
        logger.info("[DeleteUserUseCase] Fin de ejecución");
        return deleted;
    }
}