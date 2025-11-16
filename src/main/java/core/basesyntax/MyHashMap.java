package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    public static final int BASE_SIZE = 0;
    public static final int DEFAULT_CAPACITY = 16;
    public static final double LOAD_FACTOR = 0.75;
    public static final int GROWN_SHIFT_AMOUNT = 1;

    private static int capacity;
    private int size;
    private Node<K, V>[] mapData;
    private int threshold;

    public MyHashMap() {
        size = BASE_SIZE;
        capacity = DEFAULT_CAPACITY;
        threshold = (int) (capacity * LOAD_FACTOR);
        mapData = (Node<K, V>[]) new Node[DEFAULT_CAPACITY];;
    }

    @Override
    public void put(K key, V value) {
        checkThreshold();
        int index = getIndex(key);
        Node<K, V> node = mapData[index];

        while (node != null) {
            if (isEquals(node.key, key)) {
                node.value = value;
                return;
            }
            node = node.next;
        }

        Node<K, V> newNode = new Node<>(key, value, mapData[index]);
        mapData[index] = newNode;
        size++;
    }

    @Override
    public V getValue(K key) {
        int index = getIndex(key);
        Node<K, V> node = mapData[index];

        while (node != null) {
            if (isEquals(node.key, key)) {
                return node.value;
            }
            node = node.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }

    }

    private void checkThreshold() {
        int requiredThreshold = size + 1;
        if (requiredThreshold <= threshold) {
            return;
        }

        int oldCapacity = capacity;
        int newCapacity = (oldCapacity << GROWN_SHIFT_AMOUNT);
        int newThreshold = (int) (newCapacity * LOAD_FACTOR);

        Node<K, V>[] newMapData = new Node[newCapacity];

        for (Node<K, V> currentNode : mapData) {
            while (currentNode != null) {
                Node<K, V> next = currentNode.next;
                int index = currentNode.key == null ? 0
                        : Math.abs(currentNode.key.hashCode() % newCapacity);
                currentNode.next = newMapData[index];
                newMapData[index] = currentNode;
                currentNode = next;
            }
        }
        mapData = newMapData;
        threshold = newThreshold;
    }

    private int getIndex(K key) {
        return (key == null) ? 0 : Math.abs(key.hashCode() % mapData.length);
    }

    private boolean isEquals(K key1, K key2) {
        return Objects.equals(key1, key2);
    }
}
