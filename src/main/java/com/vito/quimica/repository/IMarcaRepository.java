package com.vito.quimica.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vito.quimica.model.Marca;

public interface IMarcaRepository extends JpaRepository<Marca, Integer> {

}
