package com.microservices.multiplication.entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Created by mihir.desai on 2/28/2018.
 */
@RequiredArgsConstructor
@Getter
@ToString
@EqualsAndHashCode
@Entity
public final class Multiplication {

    @Id
    @GeneratedValue
    @Column(name = "MULTIPLICATION_ID")
    private Long id;

    private final int factorA;
    private final int factorB;
    private int result;

    public Multiplication() {
        this(0,0);
    }

    public int getResult() {
        return this.factorA * this.factorB;
    }

}
