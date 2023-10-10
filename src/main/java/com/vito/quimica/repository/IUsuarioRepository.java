package com.vito.quimica.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vito.quimica.model.Usuario;


public interface IUsuarioRepository extends JpaRepository<Usuario, Integer> {

	Optional<Usuario> findByEmail(String email);
	Optional<Usuario> findByUsername(String nombre);
	
	
}
