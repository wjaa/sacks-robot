package br.com.wjaa.sackdroidweb.controller;

/**
 * 
 * @author wagneraraujo-sao
 *
 */
public enum OrderType {

	MAIOR_PRECO,
	MENOR_PRECO;

	public static OrderType getOrder(Integer orderType) {
		switch (orderType) {
		case 0:return MAIOR_PRECO;
		case 1:return MENOR_PRECO;
			
		}
		return MENOR_PRECO;
	}
}
