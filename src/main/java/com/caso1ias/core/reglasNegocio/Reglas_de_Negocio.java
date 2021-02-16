package com.caso1ias.core.reglasNegocio;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.caso1ias.core.model.Reporte;

public class Reglas_de_Negocio {
	
	public Reglas_de_Negocio() {
		
	}
	public int Horas_a_Minutos(int Horas) {
		return Horas*60; //1 Hora = 60 Minutos
	}
	public int Minutos_a_Horas(int Minutos) {
		return Minutos/60; 
	}
	public Date DevolverFecha_tipo_Date(String fecha) throws ParseException {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		java.util.Date fechaI = formatter.parse(fecha);
		return fechaI;
	}
	public String Validaciones(String fechaIni,String fechaFi,String horaIni,String horaFi) throws ParseException {
		Date fechaI=DevolverFecha_tipo_Date(fechaIni);
		Date fechaII=DevolverFecha_tipo_Date(fechaFi);
		Calendar objCalendario = Calendar.getInstance(); 
		objCalendario.setTime(fechaI); //enviar la fecha recibida
		int diaIni=objCalendario.get(Calendar.DATE);
		objCalendario.setTime(fechaII);
		int diaFin=objCalendario.get(Calendar.DATE);
		if(fechaI.after(fechaII)) { //si fechainicio es mayor posterior a fechafin
			return "La fecha de inicio debe ser menor que la fecha de fin";
		}
		if(fechaI.equals(fechaII)) {
			int horaI=CalcularMinutos(horaIni);
			int horaF=CalcularMinutos(horaFi);
			if(horaI>horaF) {
				return "Ingresa una hora de fin valida";
			}
		}
		if(diaFin-diaIni>=2)return "Las fechas no deben superar mas de un dia"; 
		return "ok";
	}
	//--------Se obtiene semana del año------------
	public int Week_Year(String date) throws ParseException {
		Date fecha=DevolverFecha_tipo_Date(date);
		Calendar objCalendario = Calendar.getInstance();
		objCalendario.setTime(fecha);
		if(DiaDominical(date)==8)return objCalendario.get(Calendar.WEEK_OF_YEAR)-2;
		return objCalendario.get(Calendar.WEEK_OF_YEAR)-1;
		}
	//--------Se obtiene el dia Domingo------------
	public int DiaDominical(String date) throws ParseException {
		Date fecha=DevolverFecha_tipo_Date(date);
		Calendar objCalendario = Calendar.getInstance();
		objCalendario.setTime(fecha);
		int diaSemana = objCalendario.get(Calendar.DAY_OF_WEEK);
		switch (diaSemana) {
			case Calendar.SUNDAY:
				return 8;
		    default:
		    	return 0;
		}
	}
	//------------------Reglas de Negocio-----------------------------
	public List<String> HorasSemanales(ArrayList<Reporte> Listatecnico,String numsemana) throws ParseException{
		int HorasNormales=0, HorasNocturnas=0,HorasDominicales=0,HorasNormalExtra=0, HorasNocturnasExtra=0,HorasDominicalesExtra=0,total=0;
		ArrayList <String> horas=new ArrayList<String>();
		for(Reporte tecnico:Listatecnico) {
			int horainicio=CalcularMinutos(tecnico.getHora_inicio());//--------Horas convertidas a minutos-----------
			int horafin=CalcularMinutos(tecnico.getHora_fin());
			int diaDomingoInicio=DiaDominical(tecnico.getFecha_inicio());
			int diaDomingoFin=DiaDominical(tecnico.getFecha_fin()); //si la fecha de fin es domingo
			int []horasTecnico = null;
			if(CompararFechas_SemanaIgual(tecnico.getFecha_inicio(), tecnico.getFecha_fin(),numsemana)) {
				if(tecnico.getFecha_inicio().equalsIgnoreCase(tecnico.getFecha_fin())) {
					if(horainicio<horafin) {
						horasTecnico=HorasSemanal_Fechas_iguales(total,horainicio,horafin,diaDomingoInicio); //El array tiene los calculos hechos
					}
				}else { //Si las fechas son diferentes
					horasTecnico=HorasSemanal_Fechas_Diferentes(horainicio,horafin,total,diaDomingoFin); //Calcula las horas de el tecnico en las 24 horas;
				}
				HorasNocturnas+=horasTecnico[0];
				HorasNocturnasExtra+=horasTecnico[1];
				HorasNormales+=horasTecnico[2];
				HorasNormalExtra+=horasTecnico[3];
				HorasDominicales+=horasTecnico[4];
				HorasDominicalesExtra+=horasTecnico[5];
			}else if(CompararFechas_SemanaDiferente(tecnico.getFecha_inicio(), tecnico.getFecha_fin(),numsemana)) {
				horasTecnico=CalcularHoras_Domingo_especial(horainicio,total); //Calcula las horas del domingo en caso de que la hora fin sea la siguiente semana
				HorasDominicales+=horasTecnico[0];
				HorasDominicalesExtra+=horasTecnico[1];
			}
			total=HorasNormales+HorasNocturnas+HorasDominicales+HorasNormalExtra+HorasNocturnasExtra+HorasDominicalesExtra;
		}//cierre for
		horas.add("Horas Normales: "+String.valueOf(HorasNormales));
		horas.add("Horas Nocturnas: "+String.valueOf(HorasNocturnas));
		horas.add("Horas Dominicales: "+String.valueOf(HorasDominicales));
		horas.add("Horas Normales Extra: "+String.valueOf(HorasNormalExtra));
		horas.add("Horas Nocturnas Extra: "+String.valueOf(HorasNocturnasExtra));
		horas.add("Horas Dominicales Extra: "+String.valueOf(HorasDominicalesExtra));
		horas.add("Total Horas: "+String.valueOf(total));
		return horas;
	}
	//recibe las horas y las calcula en minutos
	public int CalcularMinutos(String texto) {
		String []linea=texto.split(":");
		int Hora=Horas_a_Minutos(Integer.parseInt(linea[0])); //convierte hora a minutos
		int Minutos=Integer.parseInt(linea[1]);
		int totalMinutos=(Hora+Minutos); 
		return totalMinutos; 
	}
	public boolean CompararFechas_SemanaIgual(String fecha1,String fecha2,String numSemanaAnnio) throws ParseException {
		int numSemanaFecha1= Week_Year(fecha1); //fecha inicio
		int numSemanaFecha2= Week_Year(fecha2); //fecha Fin
		if(numSemanaFecha1==numSemanaFecha2 && numSemanaFecha1==Integer.parseInt(numSemanaAnnio)) {
			return true;
		}
		return false;
	}
	public boolean CompararFechas_SemanaDiferente(String fecha1,String fecha2,String numSemanaAnnio) throws ParseException {
		int numSemanaFecha1= Week_Year(fecha1); //fecha inicio
		int numSemanaFecha2= Week_Year(fecha2); //fecha Fin
		if(numSemanaFecha2-numSemanaFecha1==1 && numSemanaFecha1==Integer.parseInt(numSemanaAnnio) && DiaDominical(fecha1)==8) { //la fecha2 corresponde a la semana sgte
			return true; //fecha inicio es domingo
		}
		return false;
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
	//Calcula las horas solo del domingo en caso de que el tecnico inicio un domingo y termino al otro dia (el lunes de la siguiente semana).
	public int[] CalcularHoras_Domingo_especial(int HoraInicio,int total) {
		double HoraInicial=Minutos_a_Horas(HoraInicio);
		int domingo=0,domingoExtra=0,resultado=0;
		int []array=new int[2];
		boolean sw=false;
		if(total>48)sw=true; 
		while(HoraInicial<24) {
			if(total+resultado>48) {
				domingoExtra+=1;
			}else {
				domingo+=1;
			}
			HoraInicial++;
		}
		resultado=domingo+domingoExtra;
		if(total+resultado>48 && sw==false) {
			int []vectorHoras=RepartirHorasDomingo(total,resultado);
			domingo=vectorHoras[0];
			domingoExtra=vectorHoras[1];
		}	
		array[0]=domingo;
		array[1]=domingoExtra;
		return array;
		}	
	//Cuando llega a 48 horas entra a este metodo
	public int[] Fechas_Iguales_FirstHours48(int total,double HoraInicial,double HoraFinal) {
		int []array=new int[4];
		array[0]=0;//noche; 
		array[1]=0;//nocheExtra;
		array[2]=0;//normal;
		array[3]=0;//normalExtra;
		while(HoraInicial<HoraFinal) {
			if(HoraInicial>=20) {//horario de 8 a 12 de la noche
				if(total<48) {
					array[0]+=1;
				}else {
					array[1]+=1;
				}
			}
			if(HoraInicial>=7 && HoraInicial<20) { //horario de 7 am a 8 pm
				if(total<48) {
					array[2]+=1;
				}else {
					array[3]+=1;
				}
			}
			if(HoraInicial>=0 && HoraInicial<7) { //horario de 12 am a 7 am 
				if(total<48) {
					array[0]+=1;
				}else {
					array[1]+=1;
				}
			}
			HoraInicial++;
			total++;
		}
		return array;
	}
	public int[] Fechas_Diferentes_FirstHours48(int total,double HoraInicial,double HoraFinal,boolean bandera1) {
		int []array=new int[4];
		array[0]=0;//noche; 
		array[1]=0;//nocheExtra;
		array[2]=0;//normal;
		array[3]=0;//normalExtra;
		boolean bandera2=true;
		if(HoraInicial>=7 && HoraInicial<20) {
			total++; bandera2=false; //horas normales
		}
		while(HoraInicial<20) {
			if(total<=48) {
				array[2]+=1;
			}else {
				array[3]+=1;
			}
			if(HoraInicial>=0 && HoraInicial<7){
				if(total<=48) {
					array[2]-=1;
					array[0]+=1;
				}else {
					array[3]-=1;
					array[1]+=1;
				}
			}
			HoraInicial++;
			total++;
		}
		while(HoraInicial>=20 && HoraInicial<24) {
			if(total<=48) {
				array[0]+=1;
			}else {
				array[1]+=1;
			}
			HoraInicial++;
			total++;
		}
		if(total<=48 && bandera2==true) total++; //horas nocturnas
		while(0<HoraFinal) {
			if(HoraFinal>20) {
				if(total<=48) {
					array[0]+=1;
				}else {
					array[1]+=1;
				}
			}else if(HoraFinal>7 && HoraFinal<20) {
				if(total<=48) {
					array[2]+=1;
				}else {
					array[3]+=1;
				}
			}else {
				if(total<=48) {
					array[0]+=1;
				}else {
					array[1]+=1;
				}
				if(bandera1) {
					if(total<=48) {
						array[0]-=1;
						
					}else {
						array[1]-=1;
					}
				}
			}
			HoraFinal--;
			total++;
		}
		return array;
	}
	//---Metodo que calcula las horas cando las fechas son iguales-------------
	public int[] HorasSemanal_Fechas_iguales(int total,int hourinicio, int hourfin,int diaD) {
		double HoraInicial=Minutos_a_Horas(hourinicio);
		double HoraFinal=Minutos_a_Horas(hourfin);
		int noche=0,normal=0,nocheExtra=0,normalExtra=0,resultado=0;
		boolean sw=false; 
		int []array=new int[6];
		if(total>48)sw=true;
		while(HoraInicial<HoraFinal) {
			if(HoraInicial>=20) {//horario de 8 a 12 de la noche
				if(total+resultado>48) {
					nocheExtra+=1;
				}else {
					noche+=1;
				}
			}
			if(HoraInicial>=7 && HoraInicial<20) { //horario de 7 am a 8 pm
				if(total+resultado>48) {  //entra a la condicion solo en el siguiente registro
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
		if(total+resultado>48 && sw==false) { //cuando llega a los 48 por primera vez en un mismo registro
			HoraInicial=Minutos_a_Horas(hourinicio);
			HoraFinal=Minutos_a_Horas(hourfin);
			int []nuevoArray=Fechas_Iguales_FirstHours48(total,HoraInicial,HoraFinal);
			noche=nuevoArray[0];//noche; 
			nocheExtra=nuevoArray[1];//nocheExtra;
			normal=nuevoArray[2];//normal;
			normalExtra=nuevoArray[3];//normalExtra;
		}
		array[0]=noche;
		array[1]=nocheExtra;
		array[2]=normal;
		array[3]=normalExtra;
		array[4]=0;
		array[5]=0;
		if(diaD==8) {
			array[4]=array[0]+array[2]; //noche + normal
			array[5]=array[1]+array[3]; //nocheExtra + normalExtra;
			array[0]=0;
			array[1]=0;
			array[2]=0;
			array[3]=0;
		}
		return array;
	}
	//Calcula las horas en fechas diferentes
	public int[] HorasSemanal_Fechas_Diferentes(int HoraInicio,int HoraFin,int total,int diaD2) {
		double HoraInicial=Minutos_a_Horas(HoraInicio);
		double HoraFinal=Minutos_a_Horas(HoraFin);
		boolean sw=false,sw2=false;
		int noche=0,normal=0,nocheExtra=0,normalExtra=0,resultado=0;
		int domingo=0,domingoExtra=0;
		int []array=new int[6];
		boolean bandera1=false;
		if(diaD2==8) { //si el dia de fin es domingo --
			bandera1=true;
			if(total>48)sw2=true; // si el total ya paso por 48 
		}
		if(total>48)sw=true;
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
		//hora del del dia siguiente ejemplo(domingo)
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
		if(total+resultado+domingo>48 && sw2==false) {//si hace horas dominicales y suman mas de 48 con el result
			total++;//evita que las horas normales se pasen de 48
			int horarioNormal=total+resultado;
			int []vectorHoras=RepartirHorasDomingo(horarioNormal,domingo);
			domingo=vectorHoras[0];
			domingoExtra=vectorHoras[1];
		}
		if(total+resultado>48 && sw==false) { //cuando llega a los 48 por primera vez
			HoraInicial=Minutos_a_Horas(HoraInicio);
			HoraFinal=Minutos_a_Horas(HoraFin);
			int []nuevoArray=Fechas_Diferentes_FirstHours48(total,HoraInicial,HoraFinal,bandera1);
			noche=nuevoArray[0];//noche; 
			nocheExtra=nuevoArray[1];//nocheExtra;
			normal=nuevoArray[2];//normal;
			normalExtra=nuevoArray[3];//normalExtra;
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
}