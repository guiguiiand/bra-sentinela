package com.projeto.authservice.repository;

import com.projeto.authservice.entity.Role;
import com.projeto.authservice.entity.Scope;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface RoleRepository extends JpaRepository<Role,Long> {
    Optional<Role> findByName(String name);

    List<Role> scopes(Set<Scope> scopes);
}
