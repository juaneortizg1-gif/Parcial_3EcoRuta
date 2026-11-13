package co.edu.uniquindio;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        if (args.length > 0 && args[0].equalsIgnoreCase("pruebas")) {
            ejecutarPruebas();
        } else {
            mostrarMenu();
        }
    }


    private static void mostrarMenu() {
        Empresa empresa = crearEmpresaConDatos();
        Scanner sc = new Scanner(System.in);
        boolean salir = false;

        while (!salir) {
            System.out.println();
            System.out.println("===== EcoRuta =====");
            System.out.println("1. Registrar aportante");
            System.out.println("2. Crear entrega");
            System.out.println("3. Agregar lote a una entrega");
            System.out.println("4. Aceptar entrega");
            System.out.println("5. Consultar comprobante");
            System.out.println("6. Consultar etiquetas");
            System.out.println("7. Consultar valor acumulado de un aportante");
            System.out.println("8. Ejecutar pruebas funcionales");
            System.out.println("0. Salir");
            System.out.print("Opcion: ");
            if (!sc.hasNextLine()) {
                break;
            }
            String opcion = sc.nextLine().trim();

            switch (opcion) {
                case "1" -> menuRegistrarAportante(sc, empresa);
                case "2" -> menuCrearEntrega(sc, empresa);
                case "3" -> menuAgregarLote(sc, empresa);
                case "4" -> menuAceptarEntrega(sc, empresa);
                case "5" -> menuConsultarComprobante(sc, empresa);
                case "6" -> menuConsultarEtiquetas(sc, empresa);
                case "7" -> menuConsultarAcumulado(sc, empresa);
                case "8" -> ejecutarPruebas();
                case "0" -> salir = true;
                default -> System.out.println("Opcion invalida.");
            }
        }
        System.out.println("Hasta luego.");
    }

    private static void menuRegistrarAportante(Scanner sc, Empresa empresa) {
        String id = leer(sc, "Identificacion: ");
        String nombre = leer(sc, "Nombre: ");
        String correo = leer(sc, "Correo: ");
        boolean ok = empresa.registrarAportante(new Aportante(id, nombre, correo));
        System.out.println(ok ? "Aportante registrado." : "Rechazado: datos invalidos o identificacion ya registrada.");
    }

    private static void menuCrearEntrega(Scanner sc, Empresa empresa) {
        String codigo = leer(sc, "Codigo de la entrega: ");
        String id = leer(sc, "Identificacion del aportante: ");
        boolean ok = empresa.crearEntrega(codigo, LocalDate.now(), id);
        System.out.println(ok ? "Entrega " + codigo + " creada en estado ABIERTA."
                : "Rechazado: codigo repetido o aportante desconocido.");
    }

    private static void menuAgregarLote(Scanner sc, Empresa empresa) {
        String codigoEntrega = leer(sc, "Codigo de la entrega: ");
        if (empresa.buscarEntrega(codigoEntrega) == null) {
            System.out.println("Rechazado: la entrega no existe.");
            return;
        }
        System.out.println("Tipo de lote: 1. Papel y carton  2. Metal  3. Residuo electronico");
        String tipo = leer(sc, "Tipo: ");
        String codigo = leer(sc, "Codigo del lote: ");
        String descripcion = leer(sc, "Descripcion: ");
        int peso = leerEntero(sc, "Peso (kg): ");

        Lote lote;
        try {
            switch (tipo) {
                case "1" -> {
                    String clasificado = leer(sc, "Clasificado antes de entregar? (s/n): ");
                    lote = new PapelCarton(codigo, descripcion, peso, clasificado.equalsIgnoreCase("s"));
                }
                case "2" -> {
                    String t = leer(sc, "Tipo de metal (ALUMINIO/ACERO): ").toUpperCase();
                    lote = new Metal(codigo, descripcion, peso, TipoMetal.valueOf(t));
                }
                case "3" -> {
                    int equipos = leerEntero(sc, "Equipos completos: ");
                    lote = new ResiduoElectronico(codigo, descripcion, peso, equipos);
                }
                default -> {
                    System.out.println("Tipo de lote invalido.");
                    return;
                }
            }
        } catch (IllegalArgumentException ex) {
            System.out.println("Rechazado: " + ex.getMessage());
            return;
        }

        boolean ok = empresa.agregarLote(codigoEntrega, lote);
        System.out.println(ok ? "Lote agregado."
                : "Rechazado: entrega aceptada, codigo de lote repetido o peso invalido.");
    }

    private static void menuAceptarEntrega(Scanner sc, Empresa empresa) {
        String codigo = leer(sc, "Codigo de la entrega: ");
        Comprobante c = empresa.aceptarEntrega(codigo);
        if (c == null) {
            System.out.println("Rechazado: la entrega no existe, esta vacia o ya fue aceptada.");
        } else {
            System.out.println("Entrega aceptada. " + describir(c));
        }
    }

    private static void menuConsultarComprobante(Scanner sc, Empresa empresa) {
        String codigo = leer(sc, "Codigo de la entrega: ");
        Comprobante c = empresa.consultarComprobante(codigo);
        System.out.println(c == null ? "No hay comprobante para esa entrega." : describir(c));
    }

    private static void menuConsultarEtiquetas(Scanner sc, Empresa empresa) {
        String codigo = leer(sc, "Codigo de la entrega: ");
        List<String> etiquetas = empresa.consultarEtiquetas(codigo);
        if (etiquetas.isEmpty()) {
            System.out.println("No hay etiquetas para esa entrega.");
        } else {
            for (String etiqueta : etiquetas) {
                System.out.println(" - " + etiqueta);
            }
        }
    }

    private static void menuConsultarAcumulado(Scanner sc, Empresa empresa) {
        String id = leer(sc, "Identificacion del aportante: ");
        System.out.println("Valor acumulado: " + dinero(empresa.consultarValorAcumulado(id)));
    }


    private static void ejecutarPruebas() {
        System.out.println();
        System.out.println("===== PRUEBAS FUNCIONALES ECORUTA =====");
        LocalDate fecha = LocalDate.of(2026, 11, 12);


        Empresa e1 = crearEmpresaConDatos();
        boolean crearE01 = e1.crearEntrega("E01", fecha, "1001");
        Entrega entrega1 = e1.buscarEntrega("E01");
        boolean estadoInicialOk = crearE01 && entrega1 != null
                && entrega1.getEstado() == EstadoEntrega.ABIERTA
                && entrega1.getLotes().isEmpty()
                && entrega1.getComprobante() == null;
        boolean repetida = e1.crearEntrega("E01", fecha, "1001");
        boolean desconocido = e1.crearEntrega("E02", fecha, "9999");
        boolean conservada = e1.buscarEntrega("E01") == entrega1 && e1.buscarEntrega("E02") == null;
        reportar(1, "Registro de una entrega",
                "crearEntrega(E01, aportante 1001); crearEntrega(E01, 1001); crearEntrega(E02, aportante 9999)",
                "E01 ABIERTA, sin lotes, sin comprobante; segunda y tercera operacion rechazadas; E01 conservada",
                "E01 creada=" + crearE01 + ", estado=" + (entrega1 == null ? "n/a" : entrega1.getEstado())
                        + ", lotes=" + (entrega1 == null ? "n/a" : entrega1.getLotes().size())
                        + ", comprobante=" + (entrega1 == null ? "n/a" : entrega1.getComprobante())
                        + "; codigo repetido=" + (repetida ? "aceptado" : "rechazado")
                        + "; aportante desconocido=" + (desconocido ? "aceptado" : "rechazado")
                        + "; E01 conservada=" + conservada,
                estadoInicialOk && !repetida && !desconocido && conservada);


        Empresa e2 = crearEmpresaConDatos();
        e2.crearEntrega("E01", fecha, "1001");
        boolean l01 = e2.agregarLote("E01", new PapelCarton("L01", "Cajas de carton", 5, false));
        boolean duplicado = e2.agregarLote("E01", new Metal("L01", "Latas", 3, TipoMetal.ACERO));
        boolean pesoCero = e2.agregarLote("E01", new PapelCarton("L02", "Periodico", 0, false));
        List<Lote> lotes2 = e2.buscarEntrega("E01").getLotes();
        boolean soloPrimero = lotes2.size() == 1 && lotes2.get(0).getCodigo().equals("L01");
        reportar(2, "Validacion de lotes",
                "agregarLote(L01, 5 kg); agregarLote(L01 repetido, 3 kg); agregarLote(L02, 0 kg)",
                "L01 aceptado; codigo repetido rechazado; peso 0 rechazado; la entrega conserva solo L01",
                "L01=" + (l01 ? "aceptado" : "rechazado")
                        + "; codigo repetido=" + (duplicado ? "aceptado" : "rechazado")
                        + "; peso 0=" + (pesoCero ? "aceptado" : "rechazado")
                        + "; lotes en la entrega=" + lotes2.size(),
                l01 && !duplicado && !pesoCero && soloPrimero);


        Empresa e3 = crearEmpresaConDatos();
        e3.crearEntrega("E01", fecha, "1001");
        Comprobante c3 = e3.aceptarEntrega("E01");
        Entrega entrega3 = e3.buscarEntrega("E01");
        boolean ok3 = c3 == null && entrega3.getEstado() == EstadoEntrega.ABIERTA
                && entrega3.getComprobante() == null;
        reportar(3, "Rechazo de una entrega vacia",
                "aceptarEntrega(E01) sin lotes",
                "Operacion rechazada, estado ABIERTA, sin comprobante",
                "comprobante devuelto=" + c3 + ", estado=" + entrega3.getEstado()
                        + ", comprobante guardado=" + entrega3.getComprobante(),
                ok3);


        Empresa e4 = crearEmpresaConDatos();
        e4.crearEntrega("E01", fecha, "1001");
        Lote papel = new PapelCarton("P01", "Papel clasificado", 10, true);
        Lote metal = new Metal("M01", "Latas de aluminio", 5, TipoMetal.ALUMINIO);
        Lote electronico = new ResiduoElectronico("R01", "Computadores", 4, 2);
        boolean agregados = e4.agregarLote("E01", papel) & e4.agregarLote("E01", metal)
                & e4.agregarLote("E01", electronico);
        Comprobante c4 = e4.aceptarEntrega("E01");
        Entrega entrega4 = e4.buscarEntrega("E01");
        boolean ok4 = agregados && c4 != null
                && entrega4.getEstado() == EstadoEntrega.ACEPTADA
                && c4.pesoTotal() == 19
                && c4.valorTotal() == 24200
                && papel.calcularValor() == 6000
                && metal.calcularValor() == 11000
                && electronico.calcularValor() == 7200
                && c4.codigoEntrega().equals("E01")
                && c4.idAportante().equals("1001");
        reportar(4, "Aceptacion y calculo con los tres tipos de lote",
                "E01 con: 10 kg papel/carton clasificado, 5 kg aluminio, 4 kg electronicos con 2 equipos; aceptarEntrega(E01)",
                "Estado ACEPTADA; comprobante 19 kg y $24.200; lotes $6.000, $11.000 y $7.200",
                "estado=" + entrega4.getEstado()
                        + "; comprobante=" + (c4 == null ? "null" : c4.pesoTotal() + " kg y " + dinero(c4.valorTotal()))
                        + "; lotes=" + dinero(papel.calcularValor()) + ", " + dinero(metal.calcularValor())
                        + ", " + dinero(electronico.calcularValor()),
                ok4);


        List<String> etiquetas = e4.consultarEtiquetas("E01");
        boolean hayMetal = false;
        boolean hayElectronico = false;
        for (String etiqueta : etiquetas) {
            if (etiqueta.contains("M01") && etiqueta.contains("ALUMINIO")) {
                hayMetal = true;
            }
            if (etiqueta.contains("R01") && etiqueta.contains("Equipos completos: 2")) {
                hayElectronico = true;
            }
        }
        boolean ok5 = etiquetas.size() == 2 && hayMetal && hayElectronico;
        reportar(5, "Consulta de etiquetas",
                "consultarEtiquetas(E01) de la entrega aceptada en la prueba 4",
                "Exactamente 2 etiquetas: metal con codigo M01 y ALUMINIO; electronico con codigo R01 y 2 equipos",
                etiquetas.size() + " etiquetas: " + etiquetas,
                ok5);
    }

    private static void reportar(int numero, String nombre, String entrada, String esperado,
                                 String obtenido, boolean exito) {
        System.out.println();
        System.out.println("Prueba " + numero + ": " + nombre);
        System.out.println("  Entrada  : " + entrada);
        System.out.println("  Esperado : " + esperado);
        System.out.println("  Obtenido : " + obtenido);
        System.out.println("  Resultado: " + (exito ? "APROBADA" : "FALLIDA"));
    }



    private static Empresa crearEmpresaConDatos() {
        Empresa empresa = new Empresa("EcoRuta");
        empresa.registrarAportante(new Aportante("1001", "Ana Gomez", "ana@correo.com"));
        empresa.registrarAportante(new Aportante("1002", "Supermercado El Ahorro", "contacto@elahorro.com"));
        empresa.registrarAportante(new Aportante("1003", "Colegio Nueva Vida", "admin@nuevavida.edu"));
        return empresa;
    }

    private static String describir(Comprobante c) {
        return "Comprobante -> entrega " + c.codigoEntrega() + ", aportante " + c.idAportante()
                + ", peso total " + c.pesoTotal() + " kg, valor total " + dinero(c.valorTotal());
    }

    private static String dinero(double valor) {
        NumberFormat formato = NumberFormat.getIntegerInstance(Locale.forLanguageTag("es-CO"));
        return "$" + formato.format(Math.round(valor));
    }

    private static String leer(Scanner sc, String mensaje) {
        System.out.print(mensaje);
        return sc.hasNextLine() ? sc.nextLine().trim() : "";
    }


    private static int leerEntero(Scanner sc, String mensaje) {
        try {
            return Integer.parseInt(leer(sc, mensaje));
        } catch (NumberFormatException ex) {
            return -1;
        }
    }
}