package hashindex.core;


public class Tuple {
    private String key;
    private String data;
    
    public Tuple(String key, String data) {
        this.key = key;
        this.data = data;
    }
    
    public String getKey() {
        return key;
    }
    
    public String getData() {
        return data;
    }
    
    @Override
    public String toString() {
        return "Tuple{key='" + key + "', data='" + data + "'}";
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Tuple tuple = (Tuple) obj;
        return key.equals(tuple.key);
    }
    
    @Override
    public int hashCode() {
        return key.hashCode();
    }
}
