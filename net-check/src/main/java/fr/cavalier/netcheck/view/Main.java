
package fr.cavalier.netcheck.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import fr.cavalier.netcheck.dao.AccountAskParser;
import fr.cavalier.netcheck.dao.CheckListParser;
import fr.cavalier.netcheck.dao.CustomerParser;
import fr.cavalier.netcheck.dao.ManagerParser;
import fr.cavalier.netcheck.dao.PaymentParser;
import fr.cavalier.netcheck.model.Account;
import fr.cavalier.netcheck.model.Check;
import fr.cavalier.netcheck.model.Customer;
import fr.cavalier.netcheck.model.Enterprise;
import fr.cavalier.netcheck.model.Manager;

/*
import javax.xml.xpath.*;
import javax.xml.parsers.*;
*/
public class Main {
	
	public static void initializeManagers() {
		
		ManagerParser managerParser = new ManagerParser();
		managerParser.initializeFromFile();
		
		
		Manager manager0 = new Manager();
		manager0.setIdentifiant(0L);
		
		Customer customer0 = new Customer("Cavalier", "Charlotte", "18 rue des peupliers");
		Account customer0Account = new Account();
		customer0Account.setBalance(300.0);
		customer0.setAccount(customer0Account);
		
		Customer customer1 = new Customer("Teychene", "Francois", "18 rue des peupliers");
		Account customer1Account = new Account();
		customer1Account.setBalance(100.0);
		customer1.setAccount(customer1Account);
		
		Enterprise e1= new Enterprise();
		e1.setName("E1");
		e1.setLocation("adresse");
		Account e1Account = new Account();
		e1Account.setBalance(8000.0);
		e1.setAccount(e1Account);
		
		manager0.getUsers().add(customer0);
		manager0.getUsers().add(customer1);
		manager0.getEnterprises().add(e1);
		
		managerParser.recordManager(manager0);
		managerParser.setOutput("manager0");
		managerParser.transform();
		managerParser.cleanDocument();
		
		Manager manager1 = new Manager();
		manager1.setIdentifiant(1L);
		
		Customer customer2 = new Customer("Marchal", "Pierre", "46 avenue des landes");
		Account customer2Account = new Account();
		customer2Account.setBalance(600);
		customer2.setAccount(customer2Account);
		
		Enterprise e2= new Enterprise();
		e2.setName("E2");
		e2.setLocation("adresse2");
		Account e2Account = new Account();
		e2Account.setBalance(8000.0);
		e2.setAccount(e2Account);
		
		manager1.getUsers().add(customer2);
		manager1.getEnterprises().add(e2);
		
		managerParser.recordManager(manager1);
		managerParser.setOutput("manager1");
		managerParser.transform();
	}
	
	public static void customerAskCheckToAccountOwner() {
		Customer clientOne = new Customer("Cavalier", "Charlotte", "18 rue des peupliers");
		HashMap<String,Integer> chequesDemandes= new HashMap<String,Integer>();
		chequesDemandes.put("EUR", 10);
		chequesDemandes.put("USD", 3);
		
		
		Customer clientTwo = new Customer("Teychene", "Francois", "18 rue des peupliers");
		HashMap<String,Integer> chequesDemandes2= new HashMap<String,Integer>();
		chequesDemandes2.put("EUR", 10);
		chequesDemandes2.put("JPY", 1);
		
		CustomerParser parser=new CustomerParser();
		parser.initializeFromFile();
		parser.askNewAccount(clientOne, chequesDemandes, 200.);
		parser.askNewAccount(clientTwo, chequesDemandes2, 200.);
		parser.transform();
	}
	
	public static void accountOwnerReceiveAskedCheckAndReply() {
		ManagerParser parser = new ManagerParser();
		parser.setInput("manager0");
		parser.initializeFromFile();
		
		Manager aManager = parser.retrieveManagerFromInput();
		
		// Récupération des demades
		AccountAskParser askp=new AccountAskParser(aManager);
		askp.initializeFromFile();
		askp.parse();
		
		CheckListParser chkp = new CheckListParser();
		chkp.initializeFromFile();
		for (Customer customer : aManager.getUsers()) {
			chkp.setOutput("chequier."+customer.getName()+"-"+customer.getLastname());
			chkp.recordCheckListForUser(customer);
			chkp.transform();
		}
		
		parser.setOutput("manager0");
		parser.recordManager(aManager);
		parser.transform();
	}
	
