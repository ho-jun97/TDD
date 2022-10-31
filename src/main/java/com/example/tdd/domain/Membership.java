package com.example.tdd.domain;

import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "MEMBERSHIP")
@NoArgsConstructor
public class Membership {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
}
