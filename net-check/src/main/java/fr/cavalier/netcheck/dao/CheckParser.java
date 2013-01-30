package fr.cavalier.netcheck.dao;

import org.w3c.dom.Node;

import fr.cavalier.netcheck.model.Check;
import fr.cavalier.netcheck.util.DateFormatter;

/**
 * @author malika 
 * @date 28 janv. 2013
 * net-check
 * fr.cavalier.netcheck.dao CheckParser.java 
 */
public class CheckParser extends XmlParser {

	private Check cheque;
	
	public CheckParser(Check cheque){
		this.cheque=cheque;
		super.input=cheque.getUser().getName()+cheque.getId().toString()+"cheque";
		super.output=cheque.getUser().getName()+cheque.getId().toString()+"cheque";
		super.gestionnaire = null;
	}
	
	private void initializeFile() {
		Node root = doc.getFirstChild();
		if (root == null) {
			root = doc.createElement(output);
			doc.appendChild(root);
		} else if (!root.getNodeName().equals(output)) {
			doc.removeChild(root);
			root = doc.createElement(output);
			doc.appendChild(root);
		}
	}
	
	public void checkGenerator() {
		Node root = doc.createElement("commandCheck");
		//cheque
		Node checkNode = doc.createElement("check");
		//id
		Node checkIdNode = doc.createElement("id");
		checkIdNode.setTextContent(cheque.getId().toString());
		checkNode.appendChild(checkIdNode);
		//devises
		Node currencyNode = doc.createElement("currency");
		currencyNode.setTextContent(cheque.getCurrency());
		checkNode.appendChild(currencyNode);
		//valeur
		Node valueNode = doc.createElement("value");
		valueNode.setTextContent(((Double)cheque.getValue()).toString());
		checkNode.appendChild(valueNode);
		//destinataire
		Node dest = doc.createElement("dest");
		dest.setTextContent(cheque.getCible().getName());
		checkNode.appendChild(dest);
		//date
		Node dateNode = doc.createElement("date");
		dateNode.setTextContent(DateFormatter.convertDateToString(cheque.getDate()));
		checkNode.appendChild(dateNode);
		//gestionnaire
		Node accountOwnerNode = doc.createElement("accountOwner");
		accountOwnerNode.setTextContent(cheque.getAccountOwner().toString());
		checkNode.appendChild(accountOwnerNode);
		//client
		Node customer=doc.createElement("customer");
		//nom client
		Node nameCustomer=doc.createElement("name");
		nameCustomer.setTextContent(cheque.getUser().getName());
		customer.appendChild(nameCustomer);
		//prenom client
		Node lastNameCustomer=doc.createElement("lastName");
		lastNameCustomer.setTextContent(cheque.getUser().getLastname());
		customer.appendChild(lastNameCustomer);
		
		root.appendChild(customer);
		root.appendChild(checkNode);
		
		if (doc.getFirstChild() != null) {
			doc.removeChild(doc.getFirstChild());
		}
		doc.appendChild(root);
	}
}
