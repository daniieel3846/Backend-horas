package com.caso1ias.core.service;
import java.util.List;
import com.caso1ias.core.model.*;

public interface ReporteService {
	Reporte agregar(Reporte r);
	List<Reporte> buscarTecnico(String id);
}
