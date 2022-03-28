package m06_a3.mk.backend.model;

import java.util.Objects;
import m06_a3.mk.customdb4o.Db4oEntity;

public class Historial extends Db4oEntity {

    private static final long serialVersionUID = 1L;

    private Empleado empleado;
    private String tipo;
    private String fechahora;

    public Historial() {
    }

    public Historial(Empleado empleado, String tipo, String fechahora) {
        this.empleado = empleado;
        this.tipo = tipo;
        this.fechahora = fechahora;
    }

    public Empleado getEmpleado() {
        return this.empleado;
    }

    public void setEmpleado(Empleado empleado) {
        this.empleado = empleado;
    }

    public String getTipo() {
        return this.tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getFechahora() {
        return this.fechahora;
    }

    public void setFechahora(String fechahora) {
        this.fechahora = fechahora;
    }

    public String toString() {
        return String.format("{HISTORIAL : {Id : %d, Empleado : %s, Tipo : %s, Fecha : %s}}", this.getId(), this.empleado, this.tipo, this.fechahora);
    }

    @Override
    public int hashCode() {
        int hash = 5;
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
        final Historial other = (Historial) obj;
        if (!Objects.equals(this.tipo, other.tipo)) {
            return false;
        }
        if (!Objects.equals(this.fechahora, other.fechahora)) {
            return false;
        }
        return Objects.equals(this.empleado, other.empleado);
    }
    
    
}
