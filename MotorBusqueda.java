import java.util.*;

public class MotorBusqueda {

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
     * Ranking para los términos exactos buscados (sin sinónimos).
     * Ordena primero por número de términos de la consulta presentes en el documento (desc),
     * y luego por score (desc).
     */
    public List<Map.Entry<Integer, Double>> rankDocumentosSoloTerminos(List<String> terminosConsulta, Map<String, Ocurrencia> indice, Map<String, Integer> longitudesDocs) {
        Map<Integer, Double> scores = new HashMap<>();
        Map<Integer, Integer> terminosPorDoc = new HashMap<>();

        for (String termino : terminosConsulta) {
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
                    terminosPorDoc.put(idDocumento, terminosPorDoc.getOrDefault(idDocumento, 0) + 1);
                }
            }
        }

        List<Map.Entry<Integer, Double>> result = new ArrayList<>(scores.entrySet());
        result.sort((a, b) -> {
            int cntA = terminosPorDoc.getOrDefault(a.getKey(), 0);
            int cntB = terminosPorDoc.getOrDefault(b.getKey(), 0);
            if (cntB != cntA) return cntB - cntA;
            return b.getValue().compareTo(a.getValue());
        });
        return result;
    }

    /**
     * Devuelve cuántos términos de la consulta aparecen en un documento dado.
     */
    public int contarTerminosEnDoc(Integer docId, List<String> terminosConsulta, Map<String, Ocurrencia> indice) {
        int count = 0;
        for (String t : terminosConsulta) {
            if (indice.containsKey(t) && indice.get(t).getMap().containsKey(docId)) {
                count++;
            }
        }
        return count;
    }

    /**
     * Ranking combinado para los sinónimos de todos los términos buscados (excluyendo los términos originales).
     */
    public List<Map.Entry<Integer, Double>> rankDocumentosSinonimos(List<String> terminosConsulta, Map<String, Ocurrencia> indice, Map<String, Integer> longitudesDocs, Thesauro thesauro) {
        Set<String> todosSinonimos = new HashSet<>();
        for (String termino : terminosConsulta) {
            Set<String> sins = thesauro.getSinonimos(termino);
            if (sins != null) todosSinonimos.addAll(sins);
        }
        todosSinonimos.removeAll(terminosConsulta);
        if (todosSinonimos.isEmpty()) return new ArrayList<>();
        return calcularRanking(todosSinonimos, indice, longitudesDocs);
    }

    /**
     * Ranking combinado de términos + sinónimos (usado por rankDocuments legacy).
     */
    public List<Map.Entry<Integer, Double>> rankDocuments(List<String> terminosConsulta, Map<String, Ocurrencia> indice, Map<String, Integer> longitudesDocs, Thesauro thesauro) {
        Set<String> grupoBusqueda = new HashSet<>(terminosConsulta);
        for (String termino : terminosConsulta) {
            Set<String> sins = thesauro.getSinonimos(termino);
            if (sins != null) grupoBusqueda.addAll(sins);
        }
        return calcularRanking(grupoBusqueda, indice, longitudesDocs);
    }
}
