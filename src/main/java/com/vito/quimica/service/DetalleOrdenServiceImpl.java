package com.vito.quimica.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vito.quimica.model.DetalleOrden;
import com.vito.quimica.repository.IDetalleOrdenRepository;

@Service
public class DetalleOrdenServiceImpl implements IDetalleOrdenService {

	
	@Autowired
	private IDetalleOrdenRepository detalleOrdenRepo;
	@Override
	public DetalleOrden save(DetalleOrden detalleOrden) {
		
		return detalleOrdenRepo.save(detalleOrden);
	}

}
