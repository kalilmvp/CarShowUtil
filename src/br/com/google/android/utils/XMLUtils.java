package br.com.google.android.utils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import android.util.Log;

public class XMLUtils {
	
	private static final String TAG = "XMLUtils";
	
	public static Element getRoot(String xml, String charset) {
		InputStream in = null;
		DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
		
		byte[] bytes;
		try {
			bytes = xml.getBytes(charset);
			in = new ByteArrayInputStream(bytes);
			
			DocumentBuilder documentBuilder = builderFactory.newDocumentBuilder();
			Document dom = documentBuilder.parse(in);
			Element root = dom.getDocumentElement();
			
			if (root == null) {
				throw new RuntimeException("Invalid XML");
			}
			return root;
		} catch (UnsupportedEncodingException e) {
			Log.e(TAG, e.getMessage(), e);
		} catch (ParserConfigurationException e) {
			Log.e(TAG, e.getMessage(), e);
		} catch (SAXException e) {
			Log.e(TAG, e.getMessage(), e);
		} catch (IOException e) {
			Log.e(TAG, e.getMessage(), e);
		}
		return null;
	}
	
	public static String getText(Node node, String tag) {
		Node child = getChild(node, tag);
		if (child != null) {
			Node text = child.getFirstChild();
			if (text != null && !"".equals(text)) {
				String value = text.getNodeValue();
				return value.trim();
			}
		}
		return null;
	}
	
	public static List<Node> getChildren(Node node, String name) {
		List<Node> children = new ArrayList<Node>();
		NodeList nodes = node.getChildNodes();
		
		if (nodes != null && nodes.getLength() >= 1) {
			for (int i = 0; i < nodes.getLength(); i++) {
				Node n = nodes.item(i);
				if (name.equals(n.getNodeName())) {
					children.add(n);
				}
			}
		}
		return children;
	}

	private static Node getChild(Node node, String tag) {
		if (node == null) {
			return null;
		}
		
		NodeList childNodes = node.getChildNodes();
		if (childNodes == null) {
			return null;
		}
		
		for (int i = 0; i < childNodes.getLength(); i++) {		
			Node item = childNodes.item(i);
			if (item != null) {
				String name = item.getNodeName();
				if (tag.equalsIgnoreCase(name)) {
					return item;
				}
			}
		}
		return null;
	}
}
