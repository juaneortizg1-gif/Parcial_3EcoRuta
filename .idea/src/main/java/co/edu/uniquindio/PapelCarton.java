package co.edu.uniquindio;
public class PapelCarton extends Lote {

    private static final double VALOR_KG = 500;
    private static final double ADICIONAL_CLASIFICADO_KG = 100;

    private boolean clasificado;

    public PapelCarton(String codigo, String descripcion, int peso, boolean clasificado) {
        super(codigo, descripcion, peso);
        this.clasificado = clasificado;
    }

    @Override
    public double calcularValor() {
        double valorPorKg = VALOR_KG + (clasificado ? ADICIONAL_CLASIFICADO_KG : 0);
        return peso * valorPorKg;
    }

    public boolean isClasificado() {
        return clasificado;
    }
}