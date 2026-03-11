import java.util.*;

public class MotorBusqueda {

    /**
     * Calcula el ranking de documentos para una consulta dada.
     */
    public List<Map.Entry<Integer, Double>> rankDocuments(List<String> terminosConsulta, Map<String, Ocurrencia> indice, Map<String, Integer> longitudesDocs, Thesauro thesauro) {
        
        // Mapa para acumular la puntuación de cada documento
        Map<Integer, Double> scores = new HashMap<>();
            
        for (String terminoUsuario : terminosConsulta) {
            // Creamos un grupo de búsqueda: El término original + sus sinónimos
            Set<String> grupoBusqueda = new HashSet<>();
            grupoBusqueda.add(terminoUsuario);
            
            Set<String> sinonimos = thesauro.getSinonimos(terminoUsuario);
            grupoBusqueda.addAll(sinonimos);

            for (String termino : grupoBusqueda) {          
                // Si el término existe en el índice
                if (indice.containsKey(termino)) {
                    Ocurrencia oc = indice.get(termino);
                    // Calculamos el peso del término: a mayor TTF, menor peso
                    double ttf = oc.getFtg();
                    double pesoTermino = 1.0 / Math.log(1 + ttf);

                    // Iteramos sobre los documentos donde aparece este término
                    for (Map.Entry<Integer, Integer> entradaDoc : oc.getMap().entrySet()) {
                        Integer idDocumento = entradaDoc.getKey();
                        double tf = entradaDoc.getValue(); // Frecuencia del término en este doc
                        
                        // Obtenemos la longitud total del documento
                        double longitudDoc = longitudesDocs.getOrDefault(idDocumento, 1);

                        // Normalización: TF / L_d
                        double tfNormalizado = tf / longitudDoc;

                        // Cálculo parcial para este término y este documento
                        double scoreParcial = tfNormalizado * pesoTermino;

                        // Acumulamos el score en el documento correspondiente
                        scores.put(idDocumento, scores.getOrDefault(idDocumento, 0.0) + scoreParcial);
                    }
                }
            }
        }

        // Ordenar resultados de mayor a menor score
        List<Map.Entry<Integer, Double>> rankingResult = new ArrayList<>(scores.entrySet());
        rankingResult.sort((a, b) -> b.getValue().compareTo(a.getValue()));

        return rankingResult;
    }
}
