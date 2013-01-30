package fr.cavalier.netcheck.dao;

import org.w3c.dom.Element;

import fr.cavalier.netcheck.model.Account;
import fr.cavalier.netcheck.model.Check;
import fr.cavalier.netcheck.model.Customer;

/**
 * @author malika 
 * @date 27 janv. 2013
 * net-check
 * fr.cavalier.netcheck.dao CheckListParser.java 
 */
public class CheckListParser extends XmlParser {
	
	public CheckListParser() {
		super.input="checkList";
		super.output="checkList";
	}
	
	public CheckListParser(String inputFile, Customer customer) {
		this();
		super.input= inputFile;
		gestionnaire = new CheckListHandler(customer);
	}
	
	public void recordCheckListForUser(Customer customer) {
		
		Element client=doc.createElement("customer");
		//name
		Element name=doc.createElement("name");
		name.setTextContent(customer.getName());
		client.appendChild(name);
		//lastName
		Element lastName=doc.createElement("lastName");
		lastName.setTextContent(customer.getLastname());
		client.appendChild(lastName);
		//location
		Element location=doc.createElement("adress");
		location.setTextContent(customer.getLocation());
		client.appendChild(location);
		Account compte = customer.getAccount();
		//compte
		Element eCompte=doc.createElement("account");
		eCompte.setAttribute("Solde", ((Double)compte.getBalance()).toString());
		for(Check cheque:compte.getAvailableCheques()){
			//cheque
			Element id=doc.createElement("id");
			Element eCheck=doc.createElement("check");
			id.setTextContent(((Long)cheque.getId()).toString());
			eCheck.appendChild(id);
			
			Element currency=doc.createElement("currency");
			currency.setTextContent(cheque.getCurrency());
			eCheck.appendChild(currency);
			
			Element accountOwner=doc.createElement("accountOwner");
			accountOwner.setTextContent(cheque.getAccountOwner().toString());
			eCheck.appendChild(accountOwner);
			
			eCompte.appendChild(eCheck);
		}
		client.appendChild(eCompte);
	
		if (doc.getFirstChild() != null) {
			doc.removeChild(doc.getFirstChild());
		}
		doc.appendChild(client);
	}

}
