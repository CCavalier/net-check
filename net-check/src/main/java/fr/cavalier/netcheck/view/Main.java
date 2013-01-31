
package fr.cavalier.netcheck.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.parsers.ParserConfigurationException;

import fr.cavalier.netcheck.dao.AccountAskParser;
import fr.cavalier.netcheck.dao.CheckListParser;
import fr.cavalier.netcheck.dao.CustomerParser;
import fr.cavalier.netcheck.dao.ManagerParser;
import fr.cavalier.netcheck.dao.OrderParser;
import fr.cavalier.netcheck.dao.PaymentParser;
import fr.cavalier.netcheck.model.Account;
import fr.cavalier.netcheck.model.Check;
import fr.cavalier.netcheck.model.Customer;
import fr.cavalier.netcheck.model.Enterprise;
import fr.cavalier.netcheck.model.Manager;
import fr.cavalier.netcheck.model.Order;

/*
import javax.xml.xpath.*;
import javax.xml.parsers.*;
*/
public class Main {
	
	private static Map<String, Integer> stockForEnterprise1;
	private static Map<String, Integer> stockForEnterprise2;
	
	static {
		stockForEnterprise1 = new HashMap<String, Integer>();
		stockForEnterprise1.put("Kindle Fire", 1);
		stockForEnterprise1.put("Souris Razer Abyssus", 6);
		stockForEnterprise1.put("Clavier Logitech Xm30", 2);
		stockForEnterprise1.put("Saphire Raddeon 6900 VaporX", 1);
		
		stockForEnterprise2 = new HashMap<String, Integer>();
		stockForEnterprise2.put("Livre XML par la pratique", 2);
	}
	
	public static void initializeManagers() throws ParserConfigurationException {
		
		ManagerParser managerParser = new ManagerParser();
		
		Manager manager0 = new Manager();
		manager0.setIdentifiant(0L);
		
		Manager manager1 = new Manager();
		manager1.setIdentifiant(1L);
		
		Customer customer0 = new Customer("Cavalier", "Charlotte", "18 rue des peupliers");
		Account customer0Account = new Account();
		customer0Account.setBalance(300.0);
		customer0.setAccount(customer0Account);
		
		Customer customer1 = new Customer("Teychene", "Francois", "18 rue des peupliers");
		Account customer1Account = new Account();
		customer1Account.setBalance(100.0);
		customer1.setAccount(customer1Account);
		
		Customer customer2 = new Customer("Marchal", "Pierre", "46 avenue des landes");
		Account customer2Account = new Account();
		customer2Account.setBalance(600);
		customer2.setAccount(customer2Account);
		
		Enterprise e1= new Enterprise();
		e1.setName("E1");
		e1.setLocation("adresse");
		Account e1Account = new Account();
		e1Account.setBalance(8000.0);
		e1.setAccount(e1Account);
		
		Enterprise e2= new Enterprise();
		e2.setName("E2");
		e2.setLocation("adresse2");
		Account e2Account = new Account();
		e2Account.setBalance(8000.0);
		e2.setAccount(e2Account);
		
		manager0.getUsers().add(customer0);
		manager0.getUsers().add(customer1);
		manager0.getEnterprises().add(e1);
		
		managerParser.recordManager(manager0);
		managerParser.saveDocumentToFile("manager0");
		
		manager1.getUsers().add(customer2);
		manager1.getEnterprises().add(e2);
		
		managerParser.recordManager(manager1);
		managerParser.saveDocumentToFile("manager1");
	}
	
	public static void customerAskCheckToAccountOwner() throws ParserConfigurationException {
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
		chequesDemandes3.put("EUR", 3);
		
		CustomerParser parser=new CustomerParser();
		parser.setDtdFile("accountAsk.dtd");
		parser.initializeDocument();
		parser.askNewAccount(clientOne, chequesDemandes, 200);
		parser.askNewAccount(clientTwo, chequesDemandes2, 200);
		parser.saveDocumentToFile("accountAsk.Manager0");
		parser.initializeDocument();
		parser.askNewAccount(clientThree, chequesDemandes3, 50);
		parser.saveDocumentToFile("accountAsk.Manager1");
	}
	
