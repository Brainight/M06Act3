package m06_a3.mk.backend.model;

import java.util.Objects;
import m06_a3.mk.customdb4o.Db4oEntity;

public class Incidencia extends Db4oEntity  {

    private static final long serialVersionUID = 1L;

    private Empleado empleadoByOrigen;
    private Empleado empleadoByDestino;
    private String fechahora;
    private String detalle;
    private String tipo;

    public Incidencia() {
    }

    public Incidencia(Empleado empleadoByOrigen, Empleado empleadoByDestino, String fechahora,
            String detalle, String tipo) {

        this.empleadoByOrigen = empleadoByOrigen;
        this.empleadoByDestino = empleadoByDestino;
        this.fechahora = fechahora;
        this.detalle = detalle;
        this.tipo = tipo;
    }

    public Empleado getEmpleadoByOrigen() {
        return this.empleadoByOrigen;
    }

    public void setEmpleadoByOrigen(Empleado empleadoByOrigen) {
        this.empleadoByOrigen = empleadoByOrigen;
    }

    public Empleado getEmpleadoByDestino() {
        return this.empleadoByDestino;
    }

    public void setEmpleadoByDestino(Empleado empleadoByDestino) {
        this.empleadoByDestino = empleadoByDestino;
    }

    public String getFechahora() {
        return this.fechahora;
    }

    public void setFechahora(String fechahora) {
        this.fechahora = fechahora;
    }

    public String getDetalle() {
        return this.detalle;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }

    public String getTipo() {
        return this.tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String toString() {
        return String.format("{INCIDENCIA : {Id : %d, Origen : %s, Destino : %s, Fecha : %s, Detalle : %s, Tipo : %s}}", this.getId(), this.empleadoByOrigen,
                this.empleadoByDestino, this.fechahora, this.detalle, this.tipo);
    }

    public String simpleToString() {
        return String.format("{INCIDENCIA : {Id : %d, Origen : %s, Destino : %s, Fecha : %s, Detalle : %s, Tipo : %s}}", this.getId(),
                this.empleadoByOrigen != null ? this.empleadoByOrigen.getNombreusuario() : null,
                this.empleadoByDestino != null ? this.empleadoByDestino.getNombreusuario() : null, this.fechahora, this.detalle, this.tipo);
    }

    @Override
    public int hashCode() {
        int hash = 7;
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
        final Incidencia other = (Incidencia) obj;
        if (!Objects.equals(this.fechahora, other.fechahora)) {
            return false;
        }
        if (!Objects.equals(this.detalle, other.detalle)) {
            return false;
        }
        if (!Objects.equals(this.tipo, other.tipo)) {
            return false;
        }
        if (!Objects.equals(this.empleadoByOrigen, other.empleadoByOrigen)) {
            return false;
        }
        return Objects.equals(this.empleadoByDestino, other.empleadoByDestino);
    }
        
        

}
