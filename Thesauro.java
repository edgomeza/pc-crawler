import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.text.Normalizer;
import java.util.*;
import java.util.regex.Pattern;
public class Thesauro {
    private Map<String, Set<String>> map;

    public Thesauro() {
        this.map = new HashMap<>();
    }

    public void cargarThesauro(String rutaFichero) {
        System.out.println("Cargando relaciones de sinónimos desde: " + rutaFichero);
        
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(rutaFichero), "ISO-8859-1"))) {
            String linea;
            Pattern patternEspacios = Pattern.compile("\\s+");

            while ((linea = br.readLine()) != null) {
                if (linea.startsWith("#") || linea.trim().isEmpty()) continue;

                //Obtenemos todas las palabras de la línea
                String[] partes = linea.split("\\;");
                List<String> grupoPalabras = new ArrayList<>();

                //Las limpiamos y validamos (sin términos compuestos)
                for (String p : partes) {
                    String limpio = Normalizer.normalize(p, Normalizer.Form.NFD);
                    limpio = limpio.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");

                    if (limpio != null && !limpio.isEmpty() && !patternEspacios.matcher(limpio).find()) {
                        grupoPalabras.add(limpio);
                    }
                }

                //Relación bidireccional
                for (String termino : grupoPalabras) {
                    map.putIfAbsent(termino, new HashSet<>());
                    
                    for (String sinonimo : grupoPalabras) {
                        if (!termino.equals(sinonimo)) {
                            map.get(termino).add(sinonimo);
                        }
                    }
                }
            }
            System.out.println("Thesauro cargado. Términos con sinónimos: " + map.size());

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // Método clave para el Motor de Búsqueda
    public Set<String> getSinonimos(String termino) {
        return map.getOrDefault(termino, new HashSet<>());
    }
    
    // Método auxiliar para filtrar al indexar
    public boolean tieneSinonimos(String termino) {
        return map.containsKey(termino);
    }

    public Map<String, Set<String>> getMap() { return map; }
    public void setMap(Map<String, Set<String>> map) { this.map = map; }
}
