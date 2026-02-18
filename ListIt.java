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
}