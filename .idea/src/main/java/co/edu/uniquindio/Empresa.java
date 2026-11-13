package co.edu.uniquindio;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;


public class Empresa {

    private String nombre;
    private List<Aportante> aportantes;
    private List<Entrega> entregas;

    public Empresa(String nombre) {
        this.nombre = nombre;
        this.aportantes = new LinkedList<>();
        this.entregas = new LinkedList<>();
    }

    /** Registra un aportante nuevo. Rechaza datos nulos/vacios o identificaciones repetidas. */
    public boolean registrarAportante(Aportante aportante) {
        if (aportante == null || aportante.identificacion() == null || aportante.identificacion().isBlank()) {
            return false;
        }
        if (buscarAportante(aportante.identificacion()) != null) {
            return false;
        }
        aportantes.add(aportante);
        return true;
    }

    public Aportante buscarAportante(String identificacion) {
        for (Aportante a : aportantes) {
            if (a.identificacion().equals(identificacion)) {
                return a;
            }
        }
        return null;
    }

    public Entrega buscarEntrega(String codigo) {
        for (Entrega e : entregas) {
            if (e.getCodigo().equals(codigo)) {
                return e;
            }
        }
        return null;
    }

    /** Crea una entrega ABIERTA. Rechaza codigo repetido o aportante desconocido. */
    public boolean crearEntrega(String codigo, LocalDate fecha, String identificacion) {
        if (codigo == null || codigo.isBlank() || fecha == null) {
            return false;
        }
        if (buscarEntrega(codigo) != null) {
            return false;
        }
        Aportante aportante = buscarAportante(identificacion);
        if (aportante == null) {
            return false;
        }
        entregas.add(new Entrega(codigo, fecha, aportante));
        return true;
    }

    public boolean agregarLote(String codigoEntrega, Lote lote) {
        Entrega entrega = buscarEntrega(codigoEntrega);
        if (entrega == null) {
            return false;
        }
        return entrega.agregarLote(lote);
    }

    /** Acepta la entrega. Devuelve null si no existe, esta vacia o ya fue aceptada. */
    public Comprobante aceptarEntrega(String codigo) {
        Entrega entrega = buscarEntrega(codigo);
        if (entrega == null) {
            return null;
        }
        return entrega.aceptar();
    }

    /** Devuelve el comprobante de la entrega, o null si no existe o aun no fue aceptada. */
    public Comprobante consultarComprobante(String codigo) {
        Entrega entrega = buscarEntrega(codigo);
        if (entrega == null) {
            return null;
        }
        return entrega.getComprobante();
    }

    /** Devuelve las etiquetas de la entrega; lista vacia si no existe. */
    public List<String> consultarEtiquetas(String codigo) {
        Entrega entrega = buscarEntrega(codigo);
        if (entrega == null) {
            return new LinkedList<>();
        }
        return entrega.obtenerEtiquetas();
    }

    /** Suma los comprobantes de entregas aceptadas del aportante. Si no hay, devuelve 0. */
    public double consultarValorAcumulado(String identificacion) {
        double total = 0;
        for (Entrega e : entregas) {
            Comprobante c = e.getComprobante();
            if (e.getEstado() == EstadoEntrega.ACEPTADA && c != null
                    && c.idAportante().equals(identificacion)) {
                total += c.valorTotal();
            }
        }
        return total;
    }

    public String getNombre() {
        return nombre;
    }
}