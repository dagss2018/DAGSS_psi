/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.uvigo.esei.dagss.dominio.prescripcionfacade;

import es.uvigo.esei.dagss.dominio.daos.GenericoDAO;
import es.uvigo.esei.dagss.dominio.daos.PrescripcionDAO;
import es.uvigo.esei.dagss.dominio.daos.RecetaDAO;
import es.uvigo.esei.dagss.dominio.entidades.Prescripcion;
import es.uvigo.esei.dagss.dominio.entidades.Receta;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

/**
 *
 * @author santi
 */
@Stateless
@LocalBean
public class PrescripcionFacade {

    private PlanificadorRecetas planificadorRecetas;
    @EJB
    private PrescripcionDAO prescripcionDAO;
    @EJB
    private RecetaDAO recetaDAO;

    public PrescripcionFacade() {
        this.planificadorRecetas = new PlanificadorFran();
    }

    public void planificarRecetas(Prescripcion prescripcion) {
        List<Receta> recetas = this.planificadorRecetas.crearRecetas(prescripcion);

        prescripcionDAO.crear(prescripcion);
        recetas.forEach((receta) -> {
            recetaDAO.crear(receta);
        });
    }

}
