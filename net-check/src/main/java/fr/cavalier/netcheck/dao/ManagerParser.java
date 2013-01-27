package fr.cavalier.netcheck.dao;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import fr.cavalier.netcheck.model.Account;
import fr.cavalier.netcheck.model.Check;
import fr.cavalier.netcheck.model.Customer;
import fr.cavalier.netcheck.model.Manager;


/**
 * @author malika 
 * @date 26 janv. 2013
 * net-check
 * fr.cavalier.netcheck.dao ManagerParser.java 
 */
public class ManagerParser extends XmlParser {

	public ManagerParser(Manager manager){
		super.input="accountAsk";
		super.output="accountAsk";
		super.gestionnaire =new ManagerHandler(manager);
	}
	
	public void createChecksList(Manager manager){
		super.output="checkList";
		Account compte;
		
		Node root = doc.createElement("manager");	
		for(Customer c:manager.getUsers()){
			Element client=doc.createElement("customer");
			//name
			Element name=doc.createElement("name");
			name.setTextContent(c.getName());
			client.appendChild(name);
			//lastName
			Element lastName=doc.createElement("lastName");
			lastName.setTextContent(c.getLastname());
			client.appendChild(lastName);
			//location
			Element location=doc.createElement("location");
			location.setTextContent(c.getLocation());
			client.appendChild(location);
			compte=c.getAccount();
			//compte
			Element eCompte=doc.createElement("account");
			eCompte.setAttribute("Solde", ((Double)compte.getBalance()).toString());
			for(Check cheque:compte.getCheques()){
				//cheque
				Element id=doc.createElement("id");
				Element eCheck=doc.createElement("check");
				id.setTextContent(((Long)cheque.getId()).toString());
				eCheck.appendChild(id);
				
				Element currency=doc.createElement("currency");
				currency.setTextContent(cheque.getCurrency());
				eCheck.appendChild(currency);
				
				eCompte.appendChild(eCheck);
			}
			client.appendChild(eCompte);
			root.appendChild(client);
		}
		
		doc.removeChild(doc.getFirstChild());
		doc.appendChild(root);
	}
	
}
