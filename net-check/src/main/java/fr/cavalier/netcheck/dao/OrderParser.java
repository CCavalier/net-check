package fr.cavalier.netcheck.dao;

import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import fr.cavalier.netcheck.model.Check;
import fr.cavalier.netcheck.model.Order;
import fr.cavalier.netcheck.util.DateFormatter;

public class OrderParser extends XmlParser {
	
	public OrderParser() {
		super.gestionnaire = new OrderHandler();
	}
	
	public OrderParser(String input, String output) {
		this();
//		super.input = input;
//		super.output = output;
	}
	
	public Order getOrderFromFile() {
		return null;
	}
	
	public void createOrder(Order order) throws ParserConfigurationException {
		cleanDocument();
		
		Element root = doc.getDocumentElement();
		if (root == null) {
			root = doc.createElement("order");
			doc.appendChild(root);
		} else if (!root.getNodeName().equals("order")) {
			doc.removeChild(root);
			root = doc.createElement("order");
			doc.appendChild(root);
		}
	
		root.setAttribute("xmlns","http://www.ccavalier.fr");
		root.setAttribute("xsi:schemaLocation", "http://www.ccavalier.fr commande.xsd");
		root.setAttribute("xmlns:xsi","http://www.w3.org/2001/XMLSchema-instance");

		Node enterpriseNode = doc.createElement("entreprise");
		enterpriseNode.setTextContent(order.getEntreprise().getName());
		root.appendChild(enterpriseNode);
		
		Node itemsNode = doc.createElement("items");
		for (String item : order.getItems()) {
			Node itemNode = doc.createElement("item");
			itemNode.setTextContent(item);
			
			itemsNode.appendChild(itemNode);
		}
		root.appendChild(itemsNode);
		
		Node checksNode = doc.createElement("checks");
		
		for (Check cheque : order.getPaymentsChecks()) {
			Node checkNode = doc.createElement("check");
			//id
			Node checkIdNode = doc.createElement("id");
			checkIdNode.setTextContent(cheque.getId().toString());
			checkNode.appendChild(checkIdNode);
			//devise
			Node currencyNode = doc.createElement("currency");
			currencyNode.setTextContent(cheque.getCurrency());
			checkNode.appendChild(currencyNode);
			//valeur
			Node valueNode = doc.createElement("value");
			valueNode.setTextContent(((Double)cheque.getValue()).toString());
			checkNode.appendChild(valueNode);
			//date
			Node dateNode = doc.createElement("date");
			dateNode.setTextContent(DateFormatter.convertDateToString(cheque.getDate()));
			checkNode.appendChild(dateNode);
			//gestionnaire
			Node accountOwnerNode = doc.createElement("accountOwner");
			accountOwnerNode.setTextContent(cheque.getAccountOwner().toString());
			checkNode.appendChild(accountOwnerNode);
			
			Node customerNode = doc.createElement("customer");
			Node customerNameNode = doc.createElement("name");
			customerNameNode.setTextContent(cheque.getUser().getName());
			customerNode.appendChild(customerNameNode);
			Node customerLastnameNode = doc.createElement("lastname");
			customerLastnameNode.setTextContent(cheque.getUser().getLastname());
			customerNode.appendChild(customerLastnameNode);
			checkNode.appendChild(customerNode);
			
			checksNode.appendChild(checkNode);
		}
		
		root.appendChild(checksNode);
		
		if (doc.getFirstChild() != null) {
			doc.removeChild(doc.getFirstChild());
		}
		doc.appendChild(root);
	}
	
	public Order retrieveOrderFromFile(String fileName) {
		parse(fileName);
		return ((OrderHandler)gestionnaire).getCurrentOrder();
	}

}
