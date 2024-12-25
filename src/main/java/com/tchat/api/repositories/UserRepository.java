package com.tchat.api.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.tchat.api.entities.User;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    Optional<User> findByUsername(String username);

    @Query("SELECT a FROM User a WHERE a.email = ?1 OR a.username = ?1")
    Optional<User> findByUsernameOrEmail(String data);
}
