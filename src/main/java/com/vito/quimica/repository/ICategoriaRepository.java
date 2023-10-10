package com.vito.quimica.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vito.quimica.model.Categoria;

public interface ICategoriaRepository extends JpaRepository<Categoria, Integer> {

}
