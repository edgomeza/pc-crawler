import java.io.File;
import java.util.LinkedList;
import java.util.Queue;

public class miBot {
    public static void main(String[] args) throws Exception {
        if (args.length < 1) {
            System.out.println("ERROR. Ejecutar: >java miBot nombre_archivo");
            return;
        }

        File arbolFile = new File("fI.dir");
        if (arbolFile.exists()) {
            System.out.println("El fichero fI.dir ya existe. No se realizará ninguna acción.");
            return;
        }
        
        ListIt li = new ListIt();
        FichContPalabras fcp = new FichContPalabras();
        SalvarObjeto so = new SalvarObjeto();

        // Creamos la cola en miBot 
        Queue<File> colaProcesamiento = new LinkedList<>();
        File inicial = new File(args[0]);

        if (li.esLegible(inicial)) {
            colaProcesamiento.add(inicial);
        }

        while (!colaProcesamiento.isEmpty()) {
            File actual = colaProcesamiento.poll(); // Extraemos el primer elemento

            if (actual.isDirectory()) {
                File[] hijos = li.listarHijos(actual);
                if (hijos != null) {
                    for (File h : hijos) {
                        colaProcesamiento.add(h); // Añadimos subcarpetas o archivos a la cola
                    }
                }
            } else {
                // Si es un archivo, procesamos sus palabras
                System.out.println("Procesando: " + actual.getName());
                fcp.procesarArchivo(actual.getAbsolutePath());
            }
        }

        // Salvar el mapa resultante en el fichero serializado
        so.guardar(fcp.getMap(), "fI.dir");
        System.out.println("Proceso completado. Mapa guardado en fI.dir");

        // Exportar los resultados a un archivo de texto
        fcp.exportarResultados("finished1.txt");
        System.out.println("Resultados de texto exportados a finished1.txt");
    }
}