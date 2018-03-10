package com.microservices.multiplication.repository;

import com.microservices.multiplication.entity.MultiplicationResultAttempt;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by mihir.desai on 3/6/2018.
 */
public interface MultiplicationResultAttemptRepository extends CrudRepository<MultiplicationResultAttempt,Long> {

    List<MultiplicationResultAttempt> findTop5ByUserAliasOrderByIdDesc(String alias);
}
