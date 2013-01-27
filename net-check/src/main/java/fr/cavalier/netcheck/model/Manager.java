package fr.cavalier.netcheck.model;

import java.util.ArrayList;
import java.util.List;

/**
 * @author C Cavalier
 * @date 26 janv. 2013
 * net-check
 * fr.cavalier.netcheck.model Manager.java 
 */
public class Manager {

	private Long identifiant;
	private List<Customer> users;
	
	public Manager(){
		users=new ArrayList<Customer>();
	}
	
	/**
	 * @return the users
	 */
	public List<Customer> getUsers() {
		return users;
	}
	/**
	 * @param users the users to set
	 */
	public void setUsers(List<Customer> users) {
		this.users = users;
	}
	public Long getIdentifiant() {
		return identifiant;
	}

	public void setIdentifiant(Long identifiant) {
		this.identifiant = identifiant;
	}
	
	public void insertAsks(List<Customer> customers) {
		for (Customer customer : customers) {
			if (!getUsers().contains(customer)) {
				getUsers().add(customer);
			} else {
				Customer existingCustomer = getUsers().get(getUsers().indexOf(customer));
				Account existingAccount = existingCustomer.getAccount();
				existingAccount.setBalance(existingAccount.getBalance() + customer.getAccount().getBalance());
				existingAccount.getCheques().addAll(customer.getAccount().getCheques());
			}
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((users == null) ? 0 : users.hashCode());
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
		Manager other = (Manager) obj;
		if (users == null) {
			if (other.users != null)
				return false;
		} else if (!users.equals(other.users))
			return false;
		return true;
	}

}
