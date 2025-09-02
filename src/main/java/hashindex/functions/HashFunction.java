package hashindex.functions;

public interface HashFunction {
    int hash(String key, int numberOfBuckets);
}
