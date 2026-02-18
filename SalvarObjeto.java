import java.io.*;
import java.util.*;

public class SalvarObjeto {
    // Recibe un mapa y el nombre del archivo como parámetros
    public void guardar(Map<String,Ocurrencia> mapa, String nombreFichero) {
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