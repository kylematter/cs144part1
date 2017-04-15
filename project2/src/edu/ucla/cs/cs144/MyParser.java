/* CS144
 *
 * Parser skeleton for processing item-???.xml files. Must be compiled in
 * JDK 1.5 or above.
 *
 * Instructions:
 *
 * This program processes all files passed on the command line (to parse
 * an entire diectory, type "java MyParser myFiles/*.xml" at the shell).
 *
 * At the point noted below, an individual XML file has been parsed into a
 * DOM Document node. You should fill in code to process the node. Java's
 * interface for the Document Object Model (DOM) is in package
 * org.w3c.dom. The documentation is available online at
 *
 * http://java.sun.com/j2se/1.5.0/docs/api/index.html
 *
 * A tutorial of Java's XML Parsing can be found at:
 *
 * http://java.sun.com/webservices/jaxp/
 *
 * Some auxiliary methods have been written for you. You may find them
 * useful.
 */

package edu.ucla.cs.cs144;

import java.io.*;
import java.text.*;
import java.util.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.ErrorHandler;
import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;


class Item {
	String itemID;
	String name;
	String buyPrice;
	String firstBid;
	String currently;
	int numBids;
	String description;
	String started;
	String ends;
	String latitude;
	String longitude;
	String location;
	String country;
	String sellerID;
	
	public Item(String id) {
		this.itemID = id;
	}

	public void setAttrs(String name, String firstBid, String currently,
				String started, String ends, int numBids,
				String location, String country, String des) {
		this.name = name;
		this.firstBid = firstBid;
		this.currently = currently;
		this.started = started;
		this.ends = ends;
		this.numBids = numBids;
		this.location = location;
		this.country = country;
		this.description = des;
	}

	public void setBuyPrice(String buyPrice) {
		this.buyPrice = buyPrice;
	}

	public void setCoords(String lat, String lon) {
		this.latitude = lat;
		this.longitude = lon;
	}

	public void setSeller(String seller) {
		this.sellerID = seller;
	}

	public String toString() {
		String buy = (buyPrice != null) ? String.valueOf(buyPrice) : "\\N";
		String lat = (latitude != null) ? latitude : "\\N";
		String lon = (longitude != null) ? longitude : "\\N";
		return itemID + "|*|" + name + "|*|" + description + "|*|" + sellerID + "|*|" +  
		started + "|*|" + ends + "|*|" + currently + "|*|" + buy + "|*|" + firstBid + "|*|" +
                numBids + "|*|" + location + "|*|" + country + "|*|"+ lat + "|*|" + lon;
	}
}

class User {
	String userID;
	int rating;

	public User(String id) {
		this.userID = id;
	}

	public void setRating (int rate) {
		this.rating = rate;
	}

	public String toString() {
		return userID + "|*|" + rating;
	}
}

class Bidder extends User {
	String location;
	String country;

	public Bidder(String id) {
		super(id);
	}

	public void setLoc(String location, String country) {
		this.location = location;
        	this.country = country;
	}

	public String toString() {
		return super.toString() + "|*|" + location + "|*|" + country;
	}
}

class ItemCategory {
	String itemID;
	Set<String> categories;

	public ItemCategory(String id) {
		this.itemID = id;
		categories = new HashSet<String>();
	}

	public void addCategory (String cat) {
		categories.add(cat);
	}

	public String toString() {
		String out = "";
		for (String cat : categories) {
			out += itemID + "|*|" + cat + "\n";
		}
		return out;
	}
}

class Bid {
	String itemID;
	String time;
	String amount;
	String bidderID;

	public Bid(String itemID, String time, String amount, String bidderId) {
		this.itemID = itemID;
		this.time = time;
		this.amount = amount;
		this.bidderID = bidderID;
	}

	public String toString() {
		return itemID + "|*|" + bidderID + "|*|" + time + "|*|" + amount + "\n";
	}
}

class MyParser {
    
    static final String columnSeparator = "|*|";
    static DocumentBuilder builder;

	static Map<String, Bidder> bidderMap = new HashMap<String, Bidder>();
	static Map<String, User> sellerMap = new HashMap<String, User>();
	static int maxDesc = 4000;
	static ArrayList<Bid> bidList = new ArrayList<Bid>();
	
    
    static final String[] typeName = {
	"none",
	"Element",
	"Attr",
	"Text",
	"CDATA",
	"EntityRef",
	"Entity",
	"ProcInstr",
	"Comment",
	"Document",
	"DocType",
	"DocFragment",
	"Notation",
    };
    
    static class MyErrorHandler implements ErrorHandler {
        
        public void warning(SAXParseException exception)
        throws SAXException {
            fatalError(exception);
        }
        
