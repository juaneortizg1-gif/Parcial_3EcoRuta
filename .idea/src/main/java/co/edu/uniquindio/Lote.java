package co.edu.uniquindio;
public abstract class Lote {

    protected String codigo;
    protected String descripcion;
    protected int peso;

    public Lote(String codigo, String descripcion, int peso) {
        this.codigo = codigo;
        this.descripcion = descripcion;
        this.peso = peso;
    }


    public abstract double calcularValor();

    public String getCodigo() {
        return codigo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public int getPeso() {
        return peso;
    }
}