package fr.cavalier.netcheck.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author malika 
 * @date 27 janv. 2013
 * net-check
 * fr.cavalier.netcheck.model Enterprise.java 
 */
public class Enterprise {
	
	private static Map<String, Double> prices;
	static {
		prices = new HashMap<String, Double>();
		prices.put("Kindle Fire", 199.0);
		prices.put("Souris Razer Abyssus", 30.0);
		prices.put("Clavier Logitech Xm30", 35.5);
		prices.put("Saphire Raddeon 6900 VaporX", 120.0);
		prices.put("Livre XML par la pratique", 35.0);
	}
	
	private String name;
	private String location;
	private Account account;
	private List<Check> waitingForTreatmentChecks;
	
	private Map<String, Integer> stock;
	
	public Enterprise() {
		waitingForTreatmentChecks = new ArrayList<Check>();
		stock = new HashMap<String, Integer>();
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
	
	public Map<String, Integer> getStock() {
		return stock;
	}

	public void setStock(Map<String, Integer> stock) {
		this.stock = stock;
	}

	public boolean receiveOrder(Order order) {
		if (order.getEntreprise().equals(this)) {
			boolean result = true;
			// Validation of order
			// Verification stock ...
			double totalPrice = 0.0;
			for (String item : order.getItems()) {
				if (getStock().containsKey(item) && getStock().get(item) > 0) {
					totalPrice += prices.get(item);
				} else {
					System.out.println("Commande annule pour cause de stock inssufisant");
					result = false;
				}
			}
			
			double orderValue = 0.0;
			for (Check cheque : order.getPaymentsChecks()) {
				orderValue += cheque.getValue();
			}
			if (totalPrice > orderValue) {
				System.out.println("Commande annule pour cause de paiment insuffisant");
				result = false;
			}
			if (result == true) {
				for (String item : order.getItems()) {
					getStock().put(item, getStock().get(item) - 1);
				}
				
				for (Check cheque : order.getPaymentsChecks()) {
					getWaitingForTreatmentChecks().add(cheque);
				}
			}
			return result;
		}
		return false;
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
