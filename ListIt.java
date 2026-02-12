/*
 * ListIt.java: Lista contenido de ficheros textuales
 * (i) Felix R. Rodriguez, EPCC, Universidad de Extremadura, 2009-23
 * http://madiba.unex.es/
 */

import java.io.*;

public class ListIt {

    // Valida si la ruta existe y es legible
    public boolean esLegible(File f) {
        return f.exists() && f.canRead();
    }

    // Devuelve los archivos hijos si es un directorio, de lo contrario null
    public File[] listarHijos(File f) {
        if (f.isDirectory()) {
            return f.listFiles();
        }
        return null;
    }

    // Método para imprimir el contenido de un archivo textual
    public void leerArchivo(File f) {
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                System.out.println(linea);
            }
        } catch (IOException e) {
            System.out.println("ERROR. No se pudo leer el archivo: " + e.getMessage());
        }
    }
}