        public void error(SAXParseException exception)
        throws SAXException {
            fatalError(exception);
        }
        
        public void fatalError(SAXParseException exception)
        throws SAXException {
            exception.printStackTrace();
            System.out.println("There should be no errors " +
                               "in the supplied XML files.");
            System.exit(3);
        }
        
    }
    
    /* Non-recursive (NR) version of Node.getElementsByTagName(...)
     */
    static Element[] getElementsByTagNameNR(Element e, String tagName) {
        Vector< Element > elements = new Vector< Element >();
        Node child = e.getFirstChild();
        while (child != null) {
            if (child instanceof Element && child.getNodeName().equals(tagName))
            {
                elements.add( (Element)child );
            }
            child = child.getNextSibling();
        }
        Element[] result = new Element[elements.size()];
        elements.copyInto(result);
        return result;
    }
    
    /* Returns the first subelement of e matching the given tagName, or
     * null if one does not exist. NR means Non-Recursive.
     */
    static Element getElementByTagNameNR(Element e, String tagName) {
        Node child = e.getFirstChild();
        while (child != null) {
            if (child instanceof Element && child.getNodeName().equals(tagName))
                return (Element) child;
            child = child.getNextSibling();
        }
        return null;
    }
    
    /* Returns the text associated with the given element (which must have
     * type #PCDATA) as child, or "" if it contains no text.
     */
    static String getElementText(Element e) {
        if (e.getChildNodes().getLength() == 1) {
            Text elementText = (Text) e.getFirstChild();
            return elementText.getNodeValue();
        }
        else
            return "";
    }
    
    /* Returns the text (#PCDATA) associated with the first subelement X
     * of e with the given tagName. If no such X exists or X contains no
     * text, "" is returned. NR means Non-Recursive.
     */
    static String getElementTextByTagNameNR(Element e, String tagName) {
        Element elem = getElementByTagNameNR(e, tagName);
        if (elem != null)
            return getElementText(elem);
        else
            return "";
    }
    
