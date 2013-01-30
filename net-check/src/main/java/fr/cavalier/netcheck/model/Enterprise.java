package fr.cavalier.netcheck.model;

import java.util.ArrayList;
import java.util.List;

/**
 * @author malika 
 * @date 27 janv. 2013
 * net-check
 * fr.cavalier.netcheck.model Enterprise.java 
 */
public class Enterprise {
	
	private String name;
	private String location;
	private Account account;
	private List<Check> waitingForTreatmentChecks;
	
	public Enterprise() {
		waitingForTreatmentChecks = new ArrayList<Check>();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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
	
	public List<Check> getWaitingForTreatmentChecks() {
		return waitingForTreatmentChecks;
	}

	public void setWaitingForTreatmentChecks(List<Check> waitingForTreatmentChecks) {
		this.waitingForTreatmentChecks = waitingForTreatmentChecks;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
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
		Enterprise other = (Enterprise) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return this.getName();
	}

}
