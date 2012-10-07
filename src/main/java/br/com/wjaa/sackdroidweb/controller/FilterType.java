package br.com.wjaa.sackdroidweb.controller;

public enum FilterType{
	FILTER_MAIOR_QUE,
	FILTER_MENOR_QUE,
	FILTER_IQUAL_A;

	public static FilterType getFilter(Integer filterType) {
		switch (filterType) {
		case 0: return FILTER_MAIOR_QUE;
		case 1: return FILTER_MENOR_QUE;
		case 2: return FILTER_IQUAL_A;
		}
		return FILTER_MENOR_QUE;
	}
}
