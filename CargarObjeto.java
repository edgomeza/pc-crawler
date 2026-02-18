import java.io.*;
import java.util.*;

public class CargarObjeto {
    // Devuelve el mapa recuperado del archivo serializado
    @SuppressWarnings("unchecked")
    public Map<String, Integer> cargar(String nombreFichero) {
        try {
            FileInputStream fis = new FileInputStream(nombreFichero);
            ObjectInputStream ois = new ObjectInputStream(fis);
            Map<String, Integer> h = (Map<String, Integer>) ois.readObject();
            ois.close();
            return h;
        } catch (Exception e) { 
            System.out.println("Error al cargar: " + e); 
            return null;
        }
    }
}