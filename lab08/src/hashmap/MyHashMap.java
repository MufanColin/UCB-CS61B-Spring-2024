package hashmap;

import org.junit.jupiter.params.aggregator.ArgumentsAccessorKt;

import java.util.*;

/**
 *  A hash table-backed Map implementation.
 *
 *  Assumes null keys will never be inserted, and does not resize down upon remove().
 *  @author YOUR NAME HERE
 */
public class MyHashMap<K, V> implements Map61B<K, V> {
    /**
     * Protected helper class to store key/value pairs
     * The protected qualifier allows subclass access
     */
    protected class Node {
        K key;
        V value;

        Node(K k, V v) {
            key = k;
            value = v;
        }
    }

    /* Instance Variables */
    private Collection<Node>[] buckets;
    private int initialCapacity;
    private double loadFactor;
    private int size;
    private List<K> keysList;

    /** Constructors */
    public MyHashMap() {
        // Java's built-in HashMap settings
        this.keysList = new ArrayList<>();
        this.size = 0;
        this.initialCapacity = 16;
        this.loadFactor = 0.75;
        buckets = new Collection[this.initialCapacity];
        for (int i = 0; i < this.initialCapacity; i++) {
            buckets[i] = createBucket();
        }
    }

    public MyHashMap(int initialCapacity) {
        this.keysList = new ArrayList<>();
        this.size = 0;
        this.initialCapacity = initialCapacity;
        this.loadFactor = 0.75;
        buckets = new Collection[this.initialCapacity];
        for (int i = 0; i < this.initialCapacity; i++) {
            buckets[i] = createBucket();
        }
    }

    /**
     * MyHashMap constructor that creates a backing array of initialCapacity.
     * The load factor (# items / # buckets) should always be <= loadFactor
     *
     * @param initialCapacity initial size of backing array
     * @param loadFactor maximum load factor
     */
    public MyHashMap(int initialCapacity, double loadFactor) {
        this.keysList = new ArrayList<>();
        this.size = 0;
        this.initialCapacity = initialCapacity;
        this.loadFactor = loadFactor;
        buckets = new Collection[this.initialCapacity];
        for (int i = 0; i < this.initialCapacity; i++) {
            buckets[i] = createBucket();
        }
    }

    /**
     * Returns a data structure to be a hash table bucket
     *
     * The only requirements of a hash table bucket are that we can:
     *  1. Insert items (`add` method)
     *  2. Remove items (`remove` method)
     *  3. Iterate through items (`iterator` method)
     *  Note that that this is referring to the hash table bucket itself,
     *  not the hash map itself.
     *
     * Each of these methods is supported by java.util.Collection,
     * Most data structures in Java inherit from Collection, so we
     * can use almost any data structure as our buckets.
     *
     * Override this method to use different data structures as
     * the underlying bucket type
     *
     * BE SURE TO CALL THIS FACTORY METHOD INSTEAD OF CREATING YOUR
     * OWN BUCKET DATA STRUCTURES WITH THE NEW OPERATOR!
     */
    protected Collection<Node> createBucket() {
        // 1st choice: LinkedList
        return new LinkedList<>();
        // 2nd choice: ArrayList
        // return new ArrayList<>();
    }

    @Override
    public void put(K key, V value) {
        int hashCode = key.hashCode();
        int destBucketIndex = Math.floorMod(hashCode, this.initialCapacity);
        Collection<Node> destBucket = buckets[destBucketIndex];
        if (!containsKey(key)) {
            this.keysList.add(key);
            // do have to increase size by 1
            Node newNode = new Node(key, value);
            destBucket.add(newNode);
            size += 1;
        } else {
            // do not have to modify size
            for (Node n: destBucket) {
                if (key.equals(n.key)) {
                    n.value = value;
                    break;
                }
            }
        }
        double currentLoadFactor = size() * 1.0 / this.initialCapacity;
        if (currentLoadFactor > this.loadFactor) {
            resize(this.initialCapacity * 2);
        }
    }

    private void resize(int newCapacity) {
        Collection<Node>[] newBuckets = new Collection[newCapacity];
        for (int i = 0; i < newCapacity; i++) {
            newBuckets[i] = createBucket();
        }
        for (int i = 0; i < this.initialCapacity; i++) {
            Collection<Node> currentBucket = this.buckets[i];
            for (Node n: currentBucket) {
                int hashCode = n.key.hashCode();
                int destBucketIndex = Math.floorMod(hashCode, newCapacity);
                newBuckets[destBucketIndex].add(n);
            }
        }
        this.initialCapacity = newCapacity;
        this.buckets = newBuckets;
    }

    @Override
    public V get(K key) {
        if (containsKey(key)) {
            int hashCode = key.hashCode();
            int bucketIndex = Math.floorMod(hashCode, this.initialCapacity);
            for (Node n : this.buckets[bucketIndex]) {
                if (key.equals(n.key)) {
                    return n.value;
                }
            }
        }
        return null;
    }

    @Override
    public boolean containsKey(K key) {
        int hashCode = key.hashCode();
        int bucketIndex = Math.floorMod(hashCode, this.initialCapacity);
        for (Node n: this.buckets[bucketIndex]) {
            if (key.equals(n.key)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        for (int i = 0; i < this.initialCapacity; i++) {
            buckets[i].clear();
        }
        size = 0;
    }

    @Override
    public Set<K> keySet() {
        return new HashSet<>(keysList);
    }

    @Override
    public V remove(K key) {
        if (containsKey(key)) {
            V returnValue = get(key);
            int hashCode = key.hashCode();
            int bucketIndex = Math.floorMod(hashCode, this.initialCapacity);
            for (Node n: buckets[bucketIndex]) {
                if (key.equals(n.key)) {
                    buckets[bucketIndex].remove(n);
                    break;
                }
            }
            keysList.remove(key);
            return returnValue;
        }
        return null;
    }

    @Override
    public Iterator<K> iterator() {
        return new HashMapIterator();
    }

    private class HashMapIterator implements Iterator<K> {
        private int wizPos;
        HashMapIterator() {
            wizPos = 0;
        }
        @Override
        public boolean hasNext() {
            return wizPos < keysList.size();
        }

        @Override
        public K next() {
            K keyReturn = keysList.get(wizPos);
            wizPos += 1;
            return keyReturn;
        }
    }
}
