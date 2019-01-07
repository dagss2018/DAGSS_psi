/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.uvigo.esei.dagss.dominio.prescripcionfacade;

import es.uvigo.esei.dagss.dominio.entidades.EstadoReceta;
import es.uvigo.esei.dagss.dominio.entidades.Prescripcion;
import es.uvigo.esei.dagss.dominio.entidades.Receta;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 *
 * @author santi
 */
public class PlanificadorFran implements PlanificadorRecetas {

    @Override
    public List<Receta> crearRecetas(Prescripcion prescripcion) {
        List<Receta> toret = new ArrayList<>();

        //Mejor la aproximacion por defecto debido a perdidas o otros errores
        int diasReceta = prescripcion.getMedicamento().getNumeroDosis() / prescripcion.getDosis();
        int dias = (int) ((prescripcion.getFechaFin().getTime() - prescripcion.getFechaInicio().getTime()) / 86400000);

        for (int i = 0; i <= dias; i += diasReceta) {
            Receta receta = new Receta();
            receta.setCantidad(1);
            receta.setEstado(EstadoReceta.GENERADA);
            //Para la fecha de inicio se suma el numero de intervalo y se le da un plazo de los 7 dias anteriores
            Date inicioValidez = sumarDias(prescripcion.getFechaInicio(), i - 7);
            receta.setInicioValidez(inicioValidez);
            Date finValidez = sumarDias(prescripcion.getFechaInicio(), i + 7);
            receta.setFinValidez(finValidez);
            receta.setPrescripcion(prescripcion);
            toret.add(receta);
        }
        return toret;
    }

    public Date sumarDias(Date fecha, int dias) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(fecha); // Configuramos la fecha que se recibe
        calendar.add(Calendar.DAY_OF_YEAR, dias);  // numero de días a añadir, o restar en caso de días<
        return calendar.getTime(); // Devuelve el objeto Date con los nuevos días añadidos	
    }

}
