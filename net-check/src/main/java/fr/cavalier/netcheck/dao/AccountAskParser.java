package fr.cavalier.netcheck.dao;

import fr.cavalier.netcheck.model.Manager;


/**
 * <p>Parser pour la transformation des demandes de comptes en comptes clients</p>
 * @author malika 
 * @date 26 janv. 2013
 * net-check
 * fr.cavalier.netcheck.dao ManagerParser.java 
 */
public class AccountAskParser extends XmlParser {

	public AccountAskParser(String input, Manager manager){
		super.input=input;
		super.gestionnaire =new AccountAskHandler(manager);
	}
	
}
