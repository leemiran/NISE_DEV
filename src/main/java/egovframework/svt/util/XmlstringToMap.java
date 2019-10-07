package egovframework.svt.util;

import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public class XmlstringToMap {
	public static Map<String, String> convert(String xmlString) {
		Map<String, String> values = new HashMap<String, String>();
        
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        Document doc = null;
        try {
        	builder = factory.newDocumentBuilder();
        	doc = builder.parse(new InputSource(new StringReader(
        			xmlString)));
        } catch (Exception e) {
        	e.printStackTrace();
        }
        
        Node user = doc.getFirstChild();
        NodeList childs = user.getChildNodes();
        Node child;
        for (int i = 0; i < childs.getLength(); i++) {
            child = childs.item(i);
            System.out.println(child.getNodeName());
            System.out.println(child.getTextContent());
            values.put(child.getNodeName(), child.getTextContent());
        }
        return values;
    }
}
