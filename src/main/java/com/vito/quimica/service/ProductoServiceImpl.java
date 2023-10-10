package com.vito.quimica.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vito.quimica.model.Producto;
import com.vito.quimica.repository.iProductoRepository;

@Service
public class ProductoServiceImpl implements ProductoService {

	@Autowired
	private iProductoRepository iProductoRepository;

	@Override
	public Producto save(Producto producto) {
		return iProductoRepository.save(producto);
	}

	@Override
	public Optional<Producto> get(Integer id) {

		return iProductoRepository.findById(id);
	}

	@Override
	public void update(Producto producto) {
		iProductoRepository.save(producto);

	}

	@Override
	public void delete(Integer id) {
		iProductoRepository.deleteById(id);

	}

	@Override
	public List<Producto> findAll() {

		return iProductoRepository.findAll();
	}

}
