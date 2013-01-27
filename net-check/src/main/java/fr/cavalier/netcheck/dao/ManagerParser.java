package fr.cavalier.netcheck.dao;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import fr.cavalier.netcheck.model.Account;
import fr.cavalier.netcheck.model.Check;
import fr.cavalier.netcheck.model.Customer;
import fr.cavalier.netcheck.model.Enterprise;
import fr.cavalier.netcheck.model.Manager;

/**
 * @author malika 
 * @date 27 janv. 2013
 * net-check
 * fr.cavalier.netcheck.dao ManagerParser.java 
 */
public class ManagerParser extends XmlParser {

	public ManagerParser(){
		super();
		super.input="manager";
		super.output="manager";
	}
	
	public Manager getManagerForId(Long identifiant) {
		return null;
	}
	
	private void createCheckNode(Account compte, Node eCompte) {
		for(Check cheque:compte.getAvailableCheques()){
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
	}
	
	/**
	 * <p>Genere la liste de compte du manager</p>
	 * @param manager
	 */
	public void recordManager(Manager manager){
		Account compte;
		
		output = "manager."+manager.getIdentifiant();
		
		Node root = doc.createElement("manager");	
		
		Node idNode = doc.createElement("identifiant");
		idNode.setTextContent(manager.getIdentifiant().toString());
		root.appendChild(idNode);
		
		Node customersNode = doc.createElement("customers");
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
			
			createCheckNode(compte,eCompte);
			
			client.appendChild(eCompte);
			customersNode.appendChild(client);
		}
		root.appendChild(customersNode);
		
		Node enterprisesNode = doc.createElement("enterprises");
		for(Enterprise enterprise : manager.getEnterprises()) {
			Element client=doc.createElement("enterprise");
			//name
			Element name=doc.createElement("name");
			name.setTextContent(enterprise.getName());
			client.appendChild(name);
			//location
			Element location=doc.createElement("location");
			location.setTextContent(enterprise.getLocation());
			client.appendChild(location);
			compte=enterprise.getAccount();
			
			//compte
			Element eCompte=doc.createElement("account");
			eCompte.setAttribute("Solde", ((Double)compte.getBalance()).toString());
			createCheckNode(compte,eCompte);
			client.appendChild(enterprisesNode);
		}
		root.appendChild(enterprisesNode);
		
		if (doc.getFirstChild() != null) {
			doc.removeChild(doc.getFirstChild());
		}
		doc.appendChild(root);
	}
}
