import java.util.Map;
import java.util.TreeMap;

public class Ocurrencia implements java.io.Serializable, Comparable<Ocurrencia> {
    private Integer ftg;
    private Map<String, Integer> map;

    public Ocurrencia() {
        this.ftg = 0;
        this.map = new TreeMap<String, Integer>();
    }

    public Integer getFtg() {
        return ftg;
    }

    public Map<String, Integer> getMap() {
        return map;
    }

    public void incrementarFtg() {
        this.ftg++;
    }

    public void agregarFicheroPadre(String nombreFichero) {
        this.map.put(nombreFichero, this.map.getOrDefault(nombreFichero, 0) + 1);
    }

    @Override
    public int compareTo(Ocurrencia o) {
        return this.ftg.compareTo(o.getFtg());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Ocurrencia)) return false;
        Ocurrencia other = (Ocurrencia) obj;
        return this.ftg.equals(other.ftg) && this.map.equals(other.map);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(ftg, map);
    }

    public String toString() {
        return map.toString() + " (ftg=" + ftg + ")";
    }
}
