package m06_a3.mk.backend.model;

import java.util.Objects;
import m06_a3.mk.customdb4o.Db4oEntity;
import m06_a3.mk.customdb4o.Db4oUniqueField;

public class Empleado extends Db4oEntity {

    private static final long serialVersionUID = 1L;

    @Db4oUniqueField
    private String nombreusuario;
    private String password;
    private String nombrecompleto;
    private String telefono;

    /*private Set<Incidencia> incidenciasForOrigen = new HashSet<>(0);
	private Set<Historial> historials = new HashSet<>(0);
	private Set<Incidencia> incidenciasForDestino = new HashSet<>(0);*/

    public Empleado() {
    }

    public Empleado(String nombreusuario, String password, String nombrecompleto, String telefono) {
        this.nombreusuario = nombreusuario;
        this.password = password;
        this.nombrecompleto = nombrecompleto;
        this.telefono = telefono;
    }

    public String getNombreusuario() {
        return this.nombreusuario;
    }

    public void setNombreusuario(String nombreusuario) {
        this.nombreusuario = nombreusuario;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNombrecompleto() {
        return this.nombrecompleto;
    }

    public void setNombrecompleto(String nombrecompleto) {
        this.nombrecompleto = nombrecompleto;
    }

    public String getTelefono() {
        return this.telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    /*public Set<Incidencia>  getIncidenciasForOrigen() {
		return this.incidenciasForOrigen;
	}

	public void setIncidenciasForOrigen(Set<Incidencia>  incidenciasForOrigen) {
		this.incidenciasForOrigen = incidenciasForOrigen;
	}

	public Set<Historial>  getHistorials() {
		return this.historials;
	}

	public void setHistorials(Set<Historial>  historials) {
		this.historials = historials;
	}

	public Set<Incidencia> getIncidenciasForDestino() {
		return this.incidenciasForDestino;
	}

	public void setIncidenciasForDestino(Set<Incidencia>  incidenciasForDestino) {
		this.incidenciasForDestino = incidenciasForDestino;
	}
     */
    public String toString() {
        return String.format("{EMPLEADO : {Id: %d, Usuario : %s, Password : %s, NombreCompleto : %s, Telefono: %s}}", this.getId(), this.nombreusuario, this.password, this.nombrecompleto, this.telefono);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + Objects.hashCode(this.nombreusuario);
        hash = 53 * hash + Objects.hashCode(this.password);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Empleado other = (Empleado) obj;
        if (!Objects.equals(this.nombreusuario, other.nombreusuario)) {
            return false;
        }
        if (!Objects.equals(this.password, other.password)) {
            return false;
        }
        if (!Objects.equals(this.nombrecompleto, other.nombrecompleto)) {
            return false;
        }
        return Objects.equals(this.telefono, other.telefono);
    }
        
        
	

}
