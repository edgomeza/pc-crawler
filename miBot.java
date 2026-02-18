import java.io.File;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;


// Grupo 5 RIBW - Eduardo Gómez Almendral y Adrián Tercero Pérez
public class miBot {
    public static void main(String[] args) throws Exception {
        if (args.length < 1) {
            System.out.println("ERROR. Ejecutar: >java miBot nombre_archivo");
            return;
        }
        
        ListIt li = new ListIt();
        FichContPalabras fcp = new FichContPalabras();
        SalvarObjeto so = new SalvarObjeto();
        CargarObjeto co = new CargarObjeto();

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
                fcp.procesarArchivo(actual.getPath());
            }
        }

        File arbolFile = new File("fI.dir");

        if (arbolFile.exists()) {
            // Si el archivo existe, lo cargamos para comparar
            System.out.println("El archivo fI.dir existe. Comprobando contenido...");
            Map<String, Ocurrencia> mapaAntiguo = co.cargar("fI.dir");
            Map<String, Ocurrencia> mapaNuevo = fcp.getMap();

            // Comparamos el contenido de los mapas
            if (mapaAntiguo != null && mapaNuevo.equals(mapaAntiguo)) {
                System.out.println("El contenido del mapa actual es igual al guardado en fI.dir.");
                System.out.println("Finalizando bot");
                return;
            } 
        } else {
            // Si no existe, debemos crearlo
            System.out.println("El archivo fI.dir no existe. Se creará uno nuevo.");
        }

        // Salvar el mapa resultante en el fichero serializado
        so.guardar(fcp.getMap(), "fI.dir");
        System.out.println("Proceso completado. Mapa guardado en fI.dir");

        // Exportar los resultados a un archivo de texto
        fcp.exportarResultados("finished1.txt");
        System.out.println("Resultados de texto exportados a finished1.txt");
    }
}