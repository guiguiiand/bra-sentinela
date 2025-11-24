package com.projeto.authservice.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "tb_scope")
public class Scope {

    @Id
    @Column(name = "scope_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    public Scope() {
    }

    public Scope(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
