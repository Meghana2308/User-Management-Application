package com.springboot.usermanagementsystemapplication.repository;

import com.springboot.usermanagementsystemapplication.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    @EntityGraph(attributePaths = {"orders", "profile"})
    Optional<User> findWithOrdersById(Long id);

    @EntityGraph(attributePaths = {"orders", "profile"})
    Page<User> findAll(Pageable pageable);
}
