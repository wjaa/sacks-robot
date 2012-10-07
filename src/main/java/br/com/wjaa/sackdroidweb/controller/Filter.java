package br.com.wjaa.sackdroidweb.controller;

public class Filter {

	private FilterType type;
	private Double value;
	
	
	public Filter (FilterType type, Double value){
		this.type = type;
		this.value = value;
	}
	public FilterType getType() {
		return type;
	}
	public void setType(FilterType type) {
		this.type = type;
	}
	public Double getValue() {
		return value;
	}
	public void setValue(Double value) {
		this.value = value;
	}
	
	public boolean validade(Double v){
		if (v == null){
			return false;
		}
		switch (type) {
			case FILTER_IQUAL_A: return v.equals(value);
			case FILTER_MAIOR_QUE: return v >= value;
			case FILTER_MENOR_QUE: return v <= value;
		}
		return false;
	}
	
}
