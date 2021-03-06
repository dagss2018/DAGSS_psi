/*
 Proyecto Java EE, DAGSS-2013
 */
package es.uvigo.esei.dagss.controladores.medico;

import es.uvigo.esei.dagss.dominio.prescripcionfacade.PrescripcionFacade;
import es.uvigo.esei.dagss.controladores.autenticacion.AutenticacionControlador;
import es.uvigo.esei.dagss.dominio.daos.CitaDAO;
import es.uvigo.esei.dagss.dominio.daos.MedicamentoDAO;
import es.uvigo.esei.dagss.dominio.daos.MedicoDAO;
import es.uvigo.esei.dagss.dominio.daos.PrescripcionDAO;
import es.uvigo.esei.dagss.dominio.entidades.Cita;
import es.uvigo.esei.dagss.dominio.entidades.EstadoCita;
import es.uvigo.esei.dagss.dominio.entidades.Medicamento;
import es.uvigo.esei.dagss.dominio.entidades.Medico;
import es.uvigo.esei.dagss.dominio.entidades.Prescripcion;
import es.uvigo.esei.dagss.dominio.entidades.TipoUsuario;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

/**
 *
 * @author ribadas
 */
@Named(value = "medicoControlador")
@SessionScoped
public class MedicoControlador implements Serializable {

    @EJB
    private PrescripcionFacade prescripcionFacade;

    private Medico medicoActual;
    private String dni;
    private String numeroColegiado;
    private String password;
    private String textoBusqueda;

    private Cita citaActual;
    private List<Medicamento> medicamentosEncontrados;
    private Prescripcion nuevaPrescripcion;

    private Date fechaActual = Calendar.getInstance().getTime();

    @Inject
    private AutenticacionControlador autenticacionControlador;

    @EJB
    private MedicoDAO medicoDAO;
    @EJB
    private CitaDAO citaDAO;
    @EJB
    private PrescripcionDAO prescripcionDAO;
    @EJB
    private MedicamentoDAO medicamentoDAO;

    /**
     * Creates a new instance of AdministradorControlador
     */
    public MedicoControlador() {
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getNumeroColegiado() {
        return numeroColegiado;
    }

    public void setNumeroColegiado(String numeroColegiado) {
        this.numeroColegiado = numeroColegiado;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Medico getMedicoActual() {
        return medicoActual;
    }

    public void setMedicoActual(Medico medicoActual) {
        this.medicoActual = medicoActual;
    }

    private boolean parametrosAccesoInvalidos() {
        return (((dni == null) && (numeroColegiado == null)) || (password == null));
    }

    public Date getFechaActual() {
        return fechaActual;
    }

    public Cita getCitaActual() {
        return citaActual;
    }

    public String getTextoBusqueda() {
        return textoBusqueda;
    }

    public void setTextoBusqueda(String textoBusqueda) {
        this.textoBusqueda = textoBusqueda;
    }

    public List<Medicamento> getMedicamentosEncontrados() {
        return medicamentosEncontrados;
    }

    public Prescripcion getNuevaPrescripcion() {
        return nuevaPrescripcion;
    }

    public void setNuevaPrescripcion(Prescripcion nuevaPrescripcion) {
        this.nuevaPrescripcion = nuevaPrescripcion;
    }

    private Medico recuperarDatosMedico() {
        Medico medico = null;
        if (dni != null) {
            medico = medicoDAO.buscarPorDNI(dni);
        }
        if ((medico == null) && (numeroColegiado != null)) {
            medico = medicoDAO.buscarPorNumeroColegiado(numeroColegiado);
        }
        return medico;
    }

    public String doLogin() {
        String destino = null;
        if (parametrosAccesoInvalidos()) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "No se ha indicado un DNI ó un número de colegiado o una contraseña", ""));
        } else {
            Medico medico = recuperarDatosMedico();
            if (medico == null) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "No existe ningún médico con los datos indicados", ""));
            } else {
                if (autenticacionControlador.autenticarUsuario(medico.getId(), password,
                        TipoUsuario.MEDICO.getEtiqueta().toLowerCase())) {
                    medicoActual = medico;
                    destino = "privado/index";
                } else {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Credenciales de acceso incorrectas", ""));
                }
            }
        }
        return destino;
    }

    public List<Cita> recuperarCitasHoy() {
        return citaDAO.getCitasMedico(medicoActual.getId(), Calendar.getInstance().getTime());
    }

    public List<Prescripcion> recuperarPrescripcionesEnVigor() {
        return prescripcionDAO.getPrescioncionesEnVigor(citaActual.getPaciente().getId(), Calendar.getInstance().getTime());
    }

    public String atenderCita(Cita cita) {
        this.citaActual = cita;
        return "atender_cita";
    }

    public boolean citaAtendible(Cita cita) {
        return cita.getEstado() == EstadoCita.PLANIFICADA;
    }

    public void eliminarPrescripcion(Prescripcion prescripcion) {
        prescripcionDAO.eliminar(prescripcion);
    }

    public String finalizarCita() {
        this.citaActual.setEstado(EstadoCita.COMPLETADA);
        this.citaDAO.actualizar(citaActual);
        this.citaActual = null;
        return "ver_agenda";
    }

    public String marcarComoAusente() {
        this.citaActual.setEstado(EstadoCita.AUSENTE);
        this.citaDAO.actualizar(citaActual);
        this.citaActual = null;
        return "ver_agenda";
    }

    public String guardarNuevoPerfil() {
        medicoDAO.actualizar(medicoActual);
        return "mis_datos";
    }

    public String doBuscarMedicamento() {
        System.out.println(textoBusqueda + "\n\n");
        this.medicamentosEncontrados = medicamentoDAO.bucarMedicamentos(textoBusqueda);
        return "nueva_prescripcion";
    }

    public String seleccionarMedicamento(Medicamento medicamento) {
        this.nuevaPrescripcion = new Prescripcion();
        this.nuevaPrescripcion.setMedicamento(medicamento);
        this.nuevaPrescripcion.setMedico(medicoActual);
        this.nuevaPrescripcion.setFechaInicio(fechaActual);
        this.nuevaPrescripcion.setPaciente(citaActual.getPaciente());
        return "completar_prescripcion";
    }

    public String guardarPrescripcion() {
        prescripcionFacade.planificarRecetas(nuevaPrescripcion);
        return "ver_prescripciones";
    }
}