	public static void accountOwnerReceiveAskedCheckAndReply() throws ParserConfigurationException {
		// Manager 0 recoit ses demandes
		ManagerParser parser = new ManagerParser();
		
		Manager aManager0 = parser.retrieveManagerFromInput("manager0");
		
		AccountAskParser askp= new AccountAskParser(aManager0);
		askp.parse("accountAsk.Manager0");
		
		// Recuperation des demandes
		CheckListParser chkp = new CheckListParser();
		chkp.initializeDocument();
		for (Customer customer : aManager0.getUsers()) {
			chkp.recordCheckListForUser(customer);
			chkp.saveDocumentToFile("chequier."+customer.getName()+"-"+customer.getLastname());
		}
		
		//Enregistrement des cheques attribues
		parser.recordManager(aManager0);
		parser.saveDocumentToFile("manager0");
		
		//Manager1 recoit ses demandes
		parser = new ManagerParser();
		
		Manager aManager1 = parser.retrieveManagerFromInput("manager1");
		
		// Recuperation des demandes
		askp=new AccountAskParser(aManager1);
		askp.parse("accountAsk.Manager1");
		
		//Enregistrement des cheques attribues
		chkp = new CheckListParser();
		chkp.initializeDocument();
		for (Customer customer : aManager1.getUsers()) {
			chkp.recordCheckListForUser(customer);
			chkp.saveDocumentToFile("chequier."+customer.getName()+"-"+customer.getLastname());
		}
		parser.recordManager(aManager1);
		parser.saveDocumentToFile("manager1");
	}
	
	public static void customerReceiveCheckReplyAndPay() throws ParserConfigurationException {
		Customer client1 = new Customer();
		Customer client2 = new Customer();
		Customer client3 = new Customer();
		
		Enterprise e1= new Enterprise();
		e1.setName("E1");
		e1.setLocation("adresse");
		
		Enterprise e2= new Enterprise();
		e2.setName("E2");
		e2.setLocation("adresse2");
		
		OrderParser orderParser = new OrderParser();
		
		// Reception des cheques par les
		CheckListParser parser = new CheckListParser(client1);
		parser.parse("chequier.Cavalier-Charlotte");
		
		parser = new CheckListParser(client2);
		parser.parse("chequier.Teychene-Francois");
		
		parser = new CheckListParser(client3);
		parser.parse("chequier.Marchal-Pierre");
		
		// Passage des commandes
		
		Order order1 = new Order();
		order1.setEntreprise(e1);
		order1.getItems().add("Souris Razer Abyssus");
		order1.getItems().add("Clavier Logitech Xm30");
		order1.getItems().add("Saphire Raddeon 6900 VaporX");
		client1.paiement("EUR", 40, order1);
		client1.paiement("EUR", 149.5, order1);
		orderParser.createOrder(order1);
		orderParser.saveDocumentToFile("commande0");
		
		Order order2 = new Order();
		order2.setEntreprise(e2);
		order2.getItems().add("Livre XML par la pratique");
		client2.paiement("EUR", 37.5, order2);
		orderParser.createOrder(order2);
		orderParser.saveDocumentToFile("commande1");
		
		Order order3 = new Order();
		order3.setEntreprise(e1);
		order3.getItems().add("Kindle Fire");
		client3.paiement("EUR", 200, order3);
		client3.paiement("EUR", 100, order3);
		orderParser.createOrder(order3);
		orderParser.saveDocumentToFile("commande2");
		
		Order order4 = new Order();
		order4.setEntreprise(e2);
		order4.getItems().add("Livre XML par la pratique");
		client2.paiement("EUR", 10, order4);
		orderParser.createOrder(order4);
		orderParser.saveDocumentToFile("commande3");
		
		Order order5 = new Order();
		order5.setEntreprise(e1);
		order5.getItems().add("Kindle Fire");
		client3.paiement("EUR", 300, order5);
		orderParser.createOrder(order5);
		orderParser.saveDocumentToFile("commande4");
	}
	
