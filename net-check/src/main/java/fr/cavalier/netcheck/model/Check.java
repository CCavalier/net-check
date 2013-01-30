package fr.cavalier.netcheck.model;

import java.util.Date;

/**
 * @author C Cavalier
 * @date 26 janv. 2013
 * net-check
 * fr.cavalier.netcheck.model Check.java 
 */
public class Check implements Cloneable {

	private Long id;
	private String currency;
	private Double value;
	private Date date;
	private Customer user;
	private Enterprise cible;
	private Long accountOwner;
	
	public String getCurrency() {
		return currency;
	}
	public Enterprise getCible() {
		return cible;
	}
	public void setCible(Enterprise cible) {
		this.cible = cible;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public Double getValue() {
		return value;
	}
	public void setValue(Double value) {
		this.value = value;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public Customer getUser() {
		return user;
	}
	public void setUser(Customer user) {
		this.user = user;
	}
	public Long getAccountOwner() {
		return accountOwner;
	}
	public void setAccountOwner(Long accountOwner) {
		this.accountOwner = accountOwner;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		Check other = (Check) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	@Override
	public Object clone() {
		Check result = new Check();
		result.setAccountOwner(this.getAccountOwner());
		result.setCurrency(this.getCurrency());
		result.setDate(this.getDate());
		result.setUser(this.getUser());
		result.setValue(this.getValue());
		return result;
	}
	
	
	public void fill(Enterprise e, double pValue, Date pDate){
		this.cible=e;
		this.value=pValue;
		this.date=pDate;
	}
	
	
}
