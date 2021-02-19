package com.caso1ias.core;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.text.ParseException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.caso1ias.core.model.Reporte;
import com.caso1ias.core.reglasNegocio.Reglas_de_Negocio;
import com.caso1ias.core.repository.ReporteRepository;

@DataJpaTest
public class ReporteTest {
	
	@Autowired
	private ReporteRepository repositorio;
	
	@Test
	public void TestCreateReporte() {
		Reporte report=new Reporte("1","1","2021-02-08","07:00","2021-02-08","15:00");
		repositorio.save(report);
		assertNotNull(report);
	}
	
	@Test
	public void Find_Week_Year() throws ParseException {
		String fecha="2021-02-15";
		Reglas_de_Negocio RN=new Reglas_de_Negocio();
		assertTrue(RN.Week_Year(fecha)==(7));
	}
}
