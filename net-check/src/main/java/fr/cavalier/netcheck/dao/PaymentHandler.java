package fr.cavalier.netcheck.dao;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import fr.cavalier.netcheck.model.Check;
import fr.cavalier.netcheck.util.DateFormatter;

/**
 * @author malika 
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
		
		System.out.println("Start:"+qName);
		currentTag = qName;
		
		if(qName.equals("check")){
			currentCheck = new Check();
		}
	}

	/**
	 * détection fin de balise
	 */
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		System.out.println("End:"+qName);
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
		System.out.println("char:"+lecture);

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
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if (currentTag.equals("value")) {
			currentCheck.setValue(Double.parseDouble(lecture.trim()));
		}
	}

	/**
	 * début du parsing
	 */
	public void startDocument() throws SAXException {
	}

	/**
	 * fin du parsing
	 */
	public void endDocument() throws SAXException {
		System.out.println("End parsing");
	}

	public List<Check> getReceivedChecks() {
		return receivedChecks;
	}

	public void setReceivedChecks(List<Check> receivedChecks) {
		this.receivedChecks = receivedChecks;
	}

}
