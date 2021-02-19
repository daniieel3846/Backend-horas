package com.caso1ias.core.interfaceService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.caso1ias.core.model.Reporte;
import com.caso1ias.core.repository.ReporteRepository;
import com.caso1ias.core.service.ReporteService;


@Service
public class ReporteServiceImp implements ReporteService {
	
	
	@Autowired
	ReporteRepository repositorio;
	
	@Override
	public Reporte agregar(Reporte r) {
		return repositorio.save(r);
	}
	@Override
	public List<Reporte> buscarTecnico(String id) {
		return repositorio.findByIdtecnico(id);
	}
}
