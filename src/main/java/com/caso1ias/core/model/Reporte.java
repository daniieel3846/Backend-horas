package com.caso1ias.core.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Entity
@Table(name="reporte")
public class Reporte {
	//Attributes
	@Id
	@Column
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	@NotEmpty(message="El campo ID Técnico esta vacio")
	@NotNull(message="El ID del Técnico es obligatorio*")
	@Column
	private String idtecnico;
	@NotEmpty(message="El campo ID Servicio esta vacio")
	@NotNull(message="El ID del Servicio es obligatorio*")
	@Column
	private String idservicio;
	@NotEmpty(message="El campo ID fecha inicio esta vacio")
	@NotNull(message="La fecha de inicio es obligatoria*")
	@Column
	private String fecha_inicio;
	@NotEmpty(message="El campo hora inicio esta vacio")
	@NotNull(message="La hora de inicio es obligatoria*")
	@Column
	private String hora_inicio;
	@NotEmpty(message="El campo fecha fin esta vacio")
	@NotNull(message="La fecha de fin es obligatoria*")
	@Column
	private String fecha_fin;
	@NotEmpty(message="El campo hora fin esta vacio")
	@NotNull(message="La hora de fin es obligatoria*")
	@Column
	private String hora_fin;
	
	public Reporte() {
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getIdtecnico() {
		return idtecnico;
	}
	public String getIdservicio() {
		return idservicio;
	}
	public String getFecha_inicio() {
		return fecha_inicio;
	}
	public String getHora_inicio() {
		return hora_inicio;
	}
	public String getFecha_fin() {
		return fecha_fin;
	}
	public String getHora_fin() {
		return hora_fin;
	}
}