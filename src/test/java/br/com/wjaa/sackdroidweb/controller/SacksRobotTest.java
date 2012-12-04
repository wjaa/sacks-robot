package br.com.wjaa.sackdroidweb.controller;

import java.util.List;

import org.junit.Test;

/**
 * 
 * @author Wagner Araujo wagner.wjaa@gmail.com
 *
 */
public class SacksRobotTest {

	@Test
	public void testLetsGo() {
		SacksRobot sacksRobot = new SacksRobot(new String[]{"perfume"}, 200.0, FilterType.FILTER_MENOR_QUE, OrderType.MENOR_PRECO);
		List<Produto> produtos = sacksRobot.letsGo();
		
		System.out.println(produtos.size());
		
	}

}