	public static void customerReceiveCheckReplyAndPay() {
		Customer client = new Customer();
		
		Enterprise e1= new Enterprise();
		e1.setName("E1");
		e1.setLocation("adresse");
		
		Enterprise e2= new Enterprise();
		e2.setName("E2");
		e2.setLocation("adresse2");
		
		CheckListParser parser = new CheckListParser("chequier.Cavalier-Charlotte", client);
		parser.initializeFromFile();
		parser.parse();
		
		PaymentParser pp = new PaymentParser();
		pp.initializeFromFile();
		pp.addNewPaymentCheck(client.paiement("EUR", 100.0, e1));
		pp.addNewPaymentCheck(client.paiement("EUR", 37.5, e1));
		pp.addNewPaymentCheck(client.paiement("USD", 34.8, e2));
		pp.setOutput("paiement");
		pp.transform();
	}
	
	public static void enterpriseReceiveChecksForPayment() {
		
		PaymentParser pp = new PaymentParser("paiement", "");
		pp.initializeFromFile();
		List<Check> receivedChecks = pp.parseAndGetCheckList();
		
		Enterprise entreprise = new Enterprise();
		entreprise.setName("E1");
		entreprise.setLocation("adresse");
		for(Check c:receivedChecks){
			if (c.getCible().equals(entreprise)) {
				entreprise.getWaitingForTreatmentChecks().add(c);
			}
		}
		
		Enterprise entreprise2 = new Enterprise();
		entreprise2.setName("E2");
		entreprise2.setLocation("adresse2");
		for(Check c:receivedChecks){
			if (c.getCible().equals(entreprise2)) {
				entreprise2.getWaitingForTreatmentChecks().add(c);
			}
		}
		
		// Vérification si j'accepte ou pas par entreprise
		
		pp = new PaymentParser();
		pp.initializeFromFile();
		for (Check c:entreprise.getWaitingForTreatmentChecks()) {
			pp.addNewPaymentCheck(c);
		}
		pp.setOutput("paiement.E1");
		pp.transform();
		
		pp.cleanDocument();
		for (Check c:entreprise2.getWaitingForTreatmentChecks()) {
			pp.addNewPaymentCheck(c);
		}
		pp.setOutput("paiement.E2");
		pp.transform();
	}
	
	public static void accountOwnerReceivePaymentsForAnEnterprise() {
		ManagerParser parser = new ManagerParser();
		parser.setInput("manager0");
		parser.initializeFromFile();
		
		Manager manager0 = parser.retrieveManagerFromInput();
		
		parser.setInput("manager1");
		parser.initializeFromFile();
		
		Manager manager1 = parser.retrieveManagerFromInput();
		
		Manager[] managers = new Manager[] {manager0, manager1 };
		
		List<Check> receivedChecks = new ArrayList<Check>();
		
		PaymentParser pp = new PaymentParser("paiement.E1", "");
		pp.initializeFromFile();
		receivedChecks.addAll(pp.parseAndGetCheckList());
		pp = new PaymentParser("paiement.E2", "");
		pp.initializeFromFile();
		receivedChecks.addAll(pp.parseAndGetCheckList());
		
		for (Manager manager : managers) {
			manager.receiveChecksToCheck(receivedChecks);
		}
		
		// La journée se passe et on arrive a la fin de la journée
		
		for (Manager manager : managers) {
			Map<Long, List<Check>> checkToSend = manager.getAllReceivedChecksGroupByManager();
			
			for (Entry<Long, List<Check>> entry : checkToSend.entrySet()) {
				pp = new PaymentParser();
				pp.setInput("paiementForManager"+entry.getKey());
				pp.setOutput("paiementForManager"+entry.getKey());
				pp.initializeFromFile();
				pp.parse();
				for (Check check : entry.getValue()) {
					pp.addNewPaymentCheck(check);
				}
				pp.transform();
			}
		}
	}
	
	public static void accountOwnerReceivePaymentsForClients() {
		ManagerParser parser = new ManagerParser();
		parser.setInput("manager0");
		parser.initializeFromFile();
		
		Manager manager0 = parser.retrieveManagerFromInput();
		
		PaymentParser pp = new PaymentParser("paiementForManager0", "");
		pp.initializeFromFile();
		List<Check> receivedChecks = pp.parseAndGetCheckList();
		for (Check check : receivedChecks) {
			manager0.registerCheckUsedByClient(check);
		}

		parser.setOutput("manager0");
		parser.recordManager(manager0);
		parser.transform();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// Jeux de test
		initializeManagers();
//		// Process
		customerAskCheckToAccountOwner();
		accountOwnerReceiveAskedCheckAndReply();
		customerReceiveCheckReplyAndPay();
		enterpriseReceiveChecksForPayment();
		accountOwnerReceivePaymentsForAnEnterprise();
		accountOwnerReceivePaymentsForClients();
	}

}
