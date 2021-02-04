package com.caso1ias.core.controller;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.caso1ias.core.service.ReporteService;
import com.caso1ias.core.model.Reporte;

@CrossOrigin(origins="http://localhost:4200",maxAge=3600)
@RestController
public class Controlador {
	@Autowired
	ReporteService service;
	//---------------Modulo de Reporte de Servicio---------
	@PostMapping("/reporte")
	public String agregar(@Valid @RequestBody Reporte r, BindingResult result) {
		if(result.hasErrors()) {
			List<FieldError> errores = result.getFieldErrors();
		    for (FieldError error : errores ) {
		        return (error.getDefaultMessage());
		    }
		}
		service.agregar(r);
		return "Reporte Registrado Correctamente";
	}
	//---------------Modulo de Calculo de horas de trabajo--------------
	@GetMapping("/recibir/{idtecnico}/{numsemana}")
	public List<String> listarId(@PathVariable("idtecnico") String idtecnico,@PathVariable("numsemana") String numsemana) throws ParseException{
		ArrayList <Reporte> Listatecnico=new ArrayList<Reporte>(); //Arraylist de tipo Reporte
		ArrayList <String> horas=new ArrayList<String>();
		//String h="0";
		if(!service.buscarTecnico(idtecnico).isEmpty()){  //Invoca el metodo que busca el id del tecnico
			Listatecnico=(ArrayList<Reporte>) service.buscarTecnico(idtecnico); //Llena el arraylist con la info del tecnico
			Reporte r=new Reporte();
			//h="Total de Horas trabajadas: "+r.HorasSemanal(Listatecnico,numsemana);
			horas=(ArrayList<String>) r.HorasSemanalV2(Listatecnico,numsemana);
		}
		return horas;
	}
	/*
	@GetMapping("/prueba")
	public int retornando() throws ParseException {
		
		String miFecha="2021-02-07";
		//int numseman=4;
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		java.util.Date FechaI = formatter.parse(miFecha);
		Calendar objCalendario = Calendar.getInstance(); //Obtiene la fecha actual
		//int semanaActual= objCalendario.get(Calendar.WEEK_OF_YEAR); //semana actual
		objCalendario.setTime(FechaI); //enviar la fecha recibida
        int semanaTecnico = objCalendario.get(Calendar.WEEK_OF_YEAR)-1; // semana de la fecha del tecnico
		return semanaTecnico;
		
		//int mes = objCalendario.get(Calendar.MONTH)+1;
		//int dia = objCalendario.get(Calendar.DATE);
       // int annio = objCalendario.get(Calendar.YEAR);
	}*/
	
	/*
	@GetMapping("/prueba")
	public List<Reporte> buscarTecnico() {
		String fecha=null;
		ArrayList <Reporte> Listatecnico=new ArrayList<Reporte>();
		Listatecnico=(ArrayList<Reporte>) service.buscarTecnico("3");
		for(Reporte tecnico:Listatecnico) {
			fecha=tecnico.getFecha_inicio();
		}
		return Listatecnico;
	}
*/
}