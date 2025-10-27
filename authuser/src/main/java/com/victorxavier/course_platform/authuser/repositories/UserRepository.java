package com.victorxavier.course_platform.authuser.repositories;

import com.victorxavier.course_platform.authuser.models.UserModel;
import org.springframework.data.domain.Page; // <- IMPORTAR ISTO
import org.springframework.data.domain.Pageable; // <- IMPORTAR ISTO
import org.springframework.data.jpa.domain.Specification; // <- IMPORTAR ISTO
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


public interface UserRepository extends JpaRepository<UserModel, UUID>, JpaSpecificationExecutor<UserModel> {

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    @EntityGraph(attributePaths = "roles", type = EntityGraph.EntityGraphType.FETCH)
    Optional<UserModel> findByUsername(String username);

    @EntityGraph(attributePaths = "roles", type = EntityGraph.EntityGraphType.FETCH)
    Optional<UserModel> findById(UUID userId);

    @Override
    @EntityGraph(attributePaths = "roles", type = EntityGraph.EntityGraphType.FETCH)
    Page<UserModel> findAll(Specification<UserModel> spec, Pageable pageable);




    @Override
    @EntityGraph(attributePaths = "roles", type = EntityGraph.EntityGraphType.FETCH)
    List<UserModel> findAll(Specification<UserModel> spec);
}