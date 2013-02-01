package fr.cavalier.netcheck.util;

/**
 * @author C Cavalier 
 * @date 27 janv. 2013
 * net-check
 * fr.cavalier.netcheck.util CheckIdGenerator.java 
 */
public class CheckIdGenerator {
	
	private static long idCounter = 0L;
	
	public static long getNextId() {
		return idCounter++;
	}

}
