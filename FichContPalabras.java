import java.io.*;
import java.util.*;

public class FichContPalabras {
    private Map<String, Ocurrencia> map;

    public FichContPalabras() {
        this.map = new TreeMap<String, Ocurrencia>();
    }

    // Método para procesar un archivo y llenar el mapa en memoria
    public int procesarArchivo(String fichEntrada, Integer idDocument) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(fichEntrada));
        String linea;
        int contadorPalabras = 0;
        while ((linea = br.readLine()) != null) {
            StringTokenizer st = new StringTokenizer(linea, " ,.:;(){}!°?\t''%/|[]<=>&#+*$-¨^~");
            while (st.hasMoreTokens()) {
                String s = st.nextToken();
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
        br.close();
        return contadorPalabras;
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