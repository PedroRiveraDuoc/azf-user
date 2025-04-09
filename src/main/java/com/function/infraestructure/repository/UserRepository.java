package com.function.infraestructure.repository;

import com.function.domain.model.User;
import java.util.List;
import java.util.Optional;

public interface UserRepository {
    boolean create(User user);
    List<User> findAll();
    Optional<User> findById(int id);
    boolean update(User user);
    boolean delete(int id);
}