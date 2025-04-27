package com.function.application.usecase;

import com.function.domain.model.User;
import com.function.infraestructure.repository.UserRepository;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

/**
 * Caso de uso para la consulta de usuarios.
 */
public class GetUserUseCase {
    private final UserRepository repository;
    private final PublishDomainEventUseCase publishDomainEventUseCase;
    private final Logger logger = Logger.getLogger(GetUserUseCase.class.getName());

    /**
     * Constructor que inicializa el repositorio y el publicador de eventos.
     * @param repository Repositorio de usuarios
     * @param publishDomainEventUseCase Caso de uso para publicar eventos
     */
    public GetUserUseCase(UserRepository repository, PublishDomainEventUseCase publishDomainEventUseCase) {
        this.repository = repository;
        this.publishDomainEventUseCase = publishDomainEventUseCase;
    }

    /**
     * Obtiene todos los usuarios registrados.
     * @return Lista de usuarios
     */
    public List<User> findAll() {
        logger.info("[GetUserUseCase] Inicio de consulta de todos los usuarios");
        List<User> users = repository.findAll();
        logger.info("[GetUserUseCase] Fin de consulta de todos los usuarios");
        return users;
    }

    /**
     * Busca un usuario por su ID.
     * @param id ID del usuario a buscar
     * @return Optional con el usuario si existe
     */
    public Optional<User> findById(int id) {
        logger.info("[GetUserUseCase] Buscando usuario con ID: " + id);
        Optional<User> userOpt = repository.findById(id);
        logger.info("[GetUserUseCase] Resultado de búsqueda: " + (userOpt.isPresent() ? "Encontrado" : "No encontrado"));
        return userOpt;
    }
}