import java.io.*;
import java.util.*;

public class CargarObjeto {

    public static final int TIPO_MAPA = 1;
    public static final int TIPO_LISTA = 2;
    public static final int TIPO_THESAURO = 3;
    public static final int TIPO_LONGITUDES = 4;

    @SuppressWarnings("unchecked")
    public Object cargar(String nombreFichero, int tipo) {
        try {
            FileInputStream fis = new FileInputStream(nombreFichero);
            ObjectInputStream ois = new ObjectInputStream(fis);
            Object obj = ois.readObject();
            ois.close();

            switch (tipo) {
                case 1:
                    return (Map<String, Ocurrencia>) obj;
                case 2:
                    return (List<String>) obj;
                case 3:
                    return (Map<String, Set<String>>) obj;
                case 4:
                    return (Map<String, Integer>) obj;
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