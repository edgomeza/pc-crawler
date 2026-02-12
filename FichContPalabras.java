/*
 * FichContPalabras.java: Contabiliza palabras contenidas en un fichero
 * (i) Felix R. Rguez., EPCC, Universidad de Extremadura, 2009-23
 * http://madiba.unex.es/
 */

import java.io.*;
import java.util.*;

public class FichContPalabras {
    private Map<String, Integer> map;

    public FichContPalabras() {
        this.map = new TreeMap<>();
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
                map.put(s, map.getOrDefault(s, 0) + 1);
            }
        }
        br.close();
    }

    // Método para exportar los resultados a un archivo de texto crear fichS.txt
    public void exportarResultados(String fichSalida) throws IOException {
        PrintWriter pr = new PrintWriter(new FileWriter(fichSalida));
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            pr.println(entry.getKey() + " : " + entry.getValue());
        }
        pr.close();
    }

    public Map<String, Integer> getMap() { return map; }
    public void setMap(Map<String, Integer> map) { this.map = map; }
}