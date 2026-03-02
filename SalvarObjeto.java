import java.io.*;

public class SalvarObjeto {
    public void guardar(Object o, String nombreFichero) {
        try {
            FileOutputStream fos = new FileOutputStream(nombreFichero);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(o);
            oos.close();
        } catch (Exception e) { 
            System.out.println("Error al salvar: " + e); 
        }
    }
}