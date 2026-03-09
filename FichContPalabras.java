import java.io.*;
import java.text.Normalizer;
import java.util.*;

public class FichContPalabras {
    private Map<String, Ocurrencia> map;

    public FichContPalabras() {
        this.map = new TreeMap<String, Ocurrencia>();
    }

    // Método para procesar un archivo y llenar el mapa en memoria
    public int procesarArchivo(String fichEntrada, Integer idDocument, Thesauro thesauro) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(fichEntrada));
        String linea;
        int contadorPalabras = 0;
        while ((linea = br.readLine()) != null) {
            StringTokenizer st = new StringTokenizer(linea, " ,.:;(){}!°?\t''%/|[]<=>&#+*$-¨^~");
            while (st.hasMoreTokens()) {
                String s = st.nextToken().toLowerCase();
                s = Normalizer.normalize(s, Normalizer.Form.NFD);
                s = s.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
                if (thesauro.tieneSinonimos(s)) {
                    contadorPalabras++;
                    Ocurrencia oc = map.get(s);
                    if (oc == null) {
                        oc = new Ocurrencia();
                        map.put(s, oc);
                    }
                    oc.incrementarFtg();
                    oc.agregarFicheroPadre(idDocument);
                }
            }
        }
        br.close();
        return contadorPalabras;
    }

    // Método para exportar los resultados a un archivo de texto crear finished1.txt
    public void exportarResultados(String fichSalida, List<String> listaURLs) throws IOException {
        PrintWriter pr = new PrintWriter(new FileWriter(fichSalida));
        for (String termino : map.keySet()) {
            Ocurrencia oc = map.get(termino);
        
            pr.print(termino + " -> Aparece en: ");
            
            // StringBuilder para concatenar todas las rutas de forma limpia
            StringBuilder detalles = new StringBuilder();
            
            // Recorremos el mapa interno de la ocurrencia
            for (Map.Entry<Integer, Integer> entrada : oc.getMap().entrySet()) {
                int docId = entrada.getKey();
                int frecuencia = entrada.getValue();
                
                String pathReal = listaURLs.get(docId);
                detalles.append("[").append(pathReal).append(" (").append(frecuencia).append(" veces)] ");
            }
            
            // Escribimos la línea completa en el fichero
            pr.println(detalles.toString() + " | Frecuencia total: " + oc.getFtg());
        }
        pr.close();
    }

    public Map<String, Ocurrencia> getMap() { return map; }
    public void setMap(Map<String, Ocurrencia> map) { this.map = map; }
}