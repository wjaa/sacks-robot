package br.com.wjaa.sackdroidweb.controller;


/**
 * 
 * @author wagneraraujo-sao
 *
 */
public class Produto implements Comparable<Produto> {

	private Integer id;
	private String descricao;
	private Double precoDe;
	private Double precoPor;
	private String urlImagem;
	private String urlOferta;
	private Filter filtro;
	private OrderType ordertype;
	
	
	public Produto(Filter filtro, OrderType ordertype){
		this.filtro = filtro;
		this.ordertype = ordertype;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	public String getUrlImagem() {
		return urlImagem;
	}
	public void setUrlImagem(String urlImagem) {
		this.urlImagem = urlImagem;
	}
	public String getUrlOferta() {
		return urlOferta;
	}
	public void setUrlOferta(String urlOferta) {
		this.urlOferta = urlOferta;
	}
	public Double getPrecoDe() {
		return precoDe;
	}
	public void setPrecoDe(Double precoDe) {
		this.precoDe = precoDe;
	}
	public Double getPrecoPor() {
		return precoPor;
	}
	public void setPrecoPor(Double precoPor) {
		this.precoPor = precoPor;
	}
	
	public String getPrecoPorStr(){
		return precoPor != null ? Utils.numberAsCurrency(this.precoPor) : "R$ 0,00";
	}
	
	@Override
	public int compareTo(Produto o) {
		int comp = 0;
		
		if (o.getPrecoPor() != null && this.getPrecoPor() != null){
			comp = o.getPrecoPor().compareTo(this.precoPor);
		} 
		if (comp == 0 && o.getId() != null && this.id != null ){
			comp = o.getId().compareTo(this.id);
		}
		return comp * (this.ordertype == OrderType.MENOR_PRECO ? -1 : 1);
	}
	
	public boolean validate(){
		return this.filtro.validade(this.getPrecoPor());
	}

}
