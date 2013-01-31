package fr.cavalier.netcheck.dao;

import java.util.HashMap;
import java.util.Map.Entry;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import fr.cavalier.netcheck.model.Customer;

/**
 * <p>Classe de traitement xml pour le client</p>
 * @author malika 
 * @date 26 janv. 2013
 * net-check
 * fr.cavalier.netcheck.dao CustomerParser.java 
 */
public class CustomerParser extends XmlParser {
	
	public CustomerParser(){
		super();
	}
	
	private void initializeRootNode() {
		Node root = doc.getFirstChild();
		if (root == null) {
			root = doc.createElement("askList");
			doc.appendChild(root);
		} else if (!root.getNodeName().equals("askList")) {
			doc.removeChild(root);
			root = doc.createElement("askList");
			doc.appendChild(root);
		}
	}
	

	/**
	 * <p>Cree la demande de compte dans le fichier en charge des nouveaux utilisateurs</p>
	 * @param c
	 * @param cheque
	 * @param balance
	 */
	public void askNewAccount(Customer c, HashMap<String,Integer> cheque, double balance){
		initializeRootNode();
		NodeList nl = doc.getElementsByTagName("askList");
		Node root = nl.item(0);
		
		//creation demande
		Element ask = doc.createElement("ask");
		Element customer=doc.createElement("customer");
		//prenom
		Element name=doc.createElement("name");
		name.setTextContent(c.getName());
		customer.appendChild(name);
		
		//nom
		Element lastName=doc.createElement("lastName");
		lastName.setTextContent(c.getLastname());
		customer.appendChild(lastName);
		
		//adresse
		Element adress= doc.createElement("adress");
		adress.setTextContent(c.getLocation());
		customer.appendChild(adress);
		
		//client
		ask.appendChild(customer);
		
		//demandes de cheque
		Element account=doc.createElement("account");
		account.setAttribute("value",((Double)balance).toString());
		for(Entry<String, Integer> check: cheque.entrySet()){
			Element checkItem=doc.createElement("check");
			//curreny
			Element currency=doc.createElement("currency");
			currency.setTextContent(check.getKey());
			checkItem.appendChild(currency);
			//nombre
			Element number=doc.createElement("number");
			number.setTextContent(((Integer)check.getValue()).toString());
			checkItem.appendChild(number);
			//ajout cheque
			account.appendChild(checkItem);
		}
		ask.appendChild(account);
		
		root.appendChild(ask);
		doc.replaceChild(root, root);
	}
}
