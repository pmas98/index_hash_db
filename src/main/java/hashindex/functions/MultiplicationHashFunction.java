package hashindex.functions;

public class MultiplicationHashFunction implements HashFunction {
    private static final double A = 0.6180339887; // Razão áurea
    
    @Override
    public int hash(String key, int numberOfBuckets) {
        int hash = 0;
        for (char c : key.toCharArray()) {
            hash = hash * 31 + c;
        }
        
        double fractionalPart = (hash * A) % 1.0;
        int result = (int) (fractionalPart * numberOfBuckets);
        return Math.abs(result) % numberOfBuckets;
    }
}
