
package fr.cavalier.netcheck.view;

import java.util.HashMap;
import java.util.List;

import fr.cavalier.netcheck.dao.AccountAskParser;
import fr.cavalier.netcheck.dao.CheckListParser;
import fr.cavalier.netcheck.dao.CustomerParser;
import fr.cavalier.netcheck.dao.ManagerParser;
import fr.cavalier.netcheck.dao.PaymentParser;
import fr.cavalier.netcheck.model.Check;
import fr.cavalier.netcheck.model.Customer;
import fr.cavalier.netcheck.model.Enterprise;
import fr.cavalier.netcheck.model.Manager;

/*
import javax.xml.xpath.*;
import javax.xml.parsers.*;
*/
public class Main {
	
	public static void registerCustomersAndAskChecks() {
		Customer clientOne = new Customer("Cavalier", "Charlotte", "charlotte.cavalier@perpignan.com") ;
		HashMap<String,Integer> chequesDemandes= new HashMap<String,Integer>();
		chequesDemandes.put("EUR", 10);
		chequesDemandes.put("USD", 3);
		
		
		Customer clientTwo = new Customer("Teychene", "Francois", "fte@usap.com") ;
		HashMap<String,Integer> chequesDemandes2= new HashMap<String,Integer>();
		chequesDemandes2.put("EUR", 10);
		chequesDemandes2.put("JPY", 1);
		
		CustomerParser parser=new CustomerParser();
		parser.parse();
		parser.askNewAccount(clientOne, chequesDemandes, 200.);
		parser.askNewAccount(clientTwo, chequesDemandes2, 200.);
		parser.transform();
	}
	
	public static void registerAskedChecksAndReply() {
		Manager manager = new Manager();
		manager.setIdentifiant(0L);
		
		// Récupération des demades
		AccountAskParser askp=new AccountAskParser(manager);
		askp.parse();
		askp.saxInit();
		
		CheckListParser chkp = new CheckListParser();
		chkp.parse();
		for (Customer customer : manager.getUsers()) {
			chkp.recordCheckListForUser(customer);
			chkp.transform();
		}
		
		// Enregistrement de la listes de cheques par utilisateurs
		ManagerParser mp = new ManagerParser();
		mp.parse();
		mp.recordManager(manager);
		mp.transform();
	}
	
	public static void retrieveCheckReplyAndTryToPay() {
		Customer client = new Customer();
		
		CheckListParser parser = new CheckListParser("checkList.Cavalier-Charlotte", client);
		parser.parse();
		parser.saxInit();
		
		PaymentParser pp = new PaymentParser();
		pp.parse();
		pp.addNewPaymentCheck(client.paiement("EUR", 100.0));
		pp.addNewPaymentCheck(client.paiement("EUR", 37.5));
		pp.transform();
	}
	
	public static void enterpriseReceiveChecksForPayment() {
		Enterprise entreprise = new Enterprise();
		entreprise.setName("USAP");
		entreprise.setLocation("Perpignan");
		
		PaymentParser pp = new PaymentParser();
		pp.parse();
		List<Check> waitingCheck = pp.initSaxAndGetCheckList();
		entreprise.setWaitingForTreatmentChecks(waitingCheck);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// Part 1
		registerCustomersAndAskChecks();
		registerAskedChecksAndReply();
		retrieveCheckReplyAndTryToPay();
		enterpriseReceiveChecksForPayment();
	}

}