	public static void enterpriseReceiveChecksForPayment() throws ParserConfigurationException {
		
		OrderParser orderParser = new OrderParser();
		
		Enterprise entreprise1 = new Enterprise();
		entreprise1.setName("E1");
		entreprise1.setLocation("adresse");
		entreprise1.setStock(stockForEnterprise1);
		
		Enterprise entreprise2 = new Enterprise();
		entreprise2.setName("E2");
		entreprise2.setLocation("adresse2");
		entreprise2.setStock(stockForEnterprise2);
		
		
		Order order0 = orderParser.retrieveOrderFromFile("commande0");
		entreprise1.receiveOrder(order0);
		Order order1 = orderParser.retrieveOrderFromFile("commande1");
		entreprise2.receiveOrder(order1);
		Order order2 = orderParser.retrieveOrderFromFile("commande2");
		entreprise1.receiveOrder(order2);
		Order order3 = orderParser.retrieveOrderFromFile("commande3");
		entreprise2.receiveOrder(order3);
		Order order4 = orderParser.retrieveOrderFromFile("commande4");
		entreprise1.receiveOrder(order4);
		
		
		PaymentParser pp = new PaymentParser();
		pp.initializeDocument();
		for (Check c:entreprise1.getWaitingForTreatmentChecks()) {
			pp.addNewPaymentCheck(c);
		}
		pp.saveDocumentToFile("paiement.E1");
		
		pp.cleanDocument();
		for (Check c:entreprise2.getWaitingForTreatmentChecks()) {
			pp.addNewPaymentCheck(c);
		}
		pp.saveDocumentToFile("paiement.E2");
	}
	
	public static void accountOwnerReceivePaymentsForAnEnterprise() throws ParserConfigurationException {
		ManagerParser parser0 = new ManagerParser();
		
		Manager manager0 = parser0.retrieveManagerFromInput("manager0");
		
		ManagerParser parser1 = new ManagerParser();
		
		Manager manager1 = parser1.retrieveManagerFromInput("manager1");
		
		Manager[] managers = new Manager[] {manager0, manager1 };
		
		List<Check> receivedChecks = new ArrayList<Check>();
		
		PaymentParser pp = new PaymentParser();
		receivedChecks.addAll(pp.parseAndGetCheckList("paiement.E1"));
		receivedChecks.addAll(pp.parseAndGetCheckList("paiement.E2"));
		
		for (Manager manager : managers) {
			manager.receiveChecksToCheck(receivedChecks);
		}
		
		// La journée se passe et on arrive a la fin de la journée
		
		for (Manager manager : managers) {
			Map<Long, List<Check>> checkToSend = manager.getAllReceivedChecksGroupByManager();
			
			for (Entry<Long, List<Check>> entry : checkToSend.entrySet()) {
				pp = new PaymentParser();
				pp.initializeDocumentFromFile("paiementForManager"+entry.getKey());
				for (Check check : entry.getValue()) {
					pp.addNewPaymentCheck(check);
				}
				pp.saveDocumentToFile("paiementForManager"+entry.getKey());
			}
		}
		
		parser0.recordManager(manager0);
		parser0.saveDocumentToFile("manager0");
		
		parser1.recordManager(manager1);
		parser1.saveDocumentToFile("manager1");
	}
	
	public static void accountOwnerReceivePaymentsForClients() throws ParserConfigurationException {
		ManagerParser parser = new ManagerParser();
		
		Manager manager0 = parser.retrieveManagerFromInput("manager0");
		
		PaymentParser pp = new PaymentParser();
		List<Check> receivedChecks = pp.parseAndGetCheckList("paiementForManager0");
		for (Check check : receivedChecks) {
			manager0.registerCheckUsedByClient(check);
		}

		parser.recordManager(manager0);
		parser.saveDocumentToFile("manager0");
		
		Manager manager1 = parser.retrieveManagerFromInput("manager1");
		
		PaymentParser pp1 = new PaymentParser();
		List<Check> receivedChecks1 = pp1.parseAndGetCheckList("paiementForManager1");
		for (Check check : receivedChecks1) {
			manager1.registerCheckUsedByClient(check);
		}

		parser.recordManager(manager1);
		parser.saveDocumentToFile("manager1");
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			// Jeux de test
			initializeManagers();
			// Process
			customerAskCheckToAccountOwner();
			accountOwnerReceiveAskedCheckAndReply();
			customerReceiveCheckReplyAndPay();
			enterpriseReceiveChecksForPayment();
			accountOwnerReceivePaymentsForAnEnterprise();
			accountOwnerReceivePaymentsForClients();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
	}

}
