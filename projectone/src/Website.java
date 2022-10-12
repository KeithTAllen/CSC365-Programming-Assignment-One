import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import javax.swing.plaf.IconUIResource;
import java.io.IOException;
public class Website extends Document {

    //instance variables
    HT hashTable = new HT();
    private int totalWords;
    double similarityScore;

    // Constructor
    public Website(String baseUri) {
        super(baseUri);

    }

    //to string just returns the baseUri
    //closest thing we have to a name for pages
    //without having to parse
    public String toString(){
        return baseUri();
    }

    //parse all the words into a hashtable
    public void makeHT() throws IOException {
        Document doc = Jsoup.connect(this.baseUri()).get();
        String text = doc.body().text().replaceAll("[^A-Za-z ]+","");
//        System.out.println(text);
        String[] allWords = text.split(" ");
        totalWords = allWords.length;
        //place all words into a HT
        for( int i = 0; i < allWords.length; i++){
            if (allWords[i] != null){
                hashTable.add(allWords[i]);
            }
        }
//        System.out.println(hashTable.toArrayList());
    }

    public void correctTF() {
        //freq/total words
        hashTable.correctTF(totalWords);
    }

    public void correctTFIDF(HT corpus){
        hashTable.correctTFIDF(corpus);
    }

    public double getTFIDFVector(HT corpus){
        return hashTable.getTFIDFVector(corpus);
    }
}
