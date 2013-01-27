
package fr.cavalier.netcheck.view;

import java.util.HashMap;

import fr.cavalier.netcheck.dao.CustomerParser;
import fr.cavalier.netcheck.dao.ManagerParser;
import fr.cavalier.netcheck.model.Customer;
import fr.cavalier.netcheck.model.Manager;

/*
import javax.xml.xpath.*;
import javax.xml.parsers.*;
*/
public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Customer clientOne = new Customer("Toto", "Foo", "AZE @ Perpi") ;
		HashMap<String,Integer> chequesDemandes= new HashMap<String,Integer>();
		chequesDemandes.put("EUR", 2);
		chequesDemandes.put("USD", 3);
		
		
		Customer clientTwo = new Customer("Titi", "uzaeiuyazue", "AZE @ Perpi") ;
		HashMap<String,Integer> chequesDemandes2= new HashMap<String,Integer>();
		chequesDemandes2.put("HKD", 2);
		chequesDemandes2.put("CAD", 3);
		CustomerParser parser=new CustomerParser();
		parser.parse();
		parser.askNewAccount(clientOne, chequesDemandes, 200.);
		parser.askNewAccount(clientTwo, chequesDemandes2, 200.);
		parser.transform();
		
		Manager manager = new Manager();
		manager.setIdentifiant(0L);
		
		ManagerParser mp=new ManagerParser(manager);
		mp.parse();
		mp.saxInit();
		//mp.parse();
		mp.createChecksList(manager);
		mp.transform();
		

	}

}
