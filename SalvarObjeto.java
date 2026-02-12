/*
 * SalvarObjeto.java: Guarda un objeto serializable en un fichero
 * (i) Felix R. Rguez., EPCC, Universidad de Extremadura, 2009-23
 * http://madiba.unex.es/
 */

import java.io.*;
import java.util.*;

public class SalvarObjeto {
    // Recibe un mapa y el nombre del archivo como parámetros
    public void guardar(Map<String, Integer> mapa, String nombreFichero) {
        try {
            FileOutputStream fos = new FileOutputStream(nombreFichero);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(mapa);
            oos.close();
        } catch (Exception e) { 
            System.out.println("Error al salvar: " + e); 
        }
    }
}