package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static int INITIAL_CAPACITY = 16;
    private static double LOAD_FACTOR = 0.75;

    private int capacity;
    private int treshold;
    private int size;

    private Node<K, V>[] table;

    @Override
    public void put(K key, V value) {
        if (table == null) {
            capacity = INITIAL_CAPACITY;
            treshold = (int) (LOAD_FACTOR * capacity);

            table = new Node[capacity];
        }

        if (++size >= treshold) {
            resize();
        }

        fillMap(key, value);
    }

    @Override
    public V getValue(K key) {
        int index = hash(key) % capacity;

        if (table[index] != null) {
            Node<K, V> node = table[index];

            while (node != null) {
                if (node.key.equals(key)) {
                    return node.value;
                }

                node = node.next;
            }
        }

        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void fillMap(K key, V value) {
        int index = hash(key) % capacity;

        if (table[index] != null) {
            Node<K, V> node = table[index];

            while (node != null) {
                if (node.key.equals(key)) {
                    node.value = value;
                    return;
                }

                if (node.next == null) {
                    break;
                }

                node = node.next;
            }

            node.next = new Node<>(key, value);
        } else {
            table[index] = new Node<>(key, value);
        }
    }

    private void resize() {
        capacity <<= 1;
        treshold = (int) (capacity * LOAD_FACTOR);

        Node<K, V>[] oldTableResize = table;

        table = new Node[capacity];

        for (Node<K, V> oldTableElem : oldTableResize) {
            if (oldTableElem == null) {
                continue;
            }

            while (oldTableElem != null) {
                fillMap(oldTableElem.key, oldTableElem.value);

                oldTableElem = oldTableElem.next;
            }
        }
    }

    private int hash(Object key) {
        int h;
        return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> capacity);
    }

    private static class Node<K, V> {
        private K key;
        private V value;
        private int hash;
        private Node<K, V> next;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
            this.hash = key.hashCode();
        }
    }
}
