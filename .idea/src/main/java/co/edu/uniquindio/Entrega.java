package co.edu.uniquindio;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

public class Entrega {

    private String codigo;
    private LocalDate fecha;
    private Aportante aportante;
    private List<Lote> lotes;
    private EstadoEntrega estado;
    private Comprobante comprobante;

    public Entrega(String codigo, LocalDate fecha, Aportante aportante) {
        this.codigo = codigo;
        this.fecha = fecha;
        this.aportante = aportante;
        this.lotes = new LinkedList<>();
        this.estado = EstadoEntrega.ABIERTA;
        this.comprobante = null;
    }

    /**
     * Agrega un lote si la entrega esta abierta, el lote es valido (peso positivo)
     * y su codigo no se repite dentro de la entrega.
     */
    public boolean agregarLote(Lote lote) {
        if (estado != EstadoEntrega.ABIERTA || lote == null) {
            return false;
        }
        if (lote.getCodigo() == null || lote.getCodigo().isBlank() || lote.getPeso() <= 0) {
            return false;
        }
        for (Lote existente : lotes) {
            if (existente.getCodigo().equals(lote.getCodigo())) {
                return false;
            }
        }
        lotes.add(lote);
        return true;
    }

    /**
     * Acepta la entrega: suma el valor de los lotes de forma polimorfica y genera el comprobante.
     * Devuelve null (y no cambia nada) si la entrega esta vacia o ya fue aceptada.
     */
    public Comprobante aceptar() {
        if (estado != EstadoEntrega.ABIERTA || lotes.isEmpty()) {
            return null;
        }
        comprobante = new Comprobante(codigo, aportante.identificacion(),
                calcularPesoTotal(), calcularValorTotal());
        estado = EstadoEntrega.ACEPTADA;
        return comprobante;
    }

    public int calcularPesoTotal() {
        int total = 0;
        for (Lote lote : lotes) {
            total += lote.getPeso();
        }
        return total;
    }

    public double calcularValorTotal() {
        double total = 0;
        for (Lote lote : lotes) {
            total += lote.calcularValor(); // polimorfismo
        }
        return total;
    }

    /** Etiquetas de los lotes que implementan Etiquetable (metales y residuos electronicos). */
    public List<String> obtenerEtiquetas() {
        List<String> etiquetas = new LinkedList<>();
        for (Lote lote : lotes) {
            if (lote instanceof Etiquetable) {
                etiquetas.add(((Etiquetable) lote).generarEtiqueta());
            }
        }
        return etiquetas;
    }

    public String getCodigo() {
        return codigo;
    }

    public EstadoEntrega getEstado() {
        return estado;
    }

    public Comprobante getComprobante() {
        return comprobante;
    }

    /** Devuelve una copia para que nadie agregue lotes saltandose las validaciones. */
    public List<Lote> getLotes() {
        return new LinkedList<>(lotes);
    }
}