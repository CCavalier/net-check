package fr.cavalier.netcheck.model;

import java.util.ArrayList;
import java.util.List;

public class Order {
	
	private Enterprise entreprise;
	private List<String> items;
	private List<Check> paymentsChecks;
	
	public Order() {
		items = new ArrayList<String>();
		paymentsChecks = new ArrayList<Check>();
	}

	public Enterprise getEntreprise() {
		return entreprise;
	}

	public void setEntreprise(Enterprise entreprise) {
		this.entreprise = entreprise;
	}

	public List<String> getItems() {
		return items;
	}

	public void setItems(List<String> items) {
		this.items = items;
	}

	public List<Check> getPaymentsChecks() {
		return paymentsChecks;
	}

	public void setPaymentsChecks(List<Check> paymentsChecks) {
		this.paymentsChecks = paymentsChecks;
	}

}
