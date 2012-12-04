package br.com.wjaa.sackdroidweb.controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * 
 * @author wagneraraujo-sao
 *
 */
public class SacksRobot {

	
	
	private static final String URL_SEARCH = "http://busca.sephora.com.br/web/Resultado.aspx?q={0}";
	private static final DefaultHttpClient client = new DefaultHttpClient();
	private String [] keyWords;
	private Double valor;
	private FilterType filtertype;
	private OrderType ordertype;
	
	
	
	static{
		HttpHost proxy = new HttpHost("proxyfiliais.braspress.com.br", 3128);
		client.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
		client.getCredentialsProvider().setCredentials(AuthScope.ANY,new UsernamePasswordCredentials("wagneraraujo-sao","sao1234"));
	}
	
	public SacksRobot(String [] keyWords, Double valor, FilterType filtertype, OrderType ordertype){
		this.keyWords = keyWords;
		this.valor = valor;
		this.filtertype = filtertype;
		this.ordertype = ordertype;
	}

	public List<Produto> letsGo() {
		Cronometro.press();
		List<Produto> produtos = new ArrayList<Produto>();
		for (String word : keyWords) {
			try {
				
				HttpGet search = new HttpGet(URL_SEARCH.replace("{0}",scape(word)));
				HttpResponse response = client.execute(search);
				InputStream page = response.getEntity().getContent();
				Document doc = Jsoup.parse(page, "UTF-8", "web/Resultado.aspx?q=");
				Elements elements = doc.select(".wss_resultadoBuscaConteudoProduto a");
				produtos.addAll(makeProduto(elements));
				search.releaseConnection();
				
				/* Request para pegar o numero da ultima pagina */
				HttpPost postLastPage = new HttpPost(URL_SEARCH.replace("{0}",scape(word)));
				
				String viewStateValue = doc.select("#__VIEWSTATE").val();
				String eventValidation = doc.select("#__EVENTVALIDATION").val();
				
				List<NameValuePair> postParams = new ArrayList<NameValuePair>();
				postParams.add(new BasicNameValuePair("__VIEWSTATE", viewStateValue));
				postParams.add(new BasicNameValuePair("__EVENTTARGET", "lnkUltima"));
				postParams.add(new BasicNameValuePair("__EVENTARGUMENT",""));
				postParams.add(new BasicNameValuePair("__EVENTVALIDATION",eventValidation));
				postParams.add(new BasicNameValuePair("__LASTFOCUS",""));
				postParams.add(new BasicNameValuePair("ddlOrdenacao","relevancia"));
				postParams.add(new BasicNameValuePair("ddlPaginacao","32"));
				postParams.add(new BasicNameValuePair("tipoVisualizacao","grade"));
				UrlEncodedFormEntity entity = new UrlEncodedFormEntity(postParams);
				postLastPage.setEntity(entity);
				response = client.execute(postLastPage);
				
				Integer ultimaPagina = 1;
				if ( response.getEntity().getContent() != null ){
					
					ultimaPagina = getUltimaPagina(response.getEntity().getContent());
				}
				postLastPage.releaseConnection();
				/* fim trexo para pegar a ultima pagina*/
				
				//buscando todos as paginas dessa palavra chave.
				paginate(produtos, word, 1, ultimaPagina);
				
				
				postLastPage.releaseConnection();
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		produtos = applyFilter(filtertype, valor, produtos);
		Cronometro.press();
		
		return produtos;
	 }


	private void paginate(List<Produto> produtos, String word, int inicio,
			Integer fim) {
		Integer pagina = inicio + 1;
		while (fim >= pagina){
			try{
				HttpGet search = new HttpGet(URL_SEARCH.replace("{0}",scape(word))+"&pagina=" + pagina);
				HttpResponse response = client.execute(search);
				InputStream page = response.getEntity().getContent();
				Document doc = Jsoup.parse(page, "UTF-8", "web/Resultado.aspx?q=");
				Elements elements = doc.select(".wss_resultadoBuscaConteudoProduto a");
				produtos.addAll(makeProduto(elements));
				search.releaseConnection();
				System.out.println("Buscando pagina [" + pagina + "] de [" + fim + "]");
			}catch(Exception ex){
				ex.printStackTrace();
			}
			pagina ++;
		}
		
	}

	 @SuppressWarnings("unchecked")
	private List<Produto> applyFilter(FilterType filtetype, Double valor,
			List<Produto> produtos) {
		
		Collections.sort(produtos);
		 
		/*Filtrando os produtos pelo pre�o.*/
		Collection<Produto> produtosFiltrados = CollectionUtils.select(produtos, new Predicate() {
			
			@Override
			public boolean evaluate(Object o) {
				Produto p = (Produto)o;
				return p.validate();
			}
		});
		
		
		System.out.println("Resutlado final, produtos encontrados = " + produtos.size());
		System.out.println("produtos filtrados = " + produtosFiltrados.size());

		return new ArrayList<Produto>(produtosFiltrados);
	}


	private CharSequence scape(String word) {
		
		try {
			return URLEncoder.encode(word.trim(), 	"UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return "";
	}
	
	
	
	public List<Produto> makeProduto(Elements produto){
		
		List<Produto> produtos = new ArrayList<Produto>();
		for (int i = 0; i < produto.size(); i++){
			
			Element e = produto.get(i);
			if ( e.attr("id").startsWith("rptDados_resultadoBuscaLinkProduto") ){
				Produto p = new Produto(new Filter(filtertype, valor),ordertype);
				p.setUrlOferta(e.attr("href"));
				p.setId(Integer.valueOf(e.attr("href").replaceAll("[^0-9]", "")));
				p.setDescricao(getDescricao(e));
				p.setPrecoDe(getPrecoDe(e));
				p.setPrecoPor(getPrecoPor(e));
				p.setUrlImagem(getUrlImagem(e));
				produtos.add(p);
			}
		}
		return produtos;
		
	}


	private String getUrlImagem(Element e) {
		Elements es = e.getElementsByAttributeValue("class", "wss_resultadoBuscaConteudoVitrineImagemProduto");
		String src = es.attr("src");
		return src;
	}


	private Double getPrecoDe(Element e) {
		 String value = e.getElementsByClass("wss_resultadoBuscaConteudoVitrineDetalheProduto")
		 .get(0)
		 .getElementsByClass("wss_resultadoBuscaConteudoVitrineDetalheProdutoPrecoRegular")
		 .get(0)
		 .getElementsByTag("strong")
		 .text()
		 .replaceAll("[^0-9,]", "").replace(",", ".");
		return NumberUtils.isNumber(value)? Double.valueOf(value): 0.0d ;
	}


	private Double getPrecoPor(Element e) {
		String value = e.getElementsByClass("wss_resultadoBuscaConteudoVitrineDetalheProduto")
		 .get(0)
		 .getElementsByClass("wss_resultadoBuscaConteudoVitrineDetalheProdutoPrecoVenda")
		 .get(0)
		 .getElementsByTag("strong")
		 .text()
		 .replaceAll("[^0-9,]", "").replace(",", ".");
		return NumberUtils.isNumber(value)? Double.valueOf(value): 0.0d ;
	}


	private String getDescricao(Element e) {
		String descricao = e.getElementsByClass("wss_resultadoBuscaConteudoVitrineTituloMarca").text() + "<br/>" +
				e.getElementsByClass("wss_resultadoBuscaConteudoVitrineTituloProduto").text();
		return descricao;
	}
	
	
	private Integer getUltimaPagina(InputStream page) throws IOException{
		Document docMoved = Jsoup.parse(page, "UTF-8", "web/Resultado.aspx?q=");
		
		Pattern p = Pattern.compile("pagina=[0-9]+");
		Matcher m = p.matcher(docMoved.html());
		String paginaStr = "";
		while(m.find()){
		    paginaStr = m.group();
		}
		String pagina = paginaStr.replaceAll("[^0-9]", "");
		return NumberUtils.isNumber(pagina)? Integer.valueOf(pagina) : 0;
		
	}
	
	
}
