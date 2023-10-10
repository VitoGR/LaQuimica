package com.vito.quimica.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;


import com.vito.quimica.model.Orden;
import com.vito.quimica.model.Usuario;


public interface IOrdenRepository extends JpaRepository<Orden, Integer> {
	List<Orden> findByUsuario(Usuario usuario);
	

}
