package com.microservices.multiplication.repository;

import com.microservices.multiplication.entity.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

/**
 * Created by mihir.desai on 3/6/2018.
 */
public interface UserRepository extends CrudRepository<User,Long> {
    Optional<User> findByAlias(final String alias);
}
