import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;

class HT implements java.io.Serializable {
    public HT() {
    }

    static final class Node {
        Object key;
        Node next;
        double freq;
        Node(Object k, Node n) {
            key = k; next = n;
            freq = 0;
        }
    }
    Node[] table = new Node[8];
    int size = 0;
    private void writeObject(ObjectOutputStream s) throws IOException {
        s.defaultWriteObject();
        s.writeInt(size);
        for (int i = 0; i < table.length; ++i) {
            for (Node e = table[i]; e != null; e = e.next) {
                s.writeObject(e.key);
            }
        }
    }
    private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
        s.defaultReadObject();
        int n = s.readInt();
        for (int i = 0; i < n; ++i)
            add(s.readObject());
    }
    boolean contains(Object key) {
        int h = key.hashCode();
        int i = h & (table.length - 1);
        for (Node e = table[i]; e != null; e = e.next) {
            if (key.equals(e.key))
                return true;
        }
        return false;
    }

    // same as contains, but instead of a boolean returns
    //  a node reference. Then we can pull its freq value for
    //  computations,
    Node findNode(Object key) {
        int h = key.hashCode();
        int i = h & (table.length - 1);
        for (Node e = table[i]; e != null; e = e.next) {
            if (key.equals(e.key))
                return e;
        }
        return null;
    }
    void add(Object key) {
        int h = key.hashCode();
        int i = h & (table.length - 1);
        for (Node e = table[i]; e != null; e = e.next) {
            if (key.equals(e.key)) {
                e.freq++;
                return;
            }
        }
        table[i] = new Node(key, table[i]);
        ++size;
        if ((float)size/table.length >= 0.75f)
            resize();
    }
    void resize() {
        Node[] oldTable = table;
        int oldCapacity = oldTable.length;
        int newCapacity = oldCapacity << 1;
        Node[] newTable = new Node[newCapacity];
        for (int i = 0; i < oldCapacity; ++i) {
            for (Node e = oldTable[i]; e != null; e = e.next) {
                int h = e.key.hashCode();
                int j = h & (newTable.length - 1);
                newTable[j] = new Node(e.key, newTable[j]);
            }
        }
        table = newTable;
    }
    void remove(Object key) {
        int h = key.hashCode();
        int i = h & (table.length - 1);
        Node e = table[i], p = null;
        while (e != null) {
            if (key.equals(e.key)) {
                if (p == null)
                    table[i] = e.next;
                else
                    p.next = e.next;
                break;
            }
            p = e;
            e = e.next;
        }
    }

    ArrayList<Object> toArrayList(){
        var list = new ArrayList<Object>();
        for(int i = 0; i<table.length; i++){
            for(Node e = table[i]; e != null; e = e.next){
                list.add(e.key);
            }
        }
        return list;
    }

    //takes term freq and divides, but total words in the Website
    void correctTF(int totalWords){
        for (int i = 0; i < table.length; ++i) {
            for (Node e = table[i]; e != null; e = e.next) {
                e.freq = e.freq/totalWords;
//                System.out.println(e.freq);
            }
        }
    }

    // finds idf values from freq in the corpus
    //  the log function kept throwing negative infinities which was
    //  really messing with java. I decided that when a number was too
    //  low to set it to 0. Then later with the dot product it would
    //  then also remain zero. I do wonder if this offset some of the
    //  effectivity of tf-idf form, but it is at least consistent
    void correctIDF(){
        for (int i = 0; i < table.length; ++i) {
            for (Node e = table[i]; e != null; e = e.next) {
                if (Math.log(e.freq/10) > -10000000) {
                    e.freq = Math.log(e.freq / 10);
                } else {
                    e.freq = 0;
                }

//                System.out.println(e.freq);
            }
        }
    }

    // takes all freq. (which would be in tf form) and mult.
    //  them by the idf value found in the corpus
    void correctTFIDF(HT corpus){
        for (int i = 0; i < table.length; ++i) {
            for (Node e = table[i]; e != null; e = e.next) {
                //corpus needs to find its frequency for this key
                if(e.key != null) {
                    Node thisNode = corpus.findNode(e.key);
                    if (thisNode != null) {
                        double temp = e.freq;
                        e.freq = temp * thisNode.freq;
                    }
                }
            }
        }
    }

    // getTFIDFVector takes the TFIDF from all words that match in a
    //  website to those in the corpus and dot products corresponding
    //  ones together to give us a similarity vector
    double getTFIDFVector(HT corpus){
        ArrayList<Double> corpusList = new ArrayList<>();
        ArrayList<Double> siteList = new ArrayList<>();
        for (int i = 0; i < table.length; ++i) {
            for (Node e = table[i]; e != null; e = e.next) {
                //corpus needs to find its frequency for this key
                Node corpusNode = corpus.findNode(e.key);
                if (corpusNode != null) {
                    corpusList.add(corpusNode.freq);
                    siteList.add(e.freq);
                }
            }
        }
        double sum = 0;
        for (int i = 0; i < corpusList.size(); i++) {
            sum += corpusList.get(i) * siteList.get(i);
        }
//        System.out.println(sum);
        return sum;
    }
}