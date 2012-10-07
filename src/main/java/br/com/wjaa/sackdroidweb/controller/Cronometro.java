package br.com.wjaa.sackdroidweb.controller;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Cronometro {

	
	private static SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss:sss");
	
	public static void press(){
		System.out.println(sdf.format(new Date()));
	}
	
}
