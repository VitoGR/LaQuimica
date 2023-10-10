package com.vito.quimica.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vito.quimica.model.Categoria;
import com.vito.quimica.repository.ICategoriaRepository;
@Service
public class ICategoriaServiceImpl implements ICategoriaService {

	@Autowired
	private ICategoriaRepository categoriaRepository;
	
	@Override
	public List<Categoria> listaCategoria() {
		
		return categoriaRepository.findAll();
	}

}
