
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
		
		Customer clientThree = new Customer("Marchal", "Pierre", "46 avenue des landes");
		HashMap<String,Integer> chequesDemandes3= new HashMap<String,Integer>();
		chequesDemandes3.put("EUR", 2);
		
		CustomerParser parser=new CustomerParser();
		parser.initializeFromFile();
		parser.askNewAccount(clientOne, chequesDemandes, 200);
		parser.askNewAccount(clientTwo, chequesDemandes2, 200);
		parser.setDtdFile("accountAsk.dtd");
		parser.setOutput("accountAsk.Manager0");
		parser.transform();
		parser.cleanDocument();
		parser.setOutput("accountAsk.Manager1");
		parser.askNewAccount(clientThree, chequesDemandes3, 50);
		parser.setDtdFile("accountAsk.dtd");
		parser.transform();
	}
	
	public static void accountOwnerReceiveAskedCheckAndReply() {
		// Manager 0 recoit ses demandes
		ManagerParser parser = new ManagerParser();
		parser.setInput("manager0");
		parser.initializeFromFile();
		
		Manager aManager0 = parser.retrieveManagerFromInput();
		
		AccountAskParser askp=new AccountAskParser("accountAsk.Manager0", aManager0);
		askp.initializeFromFile();
		askp.parse();
		
		// Recuperation des demandes
		CheckListParser chkp = new CheckListParser();
		chkp.initializeFromFile();
		for (Customer customer : aManager0.getUsers()) {
			chkp.setOutput("chequier."+customer.getName()+"-"+customer.getLastname());
			chkp.recordCheckListForUser(customer);
			chkp.transform();
		}
		
		//Enregistrement des cheques attribues
		parser.setOutput("manager0");
		parser.recordManager(aManager0);
		parser.transform();
		
		//Manager1 recoit ses demandes
		parser = new ManagerParser();
		parser.setInput("manager1");
		parser.initializeFromFile();
		
		Manager aManager1 = parser.retrieveManagerFromInput();
		
		// Recuperation des demandes
		askp=new AccountAskParser("accountAsk.Manager1", aManager1);
		askp.initializeFromFile();
		askp.parse();
		
		//Enregistrement des cheques attribues
		chkp = new CheckListParser();
		chkp.initializeFromFile();
		for (Customer customer : aManager1.getUsers()) {
			chkp.setOutput("chequier."+customer.getName()+"-"+customer.getLastname());
			chkp.recordCheckListForUser(customer);
			chkp.transform();
		}
		
		parser.setOutput("manager1");
		parser.recordManager(aManager1);
		parser.transform();
	}
	
	public static void customerReceiveCheckReplyAndPay() {
		Customer client1 = new Customer();
		Customer client2 = new Customer();
		Customer client3 = new Customer();
		
		Enterprise e1= new Enterprise();
		e1.setName("E1");
		e1.setLocation("adresse");
		
		Enterprise e2= new Enterprise();
		e2.setName("E2");
		e2.setLocation("adresse2");
		
		// Reception des cheques par le client Cavalier Charlotte
		CheckListParser parser = new CheckListParser("chequier.Cavalier-Charlotte", client1);
		parser.initializeFromFile();
		parser.parse();
		
		// Creation de paiements pour le client
		PaymentParser pp = new PaymentParser();
		pp.initializeFromFile();
		pp.addNewPaymentCheck(client1.paiement("EUR", 100.0, e1));
		pp.addNewPaymentCheck(client1.paiement("EUR", 37.5, e1));
		pp.addNewPaymentCheck(client1.paiement("USD", 34.8, e2));
		pp.setOutput("paiementCavalier");
		pp.transform();
		
		
		// Reception des cheques par le client Francois Teychene
		parser = new CheckListParser("chequier.Teychene-Francois", client2);
		parser.initializeFromFile();
		parser.parse();
		
		// Creation de paiements pour le client
		pp = new PaymentParser();
		pp.initializeFromFile();
		pp.addNewPaymentCheck(client2.paiement("EUR", 150, e1));
		pp.setOutput("paiementTeychene");
		pp.transform();
		
		// Reception des cheques par le client Francois Teychene
		parser = new CheckListParser("chequier.Marchal-Pierre", client3);
		parser.initializeFromFile();
		parser.parse();
		
		// Creation de paiements pour le client
		pp = new PaymentParser();
		pp.initializeFromFile();
		pp.addNewPaymentCheck(client3.paiement("EUR", 200.0, e1));
		pp.addNewPaymentCheck(client3.paiement("EUR",50.0, e2));
		pp.setOutput("paiementMarchal");
		pp.transform();
	}
	
	public static void enterpriseReceiveChecksForPayment() {
		
		List<Check> receivedChecks = new ArrayList<Check>();
		
		PaymentParser pp = new PaymentParser("paiementCavalier", "");
		pp.initializeFromFile();
		receivedChecks.addAll(pp.parseAndGetCheckList());
		pp = new PaymentParser("paiementTeychene", "");
		pp.initializeFromFile();
		receivedChecks.addAll(pp.parseAndGetCheckList());
		pp = new PaymentParser("paiementMarchal", "");
		pp.initializeFromFile();
		receivedChecks.addAll(pp.parseAndGetCheckList());
		
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
		ManagerParser parser0 = new ManagerParser();
		parser0.setInput("manager0");
		parser0.initializeFromFile();
		
		Manager manager0 = parser0.retrieveManagerFromInput();
		
		ManagerParser parser1 = new ManagerParser();
		parser1.setInput("manager1");
		parser1.initializeFromFile();
		
		Manager manager1 = parser1.retrieveManagerFromInput();
		
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
		
		parser0.setOutput("manager0");
		parser0.recordManager(manager0);
		parser0.transform();
		
		parser1.setOutput("manager1");
		parser1.recordManager(manager1);
		parser1.transform();
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
		
		ManagerParser parser1 = new ManagerParser();
		parser1.setInput("manager1");
		parser1.initializeFromFile();
		
		Manager manager1 = parser1.retrieveManagerFromInput();
		
		PaymentParser pp1 = new PaymentParser("paiementForManager1", "");
		pp1.initializeFromFile();
		List<Check> receivedChecks1 = pp1.parseAndGetCheckList();
		for (Check check : receivedChecks1) {
			manager1.registerCheckUsedByClient(check);
		}

		parser.setOutput("manager1");
		parser.recordManager(manager1);
		parser.transform();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// Jeux de test
		initializeManagers();
		// Process
		customerAskCheckToAccountOwner();
		accountOwnerReceiveAskedCheckAndReply();
		customerReceiveCheckReplyAndPay();
		enterpriseReceiveChecksForPayment();
		accountOwnerReceivePaymentsForAnEnterprise();
		accountOwnerReceivePaymentsForClients();
	}

}
