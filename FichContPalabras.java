import java.io.*;
import java.util.*;

public class FichContPalabras {
    private Map<String, Ocurrencia> map;

    public FichContPalabras() {
        this.map = new TreeMap<String, Ocurrencia>();
    }

    // Método para procesar un archivo y llenar el mapa en memoria
    public void procesarArchivo(String fichEntrada) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(fichEntrada));
        String linea;
        while ((linea = br.readLine()) != null) {
            // Se mantiene la lógica de filtrado de caracteres especiales
            StringTokenizer st = new StringTokenizer(linea, " ,.:;(){}!°?\t''%/|[]<=>&#+*$-¨^~");
            while (st.hasMoreTokens()) {
                String s = st.nextToken();
                Ocurrencia oc = map.get(s);
                if (oc == null) {
                    oc = new Ocurrencia();
                    map.put(s, oc);
                }
                oc.incrementarFtg();
                oc.agregarFicheroPadre(fichEntrada);
            }
        }
        br.close();
    }

    // Método para exportar los resultados a un archivo de texto crear fichS.txt
    public void exportarResultados(String fichSalida) throws IOException {
        PrintWriter pr = new PrintWriter(new FileWriter(fichSalida));
        for (String entry : map.keySet()) {
            pr.println(entry + " : " + map.get(entry));
        }
        pr.close();
    }

    public Map<String, Ocurrencia> getMap() { return map; }
    public void setMap(Map<String, Ocurrencia> map) { this.map = map; }
}