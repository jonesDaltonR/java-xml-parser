package com.company;

import org.jsoup.Jsoup;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import org.unbescape.html.HtmlEscape;


public class Main {

    public static void main(String[] args){

        DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
        try{
            //Create Document object and download xml as data into object from Kent District Library event listing
            DocumentBuilder domBuilder = domFactory.newDocumentBuilder();
            Document doc = domBuilder.parse(new URL("https://www.kdl.org/rss/event-listing").openStream());
            //Sends xml data to parser to return list of events
            List<Event> eventList = parseXML(doc);

            //Iterate through list of events to return to screen
            for (Event e:eventList)
            {
                System.out.println(e);
            }

        }catch (ParserConfigurationException | MalformedURLException |SAXException ex)
        {
            System.out.println(ex);
        }
        catch (IOException ex)
        {
            System.out.println(ex);
        }

    }

    public static List<Event> parseXML(Document xml)
    {
        //Set up list of events and a place holder event
        List<Event> events = new ArrayList<>();
        Event event = new Event();

        //Create list of nodes from xml data based on xml tag of "item"
        NodeList eventList = xml.getElementsByTagName("item");

        //Iterate through node list
        for(int i = 0;i<eventList.getLength();i++)
        {
            //Get first node
            Node n = eventList.item(i);
            //Checks if node is a element node ("item" node)
            if(n.getNodeType() == Node.ELEMENT_NODE)
            {
                //If so, gets node as an element
                Element e = (Element)n;
                //Get children of node
                NodeList childrenList = e.getChildNodes();
                //Goes through each child of node
                for(int j = 0;j<childrenList.getLength();j++)
                {
                    Node d = childrenList.item(j);
                    //If child is a title tag
                    if(d.getNodeName() == "title")
                    {
                        //Get string value of tag
                        String escapedText = d.getTextContent();
                        //Remove escaped html from string
                        String unescapedText = HtmlEscape.unescapeHtml(escapedText);
                        //Parse html from unescaped string and get the text content
                        event.setTitle(Jsoup.parse(unescapedText).text());
                    }
                    if(d.getNodeName() == "description")
                    {
                        //Get string value of tag
                        String escapedText = d.getTextContent();
                        //Remove escaped html from string
                        String unescapedText = HtmlEscape.unescapeHtml(escapedText);
                        //Parse html from unescaped string and get the text content
                        event.setTitle(Jsoup.parse(unescapedText).text());
                    }
                }
                //Add event to list
                events.add(event);
                //Resets event object
                event = new Event();
            }
        }
        //Returns list of events
        return events;
    }
}

