package com.vito.quimica.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vito.quimica.model.Producto;

public interface iProductoRepository extends JpaRepository<Producto, Integer> {

}
