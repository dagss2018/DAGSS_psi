/*
 Proyecto Java EE, DAGSS-2014
 */
package es.uvigo.esei.dagss.dominio.daos;

import es.uvigo.esei.dagss.dominio.entidades.Cita;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.TypedQuery;

@Stateless
@LocalBean
public class CitaDAO extends GenericoDAO<Cita> {

    public List<Cita> getCitasMedico(long idMedico) {
        TypedQuery<Cita> q = em.createQuery("SELECT c FROM Cita AS c WHERE c.medico.id = :idMedico", Cita.class);
        q.setParameter("idMedico", idMedico);

        return q.getResultList();
    }

    public List<Cita> getCitasMedico(long idMedico, Date dia) {
        TypedQuery<Cita> q = em.createQuery("SELECT c FROM Cita AS c WHERE c.medico.id = :idMedico AND c.fecha = :fecha", Cita.class);
        q.setParameter("idMedico", idMedico);
        q.setParameter("fecha", dia);

        return q.getResultList();

    }
}
