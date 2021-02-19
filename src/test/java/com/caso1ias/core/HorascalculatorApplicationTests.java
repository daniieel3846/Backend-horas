package com.caso1ias.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.caso1ias.core.interfaceService.ReporteServiceImp;
import com.caso1ias.core.model.Reporte;

import com.caso1ias.core.repository.ReporteRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
public class HorascalculatorApplicationTests {
	
	@InjectMocks
	ReporteServiceImp reporteService;
	
	@Mock   //Datos mockeados
	ReporteRepository repositorio;
	
	@Test
	public void GuardarReporte() {
		Reporte report=new Reporte("1","1","2021-02-19","07:00","2021-02-19","15:00");
		when(repositorio.save(report)).thenReturn(report);
		assertEquals(report,reporteService.agregar(report));
	}
	
	@Test
	public void BuscarTecnico() {
		String idTenico="1";
		when(repositorio.findByIdtecnico(idTenico)).thenReturn(Stream
				.of(new Reporte("1","1","2021-02-19","07:00","2021-02-19","15:00")).collect(Collectors.toList()));
		assertEquals(1,reporteService.buscarTecnico(idTenico).size());
		
	}

}
