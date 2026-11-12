package co.edu.uniquindio;
public class Metal extends Lote implements Etiquetable {

    private static final double VALOR_KG_ALUMINIO = 2200;
    private static final double VALOR_KG_ACERO = 900;

    private TipoMetal tipo;

    public Metal(String codigo, String descripcion, int peso, TipoMetal tipo) {
        super(codigo, descripcion, peso);
        if (tipo == null) {
            throw new IllegalArgumentException("El tipo de metal es obligatorio.");
        }
        this.tipo = tipo;
    }

    @Override
    public double calcularValor() {
        double valorPorKg = (tipo == TipoMetal.ALUMINIO) ? VALOR_KG_ALUMINIO : VALOR_KG_ACERO;
        return peso * valorPorKg;
    }

    @Override
    public String generarEtiqueta() {
        return "Lote " + codigo + " - Metal: " + tipo;
    }

    public TipoMetal getTipo() {
        return tipo;
    }
}