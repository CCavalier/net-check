package fr.cavalier.netcheck.model;

import java.util.HashMap;

/**
 * @author C Cavalier
 * @date 26 janv. 2013
 * net-check
 * fr.cavalier.netcheck.model Account.java 
 */
public class Account {

	private HashMap<Check, Integer> cheques;
	public HashMap<Check, Integer> getCheques() {
		return cheques;
	}
	public void setCheques(HashMap<Check, Integer> cheques) {
		this.cheques = cheques;
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
		if(cheques.get(cheque)>= 0){
			cheques.put(cheque, cheques.get(cheque)-1);
		}else throw new Exception("No more check available");
	
	}

}
