package fr.cavalier.netcheck.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author C Cavalier
 * @date 26 janv. 2013
 * net-check
 * fr.cavalier.netcheck.model Manager.java 
 */
public class Manager {

	private Long identifiant;
	private List<Customer> users;
	private List<Enterprise> enterprises;
	private List<Check> receivedChecks;
	
	public Manager(){
		users=new ArrayList<Customer>();
		enterprises = new ArrayList<Enterprise>();
		receivedChecks = new ArrayList<Check>();
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
	
	public List<Enterprise> getEnterprises() {
		return enterprises;
	}

	public void setEnterprises(List<Enterprise> enterprises) {
		this.enterprises = enterprises;
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
				existingAccount.getAvailableCheques().addAll(customer.getAccount().getAvailableCheques());
			}
		}
	}
	
	public Map<Long,List<Check>> getAllReceivedChecksGroupByManager() {
		Map<Long, List<Check>> result = new HashMap<Long, List<Check>>();
		for (Check check : receivedChecks) {
			if (!result.containsKey(check.getAccountOwner())) {
				result.put(check.getAccountOwner(), new ArrayList<Check>());
			}
			result.get(check.getAccountOwner()).add(check);
		}
		return result;
	}
	
	public void receiveChecksToCheck(List<Check> checks) {
		for (Enterprise enterprise : getEnterprises()) {
			for (Check check : checks) {
				if (check.getCible().equals(enterprise)) {
					receivedChecks.add(check);
					enterprise.getAccount().setBalance(enterprise.getAccount().getBalance() + check.getValue());
				}
			}
		}
	}
	
	public void registerCheckUsedByClient(Check check) {
		Customer registeredCustomer = getUsers().get(getUsers().indexOf(check.getUser()));
		registeredCustomer.useCheck(check);
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
