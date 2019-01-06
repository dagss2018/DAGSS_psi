/*
 Proyecto Java EE, DAGSS-2016
 */
package es.uvigo.esei.dagss.dominio.daos;

import es.uvigo.esei.dagss.dominio.entidades.Cita;
import es.uvigo.esei.dagss.dominio.entidades.Paciente;
import es.uvigo.esei.dagss.dominio.entidades.Prescripcion;
import java.util.Date;
import java.util.List;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.TypedQuery;

@Stateless
@LocalBean
public class PrescripcionDAO extends GenericoDAO<Prescripcion> {

    public Prescripcion buscarPorIdConRecetas(Long id) {
        TypedQuery<Prescripcion> q = em.createQuery("SELECT p FROM Prescripcion AS p JOIN FETCH p.recetas"
                + "  WHERE p.id = :id", Prescripcion.class);
        q.setParameter("id", id);

        return q.getSingleResult();
    }

    // Completar aqui  
    public List<Prescripcion> getPrescioncionesEnVigor(long idPaciente, Date dia) {
        TypedQuery<Prescripcion> q = em.createQuery("SELECT c FROM Prescripcion AS c "
                + "WHERE c.paciente.id = :paciente AND c.fechaFin > :fecha", Prescripcion.class);
        q.setParameter("paciente", idPaciente);
        q.setParameter("fecha", dia);

        return q.getResultList();

    }
}
