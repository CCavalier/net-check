package fr.cavalier.netcheck.model;

import java.util.HashMap;

/**
 * @author C Cavalier
 * @date 26 janv. 2013 net-check fr.cavalier.netcheck.model Customer.java
 */
public class Customer {

	private String name;
	private String lastname;
	private String location;
	private Account account;

	/**
	 * @param name
	 * @param lastname
	 * @param location
	 */
	public Customer(String name, String lastname, String location) {
		super();
		this.name = name;
		this.lastname = lastname;
		this.location = location;
	}

	public Customer() {

	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((lastname == null) ? 0 : lastname.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Customer other = (Customer) obj;
		if(!other.getName().equals(this.getName())){
			return false;
		}else if(!other.getLastname().equals(getLastname())){
			return false;
		}
	/*	if (account == null) {
			if (other.account != null)
				return false;
		} else if (!account.equals(other.account))
			return false;
		if (lastname == null) {
			if (other.lastname != null)
				return false;
		} else if (!lastname.equals(other.lastname))
			return false;
		if (location == null) {
			if (other.location != null)
				return false;
		} else if (!location.equals(other.location))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;*/
		return true;
	}

	public void askNewAccount(double montant, HashMap<String, Integer> checks) {
		
	}
	
	@Override
	public String toString() {
		return this.getName() + " - " + this.getLastname();
	}
	
	public Check paiement(String currency, double value, Enterprise e) {
		if (getAccount().isAbleToPay(currency, value)) {
			return getAccount().writeCheck(currency, value, e);
		}
		return null;
	}
	
	public void useCheck(Check check) {
		Check registeredCheck = null;
		for (Check ownCheck : getAccount().getAvailableCheques()) {
			if (ownCheck.getId().equals(check.getId())) {
				registeredCheck = ownCheck;
				break;
			}
		}
		registeredCheck.setDate(check.getDate());
		registeredCheck.setValue(check.getValue());
		getAccount().getAvailableCheques().remove(registeredCheck);
		getAccount().getUsedCheques().add(registeredCheck);
	}
}
