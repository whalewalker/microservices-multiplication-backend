package com.multiplication.user.data.repository;

import com.multiplication.user.data.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByAlias(final String alias);

    List<User> findAllByIdIn(List<Long> idList);
}
