package hashindex.functions;

public class SimpleHashFunction implements HashFunction {
    @Override
    public int hash(String key, int numberOfBuckets) {
        return Math.abs(key.hashCode()) % numberOfBuckets;
    }
}
