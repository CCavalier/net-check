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
import fr.cavalier.netcheck.util.DateFormatter;

/**
 * @author M Sitruk 
 * @date 27 janv. 2013
 * net-check
 * fr.cavalier.netcheck.dao PaymentHandler.java 
 */
public class PaymentHandler extends DefaultHandler {
	
	private List<Check> receivedChecks;
	private Check currentCheck;
	private String currentTag;
	
	public PaymentHandler() {
		receivedChecks = new ArrayList<Check>();
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
		} else if (qName.equals("customer")) {
			currentCheck.setUser(new Customer());
		} else if (qName.equals("entreprise")) {
			currentCheck.setCible(new Enterprise());
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
			currentCheck.getCible().setName(lecture);
		}
	}

	/**
	 * début du parsing
	 */
	public void startDocument() throws SAXException {
		receivedChecks = new ArrayList<Check>();
	}

	public List<Check> getReceivedChecks() {
		return receivedChecks;
	}

	public void setReceivedChecks(List<Check> receivedChecks) {
		this.receivedChecks = receivedChecks;
	}

}
