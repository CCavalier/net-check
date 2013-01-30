package fr.cavalier.netcheck.dao;

import java.text.ParseException;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import fr.cavalier.netcheck.model.Account;
import fr.cavalier.netcheck.model.Check;
import fr.cavalier.netcheck.model.Customer;
import fr.cavalier.netcheck.model.Enterprise;
import fr.cavalier.netcheck.model.Manager;
import fr.cavalier.netcheck.util.DateFormatter;

/**
 * @author malika 
 * @date 29 janv. 2013
 * net-check
 * fr.cavalier.netcheck.dao ManagerHandler.java 
 */
public class ManagerHandler extends DefaultHandler {
	
	private String currentTag;
	private Manager manager;
	private Customer currentCustomer;
	private Enterprise currentEnterprise;
	private Account currentAccount;
	private Check currentCheck;
	private boolean isCustomerMode;
	private boolean isEnterpriseMode;
	
	public ManagerHandler() {
		isCustomerMode = false;
		isEnterpriseMode = false;
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
		
		if(qName.equals("customers")){
			isCustomerMode = true;
		} else if(qName.equals("enterprises")){
			isEnterpriseMode = true;
		} else if(qName.equals("customer")) {
			currentCustomer = new Customer();
		} else if (qName.equals("enterprise")) {
			currentEnterprise = new Enterprise();
		} else if (qName.equals("account")) {
			currentAccount = new Account();
			currentAccount.setBalance(Double.parseDouble(attributes.getValue(0)));	
		} else if (qName.equals("check")) {
			currentCheck = new Check();
		}
	}

	/**
	 * détection fin de balise
	 */
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		currentTag = "";

		if(qName.equals("customers")){
			isCustomerMode = false;
		} else if(qName.equals("enterprises")){
			isEnterpriseMode = false;
		} else if(qName.equals("customer")) {
			manager.getUsers().add(currentCustomer);
		} else if (qName.equals("enterprise")) {
			manager.getEnterprises().add(currentEnterprise);
		} else if (qName.equals("account")) {
			if (isCustomerMode) {
				currentCustomer.setAccount(currentAccount);
			}
			if (isEnterpriseMode) {
				currentEnterprise.setAccount(currentAccount);
			}
		} else if (qName.equals("check")) {
			if (currentCheck.getValue() != null && currentCheck.getDate() != null) {
				currentAccount.getUsedCheques().add(currentCheck);
			} else {
				currentAccount.getAvailableCheques().add(currentCheck);
			}
		}
	}

	/**
	 * détection de caractères
	 */
	public void characters(char[] ch, int start, int length)
			throws SAXException {
	
		String lecture = new String(ch, start, length);

		if (currentTag.equals("name")) {
			if (isCustomerMode) {
				currentCustomer.setName(lecture);
			} else if (isEnterpriseMode) {
				currentEnterprise.setName(lecture);
			}
		} else if (currentTag.equals("location")) {
			if (isCustomerMode) {
				currentCustomer.setLocation(lecture);
			} else if (isEnterpriseMode) {
				currentEnterprise.setLocation(lecture);
			}
		} else if (currentTag.equals("lastName")) {
			currentCustomer.setLastname(lecture);
		} else if (currentTag.equals("id")) {
			currentCheck.setId(Long.parseLong(lecture));
		} else if (currentTag.equals("currency")) {
			currentCheck.setCurrency(lecture);
		} else if (currentTag.equals("identifiant")) {
			manager.setIdentifiant(Long.parseLong(lecture));
		} else if (currentTag.equals("value")) {
			currentCheck.setValue(Double.parseDouble(lecture));
		} else if (currentTag.equals("date")) {
			try {
				currentCheck.setDate(DateFormatter.convertStringToDate(lecture));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * début du parsing
	 */
	public void startDocument() throws SAXException {
		manager = new Manager();
	}

	public Manager getManager() {
		return manager;
	}

	public void setManager(Manager manager) {
		this.manager = manager;
	}

}
