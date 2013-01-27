package fr.cavalier.netcheck.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * @author C Cavalier
 * @date 26 janv. 2013
 * net-check
 * fr.cavalier.netcheck.model Account.java 
 */
public class Account {

	private Set<Check> availableCheques;
	private Set<Check> usedCheques;
	private Customer user;
	private double balance;
	
	public Account(){
		availableCheques=new HashSet<Check>();
		usedCheques = new HashSet<Check>();
	}
	
	public Set<Check> getAvailableCheques() {
		return availableCheques;
	}
	
	public void setAvailableCheques(Set<Check> cheques) {
		this.availableCheques = cheques;
	}
	
	public Set<Check> getUsedCheques() {
		return usedCheques;
	}
	
	public void setUsedCheques(Set<Check> usedCheques) {
		this.usedCheques = usedCheques;
	}
	
	public double getBalance() {
		return balance;
	}
	
	public void setBalance(double balance) {
		this.balance = balance;
	}
	
	public Customer getUser() {
		return user;
	}
	
	public void setUser(Customer user) {
		this.user = user;
	}
	
	public Check getAvailableCheckForCurrency(String currency) {
		Check result = null;
		for (Check check : getAvailableCheques()) {
			if (check.getCurrency().equals(currency)) {
				if (result == null || result.getId() > check.getId()) {
					result = check;
				}
			}
		}
		return result;
	}
	
	public boolean isAbleToPay(String currency, double value) {
		if (getBalance() > value && getAvailableCheckForCurrency(currency) != null) {
			return true;
		}
		return false;
	}
	
	public Check writeCheck(String currency, double value) {
		Check availableCheck = getAvailableCheckForCurrency(currency);
		availableCheck.setValue(value);
		availableCheck.setDate(new Date());
		
		setBalance(getBalance() - value);
		getAvailableCheques().remove(availableCheck);
		getUsedCheques().add(availableCheck);
		
		return availableCheck;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((availableCheques == null) ? 0 : availableCheques.hashCode());
		long temp;
		temp = Double.doubleToLongBits(balance);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result
				+ ((usedCheques == null) ? 0 : usedCheques.hashCode());
		result = prime * result + ((user == null) ? 0 : user.hashCode());
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
		Account other = (Account) obj;
		if (availableCheques == null) {
			if (other.availableCheques != null)
				return false;
		} else if (!availableCheques.equals(other.availableCheques))
			return false;
		if (Double.doubleToLongBits(balance) != Double
				.doubleToLongBits(other.balance))
			return false;
		if (usedCheques == null) {
			if (other.usedCheques != null)
				return false;
		} else if (!usedCheques.equals(other.usedCheques))
			return false;
		if (user == null) {
			if (other.user != null)
				return false;
		} else if (!user.equals(other.user))
			return false;
		return true;
	}
	
	

}
