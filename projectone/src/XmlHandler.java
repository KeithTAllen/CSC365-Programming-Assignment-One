import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;

public class XmlHandler extends DefaultHandler {

    ArrayList<Website> siteList = new ArrayList<>(); // ArrayList of all sites
    Website currentWebsite;  // Reference to the current Product
    String currentUrl; // Reference to current URL

    public void startElement(String uri, String localName, String qName,
                             Attributes attributes) throws SAXException {
        //System.out.println("Start element");
        // Check if this is an opening element for Website
        // If so, store instantiate it and add it to the list
        if(qName.equals("site")) {
            String currentUrl = attributes.getValue("url");
            Website currentWebsite = new Website(currentUrl);
            siteList.add(currentWebsite);
            }
        }

    public void endElement(String uri, String localName, String qName) throws SAXException {
        //System.out.println("End element");
        // check if this is a closing element for a Website
        if(qName.equals("site")) {

        }
    }

    // Return a reference to array list
    public ArrayList<Website> getWebsites() {
        return siteList;
    }
}