    /* Returns the amount (in XXXXX.xx format) denoted by a money-string
     * like $3,453.23. Returns the input if the input is an empty string.
     */
    static String strip(String money) {
        if (money.equals(""))
            return money;
        else {
            double am = 0.0;
            NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.US);
            try { am = nf.parse(money).doubleValue(); }
            catch (ParseException e) {
                System.out.println("This method should work for all " +
                                   "money values you find in our data.");
                System.exit(20);
            }
            nf.setGroupingUsed(false);
            return nf.format(am).substring(1);
        }
    }

	static String dateFormat(String input) {
		SimpleDateFormat parser = new SimpleDateFormat("MMM-dd-yy HH:mm:ss");
		SimpleDateFormat output = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String outDate = "";
		try {
			Date parsed = parser.parse(input);
			outDate = output.format(parsed);
		} catch (ParseException ex) {
			System.out.println("ERROR when parsing \"" + input + "\"\n");
		}
		return outDate;
	}

	// Write out the contents of a list to a file
	static void listToFile(String file, ArrayList<Object> list) {
		try {
			FileWriter w = new FileWriter(file, true);
			PrintWriter p = new PrintWriter(w);
			for (Object obj : list) {
				p.print(obj);
			}
			p.close();
			w.close();
		} catch (IOException ex) {
			System.out.println("ERROR when writing list to file\n");
		}
	}

	// Write out the contents of a map to a file
	static void mapToFile(String file, HashMap<String, Object> map) {
		try {
			FileWriter w = new FileWriter(file, true);
			PrintWriter p = new PrintWriter(w);
			for (Map.Entry<String, Object> thing : map.entrySet()) {
				p.print(thing.getValue());
				p.println();
			}
		p.close();
		w.close();
		} catch (IOException ex) {
			System.out.println("ERROR when writing map to file\n");
		}
	}
    
    /* Process one items-???.xml file.
     */
    static void processFile(File xmlFile) {
        Document doc = null;
        try {
            doc = builder.parse(xmlFile);
        }
        catch (IOException e) {
            e.printStackTrace();
            System.exit(3);
        }
        catch (SAXException e) {
            System.out.println("Parsing error on file " + xmlFile);
            System.out.println("  (not supposed to happen with supplied XML files)");
            e.printStackTrace();
            System.exit(3);
        }
        
        /* At this point 'doc' contains a DOM representation of an 'Items' XML
         * file. Use doc.getDocumentElement() to get the root Element. */
        System.out.println("Successfully parsed - " + xmlFile);
        
        /* Fill in code here (you will probably need to write auxiliary
            methods). */
        Element root = doc.getDocumentElement();

	Element[] items = getElementsByTagNameNR(root, "Item");
	ArrayList<ItemCategory> categoryList = new ArrayList<ItemCategory>();
	Map<String, Item> itemMap = new HashMap<String, Item>();

	for (Element item : items) {
		String itemID = item.getAttribute("ItemID");
		String name = getElementTextByTagNameNR(item, "Name");
		String buyPrice = strip(getElementTextByTagNameNR(item, "Buy_Price"));
		String firstBid = strip(getElementTextByTagNameNR(item, "First_Bid"));
		String currently = strip(getElementTextByTagNameNR(item, "Currently"));
		int numBids = Integer.parseInt(getElementTextByTagNameNR(item, "Number_of_Bids"));
		String description = getElementTextByTagNameNR(item, "Description");
		if (description.length() > maxDesc)
			description = description.substring(0, maxDesc); // truncate to 4000
		String started = dateFormat(getElementTextByTagNameNR(item, "Started"));
		String ends = dateFormat(getElementTextByTagNameNR(item, "Ends"));
		String location = getElementTextByTagNameNR(item, "Location");
		String itemCountry = getElementTextByTagNameNR(item, "Country");
		String sellerID = (getElementByTagNameNR(item, "Seller")).getAttribute("UserID");
		
		Element locElement = getElementByTagNameNR(item, "Location");
		String longitude = locElement.getAttribute("Longitude");
		String latitude = locElement.getAttribute("Latitude");

		Item newItem = new Item(itemID);
		newItem.setAttrs(name, firstBid, currently, started, ends, numBids, location, itemCountry, description);
		newItem.setCoords(latitude, longitude);
		newItem.setSeller(sellerID);
		newItem.setBuyPrice(buyPrice);

		Element[] categories = getElementsByTagNameNR(item, "Category");
		ItemCategory newCategory = new ItemCategory(itemID);
		for (Element cat : categories) {
			newCategory.addCategory(cat.getTextContent());
		}

		int sellerRating = Integer.parseInt((getElementByTagNameNR(item, "Seller")).getAttribute("Rating"));
		User newSeller = new User(sellerID);
		newSeller.setRating(sellerRating);

		Element rootBids = getElementByTagNameNR(item, "Bids");
		Element[] bids = getElementsByTagNameNR(rootBids, "Bid");
		Bid newBid;
		Bidder newBidder;
		
		for (Element b : bids) {
			Element bidder = getElementByTagNameNR(b, "Bidder");
			String bidderID = bidder.getAttribute("UserID");
			newBidder = new Bidder(bidderID);
			newBidder.setRating(Integer.parseInt(bidder.getAttribute("Rating")));
			String country = getElementTextByTagNameNR(bidder, "Country");
			String loc = getElementTextByTagNameNR(bidder, "Location");
			newBidder.setLoc(location, country);

			String amount = strip(getElementTextByTagNameNR(b, "Amount"));
			String time = dateFormat(getElementTextByTagNameNR(b, "Time"));
			newBid = new Bid(itemID, time, amount, bidderID);

			bidList.add(newBid);
			bidderMap.put(bidderID, newBidder);
		}
	
		categoryList.add(newCategory);
		itemMap.put(itemID, newItem);
		sellerMap.put(sellerID, newSeller);
	}

	ArrayList<Object> itemCatList = new ArrayList<Object>(categoryList);
	listToFile("itemCategory.dat", itemCatList);
	HashMap<String, Object> itemHash = new HashMap<String, Object>(itemMap);
	mapToFile("item.dat", itemHash);
	HashMap<String, Object> sellerHash = new HashMap<String, Object>(sellerMap);
	mapToFile("seller.dat", sellerHash);
	ArrayList<Object> bidOutput = new ArrayList<Object>(bidList);
	listToFile("bid.dat", bidOutput);
	HashMap<String, Object> bidderHash = new HashMap<String, Object>(bidderMap);
	mapToFile("bidder.dat", bidderHash);
        
        
        /**************************************************************/
        
    }
    
    public static void main (String[] args) {
        if (args.length == 0) {
            System.out.println("Usage: java MyParser [file] [file] ...");
            System.exit(1);
        }
        
        /* Initialize parser. */
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setValidating(false);
            factory.setIgnoringElementContentWhitespace(true);      
            builder = factory.newDocumentBuilder();
            builder.setErrorHandler(new MyErrorHandler());
        }
        catch (FactoryConfigurationError e) {
            System.out.println("unable to get a document builder factory");
            System.exit(2);
        } 
        catch (ParserConfigurationException e) {
            System.out.println("parser was unable to be configured");
            System.exit(2);
        }
        
        /* Process all files listed on command line. */
        for (int i = 0; i < args.length; i++) {
            File currentFile = new File(args[i]);
            processFile(currentFile);
        }
    }
}
