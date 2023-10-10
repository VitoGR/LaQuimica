package com.vito.quimica.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vito.quimica.model.DetalleOrden;

public interface IDetalleOrdenRepository extends JpaRepository<DetalleOrden, Integer> {

}
