package fr.cavalier.netcheck.dao;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import fr.cavalier.netcheck.model.Check;
import fr.cavalier.netcheck.model.Customer;
import fr.cavalier.netcheck.model.Enterprise;
import fr.cavalier.netcheck.model.Order;
import fr.cavalier.netcheck.util.DateFormatter;

public class OrderHandler extends DefaultHandler {
	
	private Enterprise orderEntreprise;
	private List<Check> receivedChecks;
	private Check currentCheck;
	private String currentTag;
	
	private Order currentOrder;
	
	public OrderHandler() {
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
		
		if(qName.equals("check")){
			currentCheck = new Check();
			currentCheck.setCible(orderEntreprise);
		} else if (qName.equals("customer")) {
			currentCheck.setUser(new Customer());
		} else if (qName.equals("entreprise")) {
			orderEntreprise = new Enterprise();
		}
	}

	/**
	 * détection fin de balise
	 */
	public void endElement(String uri, String localName, String qName) throws SAXException {
		currentTag = "";
		if (qName.equals("check")){
			receivedChecks.add(currentCheck);
			currentCheck = null;
		}
	}

	/**
	 * détection de caractères
	 */
	public void characters(char[] ch, int start, int length)
			throws SAXException {
	
		String lecture = new String(ch, start, length);

		if (currentTag.equals("currency")) {
			currentCheck.setCurrency(lecture);
		} else if (currentTag.equals("id")) {
			currentCheck.setId(Long.parseLong(lecture.trim()));
		} else if (currentTag.equals("accountOwner")) {
			currentCheck.setAccountOwner(Long.parseLong(lecture.trim()));
		} else if (currentTag.equals("date")) {
			try {
				currentCheck.setDate(DateFormatter.convertStringToDate(lecture.trim()));
			} catch (ParseException e) {
				currentCheck.setDate(null);
			}
		} else if (currentTag.equals("value")) {
			if (lecture.trim().length() > 0) {
				currentCheck.setValue(Double.parseDouble(lecture.trim()));
			} else {
				currentCheck = null;
			}
		} else if (currentTag.equals("name")) {
			currentCheck.getUser().setName(lecture);
		} else if (currentTag.equals("lastname")) {
			currentCheck.getUser().setLastname(lecture);
		} else if (currentTag.equals("entreprise")) {
			orderEntreprise.setName(lecture);
		} else if (currentTag.equals("item")) {
			currentOrder.getItems().add(lecture);
		}
	}

	/**
	 * début du parsing
	 */
	public void startDocument() throws SAXException {
		orderEntreprise = null;
		receivedChecks = new ArrayList<Check>();
		currentCheck = null;
		currentOrder = new Order();
	}
	
	public void endDocument() throws SAXException {
		currentOrder.setPaymentsChecks(receivedChecks);
		currentOrder.setEntreprise(orderEntreprise);
	}

	public Order getCurrentOrder() {
		return currentOrder;
	}

	public void setCurrentOrder(Order currentOrder) {
		this.currentOrder = currentOrder;
	}

}
