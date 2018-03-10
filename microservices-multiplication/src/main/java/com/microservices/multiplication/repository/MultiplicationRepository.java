package com.microservices.multiplication.repository;

import com.microservices.multiplication.entity.Multiplication;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

/**
 * Created by mihir.desai on 3/6/2018.
 */
public interface MultiplicationRepository extends CrudRepository<Multiplication,Long> {
    Optional<Multiplication> findByFactorAAndFactorB(int factorA, int factorB);
}
