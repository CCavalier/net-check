package fr.cavalier.netcheck.dao;

import java.util.ArrayList;
import java.util.List;

import fr.cavalier.netcheck.model.Account;
import fr.cavalier.netcheck.model.Check;
import fr.cavalier.netcheck.model.Customer;
import fr.cavalier.netcheck.util.CheckIdGenerator;

/**
 * @author M Sitruk
 * @date 27 janv. 2013
 * net-check
 * fr.cavalier.netcheck.dao ManagerParserContext.java 
 * <p>Gere les differentes variables à manipuler dans le handler Account Ask</p>
 */
public class AccountAskContext {
	
	private List<Customer> customerList;
	private Customer currentCustomer;
	private Account currentAccount;
	private Check currentCheck;
	
	public AccountAskContext() {
		customerList = new ArrayList<Customer>();
	}

	public List<Customer> getCustomerList() {
		return customerList;
	}

	public void setCustomerList(List<Customer> customerList) {
		this.customerList = customerList;
	}

	public Customer getCurrentCustomer() {
		return currentCustomer;
	}

	public void setCurrentCustomer(Customer currentCustomer) {
		this.currentCustomer = currentCustomer;
	}

	public Account getCurrentAccount() {
		return currentAccount;
	}

	public void setCurrentAccount(Account currentAccount) {
		this.currentAccount = currentAccount;
	}

	public Check getCurrentCheck() {
		return currentCheck;
	}

	public void setCurrentCheck(Check currentCheck) {
		this.currentCheck = currentCheck;
	}
	
	public void addCustomer(Customer customer) {
		setCurrentCustomer(customer);
	}
	
	public void finishCustomer() {
		finishAccount();
		if (!getCustomerList().contains(getCurrentCustomer())) {
			getCustomerList().add(getCurrentCustomer());
		} else {
			setCurrentCustomer(getCustomerList().get(getCustomerList().indexOf(getCurrentCustomer())));
		}
	}
	
	public void finishAsk() {
		finishAccount();
		setCurrentCustomer(null);
	}
	
	/**
	 * 
	 * @param account
	 */
	public void addAccount(Account account) {
		if (getCurrentCustomer() == null) {
			throw new RuntimeException("Account configuration is not valid beacause it's not liked to a Customer");
		}
		if (getCurrentCustomer().getAccount() == null) {
			getCurrentCustomer().setAccount(account);
			setCurrentAccount(account);
		} else {
			setCurrentAccount(getCurrentCustomer().getAccount());
			// On ajoute les balances
			getCurrentAccount().setBalance(getCurrentAccount().getBalance() + account.getBalance());
		}
	}
	
	public void finishAccount() {
		finishCheck();
		setCurrentAccount(null);
	}
	
	public void addCheck(Check check) {
		if (getCurrentAccount() == null) {
			throw new RuntimeException("Account configuration is not valid beacause it's not liked to a Customer");
		} else if (getCurrentCustomer() == null) {
			throw new RuntimeException("Check configuration is not valid beacause it's not liked to a Customer");
		}
		check.setUser(getCurrentCustomer());
		setCurrentCheck(check);
	}
	
	public void setValueForCheck(Integer value) {
		if (getCurrentCheck() == null) {
			throw new RuntimeException("Value configuration is not valid beacause it's not liked to a Check");
		}
		Check cheque;
		for (int i=0; i<value; i++) {
			cheque = (Check) getCurrentCheck().clone();
			cheque.setId(CheckIdGenerator.getNextId());
			getCurrentAccount().getAvailableCheques().add(cheque);
		}
	}
	
	public void finishCheck() {
		setCurrentCheck(null);
	}
	
	public List<Account> getAllAccounts() {
		List<Account> result = new ArrayList<Account>();
		for (Customer customer : getCustomerList()) {
			result.add(customer.getAccount());
		}
		return result;
	}
	
	public List<Check> getAllChecks() {
		List<Check> result = new ArrayList<Check>();
		for (Customer customer : getCustomerList()) {
			result.addAll(customer.getAccount().getAvailableCheques());
		}
		return result;
	}
}
