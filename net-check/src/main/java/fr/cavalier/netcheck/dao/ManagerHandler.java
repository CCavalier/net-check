package fr.cavalier.netcheck.dao;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import fr.cavalier.netcheck.model.Account;
import fr.cavalier.netcheck.model.Check;
import fr.cavalier.netcheck.model.Customer;
import fr.cavalier.netcheck.model.Manager;

/**
 * @author C Cavalier
 * @date 26 janv. 2013 net-check 
 * fr.cavalier.netcheck.dao 
 * ManagerHandler.java
 */
public class ManagerHandler extends DefaultHandler {

	private Manager manager;
	private ManagerParserContext context;
	private String currentTag;

	public ManagerHandler(Manager manager) {
		this.context = new ManagerParserContext();
		this.manager = manager;
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
		
		if(qName.equals("customer")){
			Customer customer=new Customer();
			context.addCustomer(customer);
		} else if(qName.equals("account")){
			Account account = new Account();
			account.setBalance(Double.parseDouble(attributes.getValue(0)));	
			context.addAccount(account);
		} else if(qName.equals("check")){
			Check cheque=new Check();
			context.addCheck(cheque);
		}
	}

	/**
	 * détection fin de balise
	 */
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		System.out.println("End:"+qName);
		currentTag = "";
		if (qName.equals("customer")){
			context.finishCustomer();
		} else if (qName.equals("acount")){
			context.finishAccount();
		} else if (qName.equals("check")){
			context.finishCheck();
		} else if (qName.equals("ask")){
			context.finishAsk();
		}
	}

	/**
	 * détection de caractères
	 */
	public void characters(char[] ch, int start, int length)
			throws SAXException {
	
		String lecture = new String(ch, start, length);
		System.out.println("char:"+lecture);

		if (currentTag.equals("name")) {
			context.getCurrentCustomer().setName(lecture);
		} else if (currentTag.equals("lastName")) {
			context.getCurrentCustomer().setLastname(lecture);
		} else if (currentTag.equals("adress")) {
			context.getCurrentCustomer().setLocation(lecture);
		} else if (currentTag.equals("currency")) {
			context.getCurrentCheck().setCurrency(lecture);
		} else if (currentTag.equals("number")) {
			Integer value = Integer.parseInt(lecture.trim());
			context.setValueForCheck(value);
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
//		for(Customer c: context.getCustomerList()){
//			System.out.println("-> "+c.toString());
//		}
//		for(Account a: context.getAllAccounts()){
//			System.out.println("-> "+a.getUser().toString()+" : "+a.getBalance());
//		}
//		for (Check c : context.getAllChecks()) {
//			System.out.println("-> "+c.getUser().toString()+" : " + c.getId() + " / " + c.getCurrency());
//		}
		getManager().insertAsks(context.getCustomerList());
	}

	public ManagerParserContext getContext() {
		return context;
	}

	public void setContext(ManagerParserContext context) {
		this.context = context;
	}

	public Manager getManager() {
		return manager;
	}

	public void setManager(Manager manager) {
		this.manager = manager;
	}

}