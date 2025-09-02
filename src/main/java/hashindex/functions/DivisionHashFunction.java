package hashindex.functions;

public class DivisionHashFunction implements HashFunction {
    @Override
    public int hash(String key, int numberOfBuckets) {
        int hash = 0;
        for (char c : key.toCharArray()) {
            hash = (hash * 31 + c) % numberOfBuckets;
        }
        return Math.abs(hash);
    }
}
