package com.caso1ias.core.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.caso1ias.core.model.Reporte;

public interface ReporteRepository extends CrudRepository<Reporte,Long>{
	List<Reporte> findByIdtecnico(String id);
}
