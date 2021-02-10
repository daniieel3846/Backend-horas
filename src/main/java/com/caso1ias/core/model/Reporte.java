package com.caso1ias.core.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

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
	public String Validaciones(String fechaIni,String fechaFi,String horaIni,String horaFi) throws ParseException {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		java.util.Date FechaI = formatter.parse(fechaIni);
		java.util.Date FechaII = formatter.parse(fechaFi);
		Calendar objCalendario = Calendar.getInstance(); 
		objCalendario.setTime(FechaI); //enviar la fecha recibida
		int diaIni=objCalendario.get(Calendar.DATE);
		objCalendario.setTime(FechaII);
		int diaFin=objCalendario.get(Calendar.DATE);
		if(FechaI.after(FechaII)) { //si fechainicio es mayor posterior a fechafin
			return "La fecha de inicio debe ser menor que la fecha de fin";
		}
		if(FechaI.equals(FechaII)) {
			int horaI=CalcularMinutos(horaIni);
			int horaF=CalcularMinutos(horaFi);
			if(horaI>horaF) {
				return "Ingresa una hora de fin valida";
			}
		}
		return "ok";
		
		//int mes = objCalendario.get(Calendar.MONTH)+1;
		//int dia = objCalendario.get(Calendar.DATE);
       // int annio = objCalendario.get(Calendar.YEAR);
		
		
		
	}
	public int Horas_a_Minutos(int Horas) {
		return Horas*60; //1 Hora = 60 Minutos
	}
	public int Minutos_a_Horas(int Minutos) {
		return Minutos/60; 
	}
	//-------------------------Metodos Reglas de Negocio----------------
	public int CalcularMinutos(String texto) {
		String []linea=texto.split(":");
		int Hora=Horas_a_Minutos(Integer.parseInt(linea[0])); //convierte hora a minutos
		int Minutos=Integer.parseInt(linea[1]);
		int totalMinutos=(Hora+Minutos); 
		return totalMinutos; 
	}
	public boolean CompararFechas(String fecha1,String fecha2,String numSemanaAnnio) throws ParseException {
		int numSemanaFecha1= Week_Year(fecha1); //fecha inicio
		int numSemanaFecha2= Week_Year(fecha2); //fecha Fin
		if(numSemanaFecha1==numSemanaFecha2 && numSemanaFecha1==Integer.parseInt(numSemanaAnnio)) {
			return true;
		}
		return false;
	}
	//--------Se obtiene semana del año------------
	public int Week_Year(String date) throws ParseException {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd"); //formato de fecha
		Calendar objCalendario = Calendar.getInstance();
		java.util.Date Fecha = formatter.parse(date); //obtiene la fecha de inicio y la  transforma a formato date
		objCalendario.setTime(Fecha);
		if(DiaDominical(date)==8)return objCalendario.get(Calendar.WEEK_OF_YEAR)-2;
		return objCalendario.get(Calendar.WEEK_OF_YEAR)-1;
	}
	//--------Se obtiene el dia Domingo------------
	public int DiaDominical(String date) throws ParseException {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd"); 
		Calendar objCalendario = Calendar.getInstance();
		java.util.Date Fecha = formatter.parse(date); 
		objCalendario.setTime(Fecha);
		int diaSemana = objCalendario.get(Calendar.DAY_OF_WEEK);
	    switch (diaSemana) {
	    	case Calendar.SUNDAY:
	    		return 8;
	    	default:
	    		return 0;
	    }
	}
	//---Metodo que calcula las horas cando las fechas son iguales-------------
	public int[] HorasSemanal_Fechas_iguales(int total,int hourinicio, int hourfin,int diaD) {
		double HoraInicial=Minutos_a_Horas(hourinicio);
		double HoraFinal=Minutos_a_Horas(hourfin);
		int noche=0,normal=0,nocheExtra=0,normalExtra=0,resultado=0;
		boolean sw=false; 
		int []array=new int[6];
		while(HoraInicial<HoraFinal) {
			if(HoraInicial>=20) {//horario de 8 a 12 de la noche
				if(total+resultado>48) {
					nocheExtra+=1;
				}else {
					noche+=1;
				}
			}
			if(HoraInicial>=7 && HoraInicial<20) { //horario de 7 am a 8 pm
				if(total+resultado>48) {  //entra a la condicion solo cuando se ingresa otro nuevo
					normalExtra+=1;
				}else {
					normal+=1;
				}
			}
			if(HoraInicial>=0 && HoraInicial<7) { //horario de 12 am a 7 am 
				if(total+resultado>48) {
					nocheExtra+=1;
				}else {
					noche+=1;
				}
			}
			HoraInicial++;
		}
		resultado=noche+normal;
		if(total+resultado>48 && sw==false) { //cuando llega a los 48 por primera vez
			sw=true; //true para que no vuelva a entrar a la condicion
			normal=0;
			noche=0;
			normalExtra=0;
			nocheExtra=0;
			HoraInicial=Minutos_a_Horas(hourinicio);
			HoraFinal=Minutos_a_Horas(hourfin);
			while(HoraInicial<HoraFinal) {
				if(HoraInicial>=20) {//horario de 8 a 12 de la noche
					if(total<48) {
						noche+=1;
					}else {
						nocheExtra+=1;
					}
				}
				if(HoraInicial>=7 && HoraInicial<20) { //horario de 7 am a 8 pm
					if(total<48) {
						normal+=1;
					}else {
						normalExtra+=1;
					}
				}
				if(HoraInicial>=0 && HoraInicial<7) { //horario de 12 am a 7 am 
					if(total<48) {
						noche+=1;
					}else {
						nocheExtra+=1;
					}
				}
				HoraInicial++;
				total++;
			}
		}
		array[0]=noche;
		array[1]=nocheExtra;
		array[2]=normal;
		array[3]=normalExtra;
		array[4]=0;
		array[5]=0;
		if(diaD==8) {
			array[0]=0;
			array[1]=0;
			array[2]=0;
			array[3]=0;
			array[4]=noche+normal;
			array[5]=nocheExtra+normalExtra;
		}
		return array;
	}
	//Calcula las horas en fechas diferentes
	public int[] CalcularHoras24HorasSeguidas(int HoraInicio,int HoraFin,int total,int diaD,int diaD2) {
		double HoraInicial=Minutos_a_Horas(HoraInicio);
		double HoraFinal=Minutos_a_Horas(HoraFin);
		boolean sw=false;
		int noche=0,normal=0,nocheExtra=0,normalExtra=0,resultado=0;
		int domingo=0,domingoExtra=0;
		int []array=new int[6];
		boolean bandera1=false;
		if(diaD2==8) { //si el dia de fin es domingo
			bandera1=true;
		}
		
		while(HoraInicial<20) {
			if(total+resultado>48) {
				normalExtra+=1;
			}else {
				normal+=1;
			}
			if(HoraInicial>=0 && HoraInicial<7){
				if(total+resultado>48) {
					normalExtra-=1;
					nocheExtra+=1;
				}else {
					noche+=1;
					normal-=1;
				}
				//Horas de la madrugada 
				/*if(bandera1) {
					if(total+resultado>48) {
						nocheExtra-=1;
						domingoExtra+=1;
					}else {
						noche-=1;
						domingo+=1;
					}
					
				}*/
			}
			HoraInicial++;
		}
		while(HoraInicial>=20 && HoraInicial<24) {
			if(total+resultado>48) {
				nocheExtra+=1;
			}else {
				noche+=1;
			}
			HoraInicial++;
		}
		
		//hora del del dia siguiente
		while(0<HoraFinal) {
			if(HoraFinal>20) {
				if(total+resultado>48) {
					nocheExtra+=1;
				}else {
					noche+=1;
				}
				if(bandera1) {
					if(total+resultado>48) {
						nocheExtra-=1;
						domingoExtra+=1;
					}else {
						domingo+=1;
						noche-=1;
					}
				}
			}else if(HoraFinal>7 && HoraFinal<20) {
				if(total+resultado>48) {
					normalExtra+=1;
				}else {
					normal+=1;
				}
				
				if(bandera1) {
					if(total+resultado>48) {
						normalExtra-=1;
						domingoExtra+=1;
					}else {
						domingo+=1;
						normal-=1;
					}
				}
				
			}else { //Horas de la madrugada de 0 a 7 am
				if(total+resultado>48) {
					nocheExtra+=1;
				}else {
					noche+=1;
				}
				
				if(bandera1) {
					if(total+resultado>48) {
						nocheExtra-=1;
						domingoExtra+=1;
					}else {
						domingo+=1;
						noche-=1;
					}
				}
			}
			HoraFinal--;
		}
		resultado=noche+normal;
		
		if(total+resultado+domingo>48) {
			int horarioNormal=total+resultado;
			int []vectorHoras=RepartirHorasDomingo(horarioNormal,domingo);
			domingo=vectorHoras[0];
			domingoExtra=vectorHoras[1];
		}
		
		
		if(total+resultado>48 && sw==false) { //cuando llega a los 48 por primera vez
			sw=true; //true para que no vuelva a entrar a la condicion
			normal=0;
			noche=0;
			normalExtra=0;
			nocheExtra=0;
			HoraInicial=Minutos_a_Horas(HoraInicio);
			HoraFinal=Minutos_a_Horas(HoraFin);
			while(HoraInicial<20) {
				if(total<=48) {
					normal+=1;
				}else {
					normalExtra+=1;
				}
				if(HoraInicial>=0 && HoraInicial<7){
					if(total<=48) {
						normal-=1;
						noche+=1;
					}else {
						normalExtra-=1;
						nocheExtra+=1;
					}
				}
				HoraInicial++;
				total++;
			}
			
			while(HoraInicial>=20 && HoraInicial<24) {
				if(total<=48) {
					noche+=1;
				}else {
					nocheExtra+=1;
				}
				HoraInicial++;
				total++;
			}
			total++;
			
			while(0<HoraFinal) {
				if(HoraFinal>20) {
					if(total<=48) {
						noche+=1;
					}else {
						nocheExtra+=1;
					}
					//------------------ ver
					if(bandera1) {
						if(total<=48) {
							noche-=1;
						}else {
							nocheExtra-=1;
						}
					}
					
				}else if(HoraFinal>7 && HoraFinal<20) {
					if(total<=48) {
						normal+=1;
					}else {
						normalExtra+=1;
					}
					//------------------ ver
					if(bandera1) {
						if(total<=48) {
							normal-=1;
						}else {
							normalExtra-=1;
						}
					}
					
					
				}else {
					if(total<=48) {
						noche+=1;
					}else {
						nocheExtra+=1;
					}
					//----------Funcional-------
					if(bandera1) {
						if(total<=48) {
							noche-=1;
							
						}else {
							nocheExtra-=1;
						}
					}
					
				}
				HoraFinal--;
				total++;
			}
			
		}
		array[0]=noche;
		array[1]=nocheExtra;
		array[2]=normal;
		array[3]=normalExtra;
		array[4]=0;
		array[5]=0;
		if(diaD2==8) {
			array[0]=noche;
			array[1]=nocheExtra; 
			array[2]=normal;
			array[3]=normalExtra;
			array[4]=domingo;
			array[5]=domingoExtra;
			
		}
		return array;
	}
	
	public int[] Verificar_TotalHoras(int total,int hour1, int hour2) {
		int horasHechas=0;
		int []array=new int[2];
		int horaNormal=0;
		if(total+horasHechas>48) {
		for(;total<48;total++) {
			horaNormal++;
			horasHechas--; 
		}
		array[0]=horaNormal; //horas menores a 48
		array[1]=horasHechas; //horas mayores a 48
		}else {
			array[0]=horasHechas; //horas normales
			array[1]=0;
		}
		return array;
	}
	//Metodo usado para verificar las horas dominicales
	public int[] RepartirHorasDomingo(int total,int horasDomingo) {
		int []vectorHoras=new int[2];
		int horaMenor48=0;
		int horaMayor48=horasDomingo;
		for(;total<48;total++) {
			horaMenor48++;
			horaMayor48--; 
		}
		vectorHoras[0]=horaMenor48; //horas menores a 48
		vectorHoras[1]=horaMayor48; //horas mayores a 48
		return vectorHoras;
	}
	
	public List<String> HorasSemanales(ArrayList<Reporte> Listatecnico,String numsemana) throws ParseException{
		int HorasNormales=0, HorasNocturnas=0,HorasDominicales=0,HorasNormalExtra=0, HorasNocturnasExtra=0,HorasDominicalesExtra=0,total=0;
		ArrayList <String> horas=new ArrayList<String>();
		for(Reporte tecnico:Listatecnico) {
			int horainicio=CalcularMinutos(tecnico.getHora_inicio());//--------Horas convertidas a minutos-----------
			int horafin=CalcularMinutos(tecnico.getHora_fin());
			int diaDomingo=DiaDominical(tecnico.getFecha_inicio());
			int diaDomingo2=DiaDominical(tecnico.getFecha_fin()); //si la fecha de fin es domingo
			if(CompararFechas(tecnico.getFecha_inicio(), tecnico.getFecha_fin(),numsemana)) {
				int []array = null;
				if(tecnico.getFecha_inicio().equalsIgnoreCase(tecnico.getFecha_fin())) {
					if(horainicio<horafin) {
						array=HorasSemanal_Fechas_iguales(total,horainicio,horafin,diaDomingo); //El array tiene los calculos hechos
					}
					HorasNocturnas+=array[0];
					HorasNocturnasExtra+=array[1];
					HorasNormales+=array[2];
					HorasNormalExtra+=array[3];
					HorasDominicales+=array[4];
					HorasDominicalesExtra+=array[5];
				}else { //Si las fechas son diferentes
					array=CalcularHoras24HorasSeguidas(horainicio,horafin,total,diaDomingo,diaDomingo2); //Calcula las horas de el tecnico en las 24 horas;
					HorasNocturnas+=array[0];
					HorasNocturnasExtra+=array[1];
					HorasNormales+=array[2];
					HorasNormalExtra+=array[3];	
					HorasDominicales+=array[4];
					HorasDominicalesExtra+=array[5];
				}
			} //cierre for
			total=HorasNormales+HorasNocturnas+HorasDominicales+HorasNormalExtra+HorasNocturnasExtra+HorasDominicalesExtra;
		}
		horas.add("Horas Normales: "+String.valueOf(HorasNormales));
		horas.add("Horas Nocturnas: "+String.valueOf(HorasNocturnas));
		horas.add("Horas Dominicales: "+String.valueOf(HorasDominicales));
		horas.add("Horas Normales Extra: "+String.valueOf(HorasNormalExtra));
		horas.add("Horas Nocturnas Extra: "+String.valueOf(HorasNocturnasExtra));
		horas.add("Horas Dominicales Extra: "+String.valueOf(HorasDominicalesExtra));
		horas.add("Total Horas: "+String.valueOf(total));
		return horas;
		//return String.valueOf(total);
	}
				/*else {
					
					
					//Horas nocturnas de madrugada
					if(horainicio>=1200 && horainicio<1440) {//hora de inicio entre las 8 pm y 12 am
						if(horafin>=0 && horafin<420) { //12 a 7 am -- madrugada
							array=Verificar_TotalHoras(total,horainicio,horafin,2);
							HorasNocturnas+=array[0];
							HorasNocturnasExtra+=array[1];
						}else if(horafin>420 && horafin<1200) {  //horas normales
							array=CalcularHorasV3(horainicio,horafin,total);
							HorasNocturnas+=array[0];
							HorasNocturnasExtra+=array[1];
							HorasNormales+=array[2];
							HorasNormalExtra+=array[3];
						
						}else {
							array=CalcularHorasV3(horainicio,horafin,total);
							HorasNocturnas+=array[0];
							HorasNocturnasExtra+=array[1];
							HorasNormales+=array[2];
							HorasNormalExtra+=array[3];
					
						}
					}else if(horainicio>=420 && horainicio<1200) { //hora de inicio entre las 7 am y 6 pm
						if(horafin>=0 && horafin<420) { //8 am  a 7 am -- madrugada
							array=CalcularHorasV25(horainicio,horafin,total);
							HorasNocturnas+=array[0];
							HorasNocturnasExtra+=array[1];
							HorasNormales+=array[2];
							HorasNormalExtra+=array[3];
						}else {  //horas normales
							array=CalcularHorasV25(horainicio,horafin,total);
							HorasNocturnas+=array[0];
							HorasNocturnasExtra+=array[1];
							HorasNormales+=array[2];
							HorasNormalExtra+=array[3];
							
								
							}
					}
				}*/
			
			
		
		
	
	
	//------------------Metodos prueba -- en desarrollo-----------------
	//---------Calcula el total de horas conviertiendo horas a minutos --
		/*--------------
		public List<String> HorasSemanal(ArrayList<Reporte> Listatecnico,String numsemana) throws ParseException{
			int HorasNormales=0, HorasNocturnas=0,HorasDominicales=0,HorasNormalExtra=0, HorasNocturnasExtra=0,HorasDominicalesExtra=0,total=0;
			ArrayList <String> horas=new ArrayList<String>();
			for(Reporte tecnico:Listatecnico) {
				int horainicio=CalcularMinutos(tecnico.getHora_inicio());//--------Horas convertidas a minutos-----------
				int horafin=CalcularMinutos(tecnico.getHora_fin());
				if(CompararFechas(tecnico.getFecha_inicio(), tecnico.getFecha_fin(),numsemana)) {
					if(DiaDominical(tecnico.getFecha_inicio())==8) {
						HorasDominicales+=CalcularHoras(horainicio,horafin);
						if(Verificar_TotalHoras(total)) HorasDominicalesExtra+=CalcularHoras(horainicio,horafin);
					}else if(horainicio>=420 && horainicio<1200 && horafin<=1200) {
						if(Verificar_TotalHoras(total)) {
							HorasNormalExtra+=CalcularHoras(horainicio,horafin);
						}else {
							HorasNormales+=CalcularHoras(horainicio,horafin);
						}
					}else if(horainicio>=1200) {
						if(Verificar_TotalHoras(total)) {
							HorasNocturnasExtra+=CalcularHoras(horainicio,horafin);
						}else {
							HorasNocturnas+=CalcularHoras(horainicio,horafin);
						}
					}
				}
				total=HorasNormales+HorasNocturnas+HorasDominicales+HorasNormalExtra+HorasNocturnasExtra+HorasDominicalesExtra;
			}
			
			horas.add("Horas Normales: "+String.valueOf(HorasNormales));
			horas.add("Horas Nocturnas: "+String.valueOf(HorasNocturnas));
			horas.add("Horas Dominicales: "+String.valueOf(HorasDominicales));
			horas.add("Horas Normales Extra: "+String.valueOf(HorasNormalExtra));
			horas.add("Horas Nocturnas Extra: "+String.valueOf(HorasNocturnasExtra));
			horas.add("Horas Dominicales Extra: "+String.valueOf(HorasDominicalesExtra));
			horas.add("Total Horas: "+String.valueOf(total));
			return horas;
			//return String.valueOf(total);
		}*/
	
	
	
	
}
