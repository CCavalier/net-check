package fr.cavalier.netcheck.dao;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import fr.cavalier.netcheck.model.Account;
import fr.cavalier.netcheck.model.Check;
import fr.cavalier.netcheck.model.Customer;

/**
 * @author malika 
 * @date 27 janv. 2013
 * net-check
 * fr.cavalier.netcheck.dao CheckListHandler.java 
 */
public class CheckListHandler extends DefaultHandler {

	private Customer customer;
	private Check currentCheck;
	private String currentTag;
	
	public CheckListHandler(Customer customer) {
		this.customer = customer;
	}
	
	public Customer getCustomer() {
		return customer;
	}


	public void setCustomer(Customer customer) {
		this.customer = customer;
	}
	
	/**
	 * détection d'ouverture de balise
	 * @param uri l'uri de la balise
	 * @param localName  le nom local de la balise
	 * @param qName le nom de la balise (exemple: "div","h1" etc...)
	 * @param attributes  la liste des attributs de la balise
	 */
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		
		currentTag = qName;
		
		if(qName.equals("account")){
			Account account = new Account();
			account.setBalance(Double.parseDouble(attributes.getValue(0)));	
			getCustomer().setAccount(account);
		} else if(qName.equals("check")){
			currentCheck = new Check();
		}
	}

	/**
	 * détection fin de balise
	 */
	public void endElement(String uri, String localName, String qName) throws SAXException {
		currentTag = "";
		if (qName.equals("check")){
			getCustomer().getAccount().getAvailableCheques().add(currentCheck);
			currentCheck.setUser(getCustomer());
			currentCheck = null;
		}
	}

	/**
	 * détection de caractères
	 */
	public void characters(char[] ch, int start, int length)
			throws SAXException {
	
		String lecture = new String(ch, start, length);

		if (currentTag.equals("name")) {
			getCustomer().setName(lecture);
		} else if (currentTag.equals("lastName")) {
			getCustomer().setLastname(lecture);
		} else if (currentTag.equals("adress")) {
			getCustomer().setLocation(lecture);
		} else if (currentTag.equals("currency")) {
			currentCheck.setCurrency(lecture);
		} else if (currentTag.equals("id")) {
			currentCheck.setId(Long.parseLong(lecture.trim()));
		} else if (currentTag.equals("accountOwner")) {
			currentCheck.setAccountOwner(Long.parseLong(lecture.trim()));
		}
	}
	
}
