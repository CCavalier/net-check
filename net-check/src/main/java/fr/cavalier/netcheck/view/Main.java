
package fr.cavalier.netcheck.view;

import java.util.HashMap;

import fr.cavalier.netcheck.dao.CustomerParser;
import fr.cavalier.netcheck.model.Customer;

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
		
		CustomerParser parser=new CustomerParser();
		parser.parse();
		parser.askNewAccount(clientOne, chequesDemandes, 200.);
		parser.transform();
		

	}

}
