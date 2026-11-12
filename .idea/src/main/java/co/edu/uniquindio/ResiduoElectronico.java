package co.edu.uniquindio;
public class ResiduoElectronico extends Lote implements Etiquetable {

    private static final double VALOR_KG = 1400;
    private static final double VALOR_EQUIPO_COMPLETO = 800;

    private int equiposCompletos;

    public ResiduoElectronico(String codigo, String descripcion, int peso, int equiposCompletos) {
        super(codigo, descripcion, peso);
        if (equiposCompletos < 0) {
            throw new IllegalArgumentException("Los equipos completos deben ser mayores o iguales a cero.");
        }
        this.equiposCompletos = equiposCompletos;
    }

    @Override
    public double calcularValor() {
        return peso * VALOR_KG + equiposCompletos * VALOR_EQUIPO_COMPLETO;
    }

    @Override
    public String generarEtiqueta() {
        return "Lote " + codigo + " - Equipos completos: " + equiposCompletos;
    }

    public int getEquiposCompletos() {
        return equiposCompletos;
    }
}