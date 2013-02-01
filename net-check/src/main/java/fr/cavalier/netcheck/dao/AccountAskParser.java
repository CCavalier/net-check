package fr.cavalier.netcheck.dao;

import fr.cavalier.netcheck.model.Manager;


/**
 * <p>Parser pour la transformation des demandes de comptes en comptes clients</p>
 * @author C Cavalier 
 * @date 26 janv. 2013
 * net-check
 * fr.cavalier.netcheck.dao ManagerParser.java 
 */
public class AccountAskParser extends XmlParser {

	public AccountAskParser(Manager manager){
		super.gestionnaire = new AccountAskHandler(manager);
	}
	
}
