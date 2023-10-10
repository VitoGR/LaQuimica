package com.vito.quimica.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vito.quimica.model.Marca;
import com.vito.quimica.repository.IMarcaRepository;

@Service
public class MarcaServiceImpl implements IMarcaService {
	
	@Autowired
	private IMarcaRepository marcaRepository;

	@Override
	public List<Marca> listaMarcas() {
	
		return (List<Marca>) marcaRepository.findAll();
	}

}
