package fr.cavalier.netcheck.dao;

import java.util.List;

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
	
	public PaymentParser(){
		super.input="paymentAsk";
		super.output="paymentAsk";
		super.gestionnaire = new PaymentHandler();
	}
	
	private void initializeFile() {
		Node root = doc.getFirstChild();
		if (root == null) {
			root = doc.createElement("paymentChecks");
			doc.appendChild(root);
		} else if (!root.getNodeName().equals("paymentChecks")) {
			doc.removeChild(root);
			root = doc.createElement("paymentChecks");
			doc.appendChild(root);
		}
	}
	
	public void addNewPaymentCheck(Check cheque) {
		initializeFile();
		Node root = doc.getFirstChild();
		Node checkNode = doc.createElement("check");
		
		Node checkIdNode = doc.createElement("id");
		checkIdNode.setTextContent(cheque.getId().toString());
		checkNode.appendChild(checkIdNode);
		Node currencyNode = doc.createElement("currency");
		currencyNode.setTextContent(cheque.getCurrency());
		checkNode.appendChild(currencyNode);
		Node valueNode = doc.createElement("value");
		valueNode.setTextContent(((Double)cheque.getValue()).toString());
		checkNode.appendChild(valueNode);
		Node dateNode = doc.createElement("date");
		dateNode.setTextContent(DateFormatter.convertDateToString(cheque.getDate()));
		checkNode.appendChild(dateNode);
		Node accountOwnerNode = doc.createElement("accountOwner");
		accountOwnerNode.setTextContent(cheque.getAccountOwner().toString());
		checkNode.appendChild(accountOwnerNode);
		
		root.appendChild(checkNode);
	}
	
	public List<Check> initSaxAndGetCheckList() {
		this.saxInit();
		return ((PaymentHandler)super.gestionnaire).getReceivedChecks();
	}

}
