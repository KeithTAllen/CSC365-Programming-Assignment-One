import java.io.IOException;

import java.util.ArrayList;

public class main {

    public static void main(String[] args) throws IOException, InterruptedException {
//        System.out.println("Hello World");

        // create the GUI and wait for an input
        WebsiteInput inputBox = new WebsiteInput();
        while(inputBox.buttonPressed == false) {
            Thread.sleep(200);
        }

        // user Website is created, and it creates its HT and updates its term frequency
        Website inputSite = new Website(inputBox.tf.getText());
        inputSite.makeHT();
        inputSite.correctTF();
//        System.out.println(inputSite.baseUri());

        //get the XML going, so we can use our 10 consistent sources
        XmlReader myXmlReader = new XmlReader("C:\\GitHub\\CSC365\\projectone\\websites.xml");
        //c reate a corpus to hold all keys of all websites
        HT corpus = new HT();
        // create the list of our consistent websites
        ArrayList<Website> websiteList = myXmlReader.getWebsites();

        for (Website w: websiteList) {
            //make HT for all websites in XML
            w.makeHT();
            //correct the freq to term freq.
            w.correctTF();
            //add all of our keys to the corpus
            for(Object o: w.hashTable.toArrayList()){
                corpus.add(o);
            }
        }
        //add user input to corpus
//        corpus.add(inputSite);
        //Originally I needed this to stop some methods from throwing
        //  null pointers, but I fixed that. I debated throwing it back
        //  in, but decided against it to give us a more fixed corpus.

        // corpus go through and divide all freq by 10
        corpus.correctIDF();

        // user input updates to true tfidf
        inputSite.correctTFIDF(corpus);

        // websiteList updates to true tfidf
        for (Website w: websiteList){
            w.correctTFIDF(corpus);
        }

        // now all websites have their freq in tfidf form now just to compare and return.
        ArrayList<Double> similarityList = new ArrayList<>();
        //for each website compare the like terms and dot product them with the input website
        for (Website w: websiteList){
            similarityList.add(w.getTFIDFVector(corpus));
        }
        // create the vector for user input
        Double inputSiteVector = inputSite.getTFIDFVector(corpus);

        // finds the lowest absolute value from the input and the vectors from
        //  our 10 websites.
        double minimum = 100 * Math.abs(similarityList.get(0)-inputSiteVector); //multiply by 100 to help java with rounding
        int indexOfMax = 0; //keep track of where we saw the lowest
        for (int i = 1; i < similarityList.size(); i++) {
//            System.out.println(Math.abs(similarityList.get(i)-inputSiteVector));
            // find lowest item in the array list
            if (minimum > (100 * Math.abs(similarityList.get(i)-inputSiteVector))) {
                minimum = similarityList.get(i);
                indexOfMax = i;
//                System.out.println(indexOfMax);
            }
        }

        // set out gui to show the most similar website
        Website out = websiteList.get(indexOfMax);
        inputBox.l.setText("You might also like: " + out.baseUri());
    }
}