package com.projeto.authservice.repository;

import com.projeto.authservice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {

    Optional<User> findByUsername(String username);

    @Modifying
    @Query("update User u set u.tokenVersion = u.tokenVersion + 1 where u.id = :userId")
    void incrementTokenVersion(Long userId);

    @Query("select u.tokenVersion from User u where u.id = :userId")
    Integer findTokenVersionByUserId(Long userId);
}
