package fr.cavalier.netcheck.model;

import java.util.HashSet;
import java.util.Set;

/**
 * @author C Cavalier
 * @date 26 janv. 2013
 * net-check
 * fr.cavalier.netcheck.model Account.java 
 */
public class Account {

	private Set<Check> cheques;
	private double balance;
	
	public Account(){
		cheques=new HashSet<Check>();
	}
	public Set<Check> getCheques() {
		return cheques;
	}
	public void setCheques(Set<Check> cheques) {
		this.cheques = cheques;
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
	private Customer user;
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cheques == null) ? 0 : cheques.hashCode());
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
		if (cheques == null) {
			if (other.cheques != null)
				return false;
		} else if (!cheques.equals(other.cheques))
			return false;
		if (user == null) {
			if (other.user != null)
				return false;
		} else if (!user.equals(other.user))
			return false;
		return true;
	}
	
	public void useCheck(Check cheque) throws Exception{
		if (getCheques().contains(cheque)) {
			getCheques().remove(cheque);
		}
	
	}

}
