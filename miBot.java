import java.io.File;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;


// Grupo 5 RIBW - Eduardo Gómez Almendral y Adrián Tercero Pérez
public class miBot {

    private static boolean esFicheroTexto(File f) {
        String nombre = f.getName().toLowerCase();
        return nombre.endsWith(".txt")
            || nombre.endsWith(".csv")
            || nombre.endsWith(".xml")
            || nombre.endsWith(".html")
            || nombre.endsWith(".htm")
            || nombre.endsWith(".java")
            || nombre.endsWith(".log");
    }
    public static void main(String[] args) throws Exception {
        if (args.length < 1) {
            System.out.println("ERROR. Ejecutar: >java miBot nombre_archivo");
            return;
        }
        
        ListIt li = new ListIt();
        FichContPalabras fcp = new FichContPalabras();
        SalvarObjeto so = new SalvarObjeto();
        CargarObjeto co = new CargarObjeto();
        MotorBusqueda motor = new MotorBusqueda();
        Thesauro thesauro = new Thesauro();

        Map<String, Integer> longitudesDocs = new HashMap<>();
        List<String> listaURLs = new ArrayList<>();

        // Creamos la cola en miBot 
        Queue<File> colaProcesamiento = new LinkedList<>();
        File inicial = new File(args[0]);
        File thesauroFile = new File("thesauro.set");

        if (li.esLegible(thesauroFile)) {
            Map<String, Set<String>> thesauroMap = (Map<String, Set<String>>) co.cargar("thesauro.set", 3);
            thesauro.setMap(thesauroMap);
        }
        else {
            thesauro.cargarThesauro("thesauro_ES.txt");
            so.guardar(thesauro.getMap(), "thesauro.set");
        }

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
            } else if (esFicheroTexto(actual)) { 
                listaURLs.add(actual.getPath());
                int docId = listaURLs.size();
                System.out.println("Procesando: " + actual.getName());
                int totalPalabras = fcp.procesarArchivo(actual.getPath(), docId - 1, thesauro);
                longitudesDocs.put(actual.getPath(), totalPalabras);
            } else {
                System.out.println("Ignorado (no es texto): " + actual.getName());
            }
        }

        File arbolFile = new File("fI.dir");

        if (arbolFile.exists()) {
            // Si el archivo existe
            System.out.println("El archivo fI.dir existe. Comprobando contenido...");
            //Map<String, Ocurrencia> mapaAntiguo = (Map<String, Ocurrencia>) co.cargar("fI.dir", 1);
            //List<String> fatAntigua = (List<String>) co.cargar("fI_fat.dir", 2);
            //Map<String, Integer> longitudesDocs = (Map<String, Integer>) co.cargar("fI_long.dir", 4);

        } else {
            // Si no existe, debemos crearlo
            System.out.println("El archivo fI.dir no existe. Se creará uno nuevo.");
        }

        // Salvar el mapa resultante en el fichero serializado
        so.guardar(fcp.getMap(), "fI.dir");
        so.guardar(listaURLs, "fI_fat.dir");
        so.guardar(longitudesDocs, "fI_long.dir");
        System.out.println("Proceso completado. Mapa guardado en fI.dir");

        // Exportar los resultados a un archivo de texto
        fcp.exportarResultados("finished1.txt", listaURLs);
        System.out.println("----------------------------------------------");
        System.out.println("Resultados de texto exportados a finished1.txt");
        System.out.println("----------------------------------------------");
        System.out.println("Términos disponibles en el índice:");
        System.out.println(fcp.getMap().keySet());

        System.out.println("\nPara poder salir, introduce '/salir' o deja la línea en blanco:");
        while (true) {
            String query = System.console().readLine("Introduce un término: ").toLowerCase();
            query = Normalizer.normalize(query, Normalizer.Form.NFD);
            query = query.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
            
            if (query.equalsIgnoreCase("/salir")){
                System.out.println("Cerrando..."); 
                break;
            }

            if (query.trim().isEmpty()){
                System.out.println("Cerrando..."); 
                break;
            }
            
            // Llamada al MotorBusqueda
            List<Map.Entry<Integer, Double>> ranking = motor.rankDocuments(query, fcp.getMap(), longitudesDocs, thesauro);

            if (ranking.isEmpty()) {
                System.out.println("No se encontraron coincidencias.");
            } else {
                int i = 1;
                for (Map.Entry<Integer, Double> res : ranking) {
                    System.out.printf("%d. %s (Score: %.4f)\n", i++, listaURLs.get(res.getKey()), res.getValue());
                    if (i > 10) break;
                }
            }
            System.out.println();
            System.out.println("----------------------------------------------");
        }
    }
}