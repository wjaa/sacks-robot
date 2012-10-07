package br.com.wjaa.sackdroidweb.controller;

import java.text.NumberFormat;

/**
 * 
 * @author wagneraraujo-sao
 *
 */
public class Utils {

	
	/**
	 * 
	 * @param value
	 * @return
	 */
	public static final String numberAsCurrency(Object value) {
		NumberFormat nf = NumberFormat.getCurrencyInstance();
		return nf.format(value);
	}
	
	
}
