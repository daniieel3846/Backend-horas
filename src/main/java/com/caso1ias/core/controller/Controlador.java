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
import com.caso1ias.core.reglasNegocio.Reglas_de_Negocio;

@CrossOrigin(origins="http://localhost:4200",maxAge=3600)
@RestController
public class Controlador {
	@Autowired
	ReporteService service;
	//---------------Modulo de Reporte de Servicio---------
	@PostMapping("/reporte")
	public String agregar(@Valid @RequestBody Reporte r, BindingResult result) throws ParseException {
		
		//------Validacion de errores-------
		if(result.hasErrors()) {
			List<FieldError> errores = result.getFieldErrors();
		    for (FieldError error : errores ) {
		        return (error.getDefaultMessage());
		    }
		}
		
		//-------Validacion de datos-------------
		String fecha1=r.getFecha_inicio(),fecha2=r.getFecha_fin(),hora1=r.getHora_inicio(),hora2=r.getHora_fin();
		Reglas_de_Negocio RN=new Reglas_de_Negocio();
		String respuesta=RN.Validaciones(fecha1,fecha2,hora1,hora2);
		if(respuesta.equals("ok")) {
			service.agregar(r);
			return "Reporte Registrado Correctamente";
		}else {
			return respuesta;
		}
	}
	
	//---------------Modulo de Calculo de horas de trabajo--------------
	@GetMapping("/recibir/{idtecnico}/{numsemana}")
	public List<String> listarId(@PathVariable("idtecnico") String idtecnico,@PathVariable("numsemana") String numsemana) throws ParseException{
		ArrayList <Reporte> Listatecnico=new ArrayList<Reporte>(); //Arraylist de tipo Reporte
		ArrayList <String> horas=new ArrayList<String>();
		horas.add("Total Horas: 0");
		if(!service.buscarTecnico(idtecnico).isEmpty()){  //Invoca el metodo que busca el id del tecnico
			Listatecnico=(ArrayList<Reporte>) service.buscarTecnico(idtecnico); //Llena el arraylist con la info del tecnico
			Reglas_de_Negocio RN=new Reglas_de_Negocio();
			horas=(ArrayList<String>) RN.HorasSemanales(Listatecnico,numsemana);
		}
		return horas;
	}
}