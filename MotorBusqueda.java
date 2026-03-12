import java.util.*;

public class MotorBusqueda {

    /**
     * Método privado que calcula el ranking de documentos para un conjunto de términos.
     */
    private List<Map.Entry<Integer, Double>> calcularRanking(Set<String> terminos, Map<String, Ocurrencia> indice, Map<String, Integer> longitudesDocs) {
        Map<Integer, Double> scores = new HashMap<>();

        for (String termino : terminos) {
            if (indice.containsKey(termino)) {
                Ocurrencia oc = indice.get(termino);
                double ttf = oc.getFtg();
                double pesoTermino = 1.0 / Math.log(1 + ttf);

                for (Map.Entry<Integer, Integer> entradaDoc : oc.getMap().entrySet()) {
                    Integer idDocumento = entradaDoc.getKey();
                    double tf = entradaDoc.getValue();
                    double longitudDoc = longitudesDocs.getOrDefault(idDocumento, 1);
                    double tfNormalizado = tf / longitudDoc;
                    double scoreParcial = tfNormalizado * pesoTermino;
                    scores.put(idDocumento, scores.getOrDefault(idDocumento, 0.0) + scoreParcial);
                }
            }
        }

        List<Map.Entry<Integer, Double>> result = new ArrayList<>(scores.entrySet());
        result.sort((a, b) -> b.getValue().compareTo(a.getValue()));
        return result;
    }

    /**
     * Ranking para el término exacto buscado (sin sinónimos).
     */
    public List<Map.Entry<Integer, Double>> rankDocumentosSoloTermino(String terminoUsuario, Map<String, Ocurrencia> indice, Map<String, Integer> longitudesDocs) {
        Set<String> soloTermino = new HashSet<>();
        soloTermino.add(terminoUsuario);
        return calcularRanking(soloTermino, indice, longitudesDocs);
    }

    /**
     * Ranking combinado para los sinónimos del término buscado (excluyendo el término original).
     */
    public List<Map.Entry<Integer, Double>> rankDocumentosSinonimos(String terminoUsuario, Map<String, Ocurrencia> indice, Map<String, Integer> longitudesDocs, Thesauro thesauro) {
        Set<String> sinonimos = thesauro.getSinonimos(terminoUsuario);
        if (sinonimos == null || sinonimos.isEmpty()) {
            return new ArrayList<>();
        }
        return calcularRanking(sinonimos, indice, longitudesDocs);
    }
}
