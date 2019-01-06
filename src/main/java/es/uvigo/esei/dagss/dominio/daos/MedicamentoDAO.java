/*
 Proyecto Java EE, DAGSS-2014
 */
package es.uvigo.esei.dagss.dominio.daos;

import es.uvigo.esei.dagss.dominio.entidades.Medicamento;
import java.util.List;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.TypedQuery;

@Stateless
@LocalBean
public class MedicamentoDAO extends GenericoDAO<Medicamento> {

    public List<Medicamento> bucarMedicamentos(String textoBusqueda) {
        TypedQuery<Medicamento> q = em.createQuery("SELECT c FROM Medicamento AS c "
                + "WHERE c.nombre LIKE :txt OR "
                + "c.fabricante LIKE :txt OR "
                + "c.familia LIKE :txt OR "
                + "c.principioActivo LIKE :txt", Medicamento.class);
        q.setParameter("txt", "%" + textoBusqueda + "%");
        return q.getResultList();
    }
}
