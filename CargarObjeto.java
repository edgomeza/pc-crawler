import java.io.*;
import java.util.*;

public class CargarObjeto {

    public static final int TIPO_MAPA = 1;
    public static final int TIPO_LISTA = 2;

    @SuppressWarnings("unchecked")
    public Object cargar(String nombreFichero, int tipo) {
        try {
            FileInputStream fis = new FileInputStream(nombreFichero);
            ObjectInputStream ois = new ObjectInputStream(fis);
            Object obj = ois.readObject();
            ois.close();

            switch (tipo) {
                case TIPO_MAPA:
                    return (Map<String, Ocurrencia>) obj;
                case TIPO_LISTA:
                    return (List<String>) obj;
                default:
                    System.out.println("Tipo desconocido: " + tipo);
                    return null;
            }
        } catch (Exception e) {
            System.out.println("Error al cargar: " + e);
            return null;
        }
    }
}