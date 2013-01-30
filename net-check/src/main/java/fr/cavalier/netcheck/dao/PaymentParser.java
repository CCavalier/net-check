package fr.cavalier.netcheck.dao;

import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import fr.cavalier.netcheck.model.Check;
import fr.cavalier.netcheck.util.DateFormatter;

/**
 * @author malika 
 * @date 27 janv. 2013
 * net-check
 * fr.cavalier.netcheck.dao PaymentParser.java 
 */
public class PaymentParser extends XmlParser {
	
	private CheckParser chequeParser;
	
	public PaymentParser(){
		super.input="paymentAsk";
		super.output="paymentAsk";
		super.gestionnaire = new PaymentHandler();
	}
	
	public PaymentParser(String input, String output) {
		this();
		super.input = input;
		super.output = output;
	}
	
	private void initializeFile() {
		Element root = doc.getDocumentElement();
		if (root == null) {
			root = doc.createElement("paymentChecks");
			doc.appendChild(root);
		} else if (!root.getNodeName().equals("paymentChecks")) {
			doc.removeChild(root);
			root = doc.createElement("paymentChecks");
			doc.appendChild(root);
		}
		root.setAttribute("xmlns","http://www.ccavalier.fr");
		root.setAttribute("xsi:schemaLocation", "http://www.ccavalier.fr payment.xsd");
		root.setAttribute("xmlns:xsi","http://www.w3.org/2001/XMLSchema-instance");
	}
	
	public void addNewPaymentCheck(Check cheque) {
		initializeFile();
		Node root = doc.getFirstChild();
		//cheque
		
		chequeParser=new CheckParser(cheque);
		chequeParser.initializeFromFile();
		chequeParser.checkGenerator();
		chequeParser.transform();
		
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
		
		//entreprise
		Node enterpriseNode = doc.createElement("entreprise");
		enterpriseNode.setTextContent(cheque.getCible().getName());
		checkNode.appendChild(enterpriseNode);
		
		Node customerNode = doc.createElement("customer");
		Node customerNameNode = doc.createElement("name");
		customerNameNode.setTextContent(cheque.getUser().getName());
		customerNode.appendChild(customerNameNode);
		Node customerLastnameNode = doc.createElement("lastname");
		customerLastnameNode.setTextContent(cheque.getUser().getLastname());
		customerNode.appendChild(customerLastnameNode);
		checkNode.appendChild(customerNode);
		
		root.appendChild(checkNode);
	}
	
	public List<Check> parseAndGetCheckList() {
		this.parse();
		return ((PaymentHandler)super.gestionnaire).getReceivedChecks();
	}

}
