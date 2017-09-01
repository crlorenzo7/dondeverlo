package com.mediaforyou.integracion.fuentes;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Trakt {
	
	String IMAGE_ROOT = "https://walter.trakt.tv/images/";

	public Trakt(){
		
	}
	
	public boolean isValidImage(String img) {
		String[] trozos = img.split(IMAGE_ROOT);
		if(trozos.length==2) {
			return true;
		}
		return false;
	}
	
	public ArrayList<JSONObject> getTemporada(String codigo,int i){
		ArrayList<JSONObject> temporada=new ArrayList();
		
		String url="https://trakt.tv/shows/"+codigo+"/seasons/"+i;
		
		Document doc;
		try {
			doc = Jsoup.connect(url).timeout(0).get();;
			
			Elements lista_capitulos = doc.select("div#seasons-episodes-sortable").first().select("div.row");
			
			for(int j =0;j<lista_capitulos.size();j++) {
				JSONObject capitulo=new JSONObject();
				
				String dsn=lista_capitulos.get(j).attr("data-season-number").trim();
				if(dsn!="") {
					capitulo.put("titulo", lista_capitulos.get(j).select("meta[itemprop=name]").attr("content").trim());
					capitulo.put("imagen", lista_capitulos.get(j).select("meta[itemprop=image]").attr("content").trim());
					capitulo.put("temporada", Integer.parseInt(dsn));
					capitulo.put("orden", Integer.parseInt(lista_capitulos.get(j).attr("data-number").trim()));
					
					if(!(capitulo.getString("titulo").isEmpty()) && !(capitulo.getString("imagen").isEmpty()) ) {
						temporada.add(capitulo);
					}
				}
				
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return temporada;
	}
	
	public ArrayList<JSONObject> getCapitulos(String codigo){
		ArrayList<JSONObject> capitulos=new ArrayList();
		
		String url="https://trakt.tv/shows/"+codigo;
		
		Document doc;
		try {
			doc = Jsoup.connect(url).timeout(0).get();
			
			int ntemporadas = Integer.parseInt(doc.select("h2#seasons").first().select("span.season-count").first().attr("data-all-count").trim());
			
			for(int i=1;i<=ntemporadas;i++) {
				ArrayList<JSONObject> temporada = this.getTemporada(codigo,i);
				for(int j=0;j<temporada.size();j++) {
					capitulos.add(temporada.get(j));
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return capitulos;
	}
	
	public ArrayList<JSONObject> getReparto(Document doc){
		
		ArrayList<JSONObject> reparto=new ArrayList<JSONObject>();
		Element container = doc.select("section#info-wrapper").first();
		if(container!=null) {
			Element continerActores =container.select("div#actors").first();
			if(continerActores!=null) {
				Elements lista_actores = container.select("div#actors").first().select("li");
				
				for(int i=0;i<lista_actores.size();i++) {
					JSONObject actor=new JSONObject();
					
					String imagen=lista_actores.get(i).select("meta[itemprop=image]").first().attr("content").trim();
					if(this.isValidImage(imagen)) {
						actor.put("nombre", lista_actores.get(i).select("h4.name").first().text().trim());
						actor.put("personaje", lista_actores.get(i).select("h4.character").first().text().trim());
						actor.put("imagen", imagen+".webp");
						actor.put("orden", (i+1));
						reparto.add(actor);
					}
				}
			}
			
		}
		return reparto;
	}
	
	public String getCalificacion(Document doc) {
		if(doc.select("div#summary-ratings-wrapper").first()!=null) {
			if(doc.select("div#summary-ratings-wrapper").first().select("div.rating").first()!=null) {
				Double calificacion = Double.parseDouble(doc.select("div#summary-ratings-wrapper").first().select("div.rating").first().text().replace("%","").trim());
				calificacion=calificacion/10;
				return calificacion.toString();
			}else {
				return "0.0";
			}
		}else {
			return "0.0";
		}
	}
	
	public String getImdb(Document doc) {
		String imdb="";
		boolean encontrado = false;
		if(doc.select("ul.external").first()!=null) {
			Elements lista = doc.select("ul.external").first().select("a");
			for(int i=0;i<lista.size() && !encontrado;i++) {
				Element a=lista.get(i);
				if(a.text().trim().equals("IMDB")) {
					String[] trozos= a.attr("href").split("tt");
					if(trozos.length>=3) {
						imdb=(a.attr("href").split("tt")[2]).trim();
						encontrado=true;
					}
				}
			}
		}
		
		return "tt"+imdb;
	}
	
	public String getTVDB(Document doc) {
		String imdb="";
		boolean encontrado = false;
		Elements lista = doc.select("ul.external").first().select("a");
		for(int i=0;i<lista.size() && !encontrado;i++) {
			Element a=lista.get(i);
			if(a.text().trim().equals("TVDB")) {
				imdb=(a.attr("href").split("=")[1]).trim();
				encontrado=true;
			}
		}
		
		return imdb;
	}
	
	public JSONObject getFichaPorTitulo(int seccion,int anio,String titulo_en) {
		
		String url="https://trakt.tv/search/?query="+titulo_en.replaceAll("\\s+","+");
		JSONObject ficha=new JSONObject();
		try {
			
			Document doc = Jsoup.connect(url).timeout(0).get();
			Elements resultados = doc.select("div.fanarts").first().select("div.grid-item");
			boolean encontrado=false;
			for(int i=0;i<resultados.size() && !encontrado;i++) {
				Element e=resultados.get(i);
				JSONObject id=new JSONObject();
				//id.put("anio", Integer.parseInt(e.select("a").first().select("div.fanart").first().select("div.titles").first().select("h3").first().select("span.year").first().text().trim()));
				Elements enlaces=e.select("a");
				
				for(Element pos:enlaces) {
					if(pos.select("div.fanart").first()!=null && !pos.select("div.fanart").first().select("div.titles").first().select("h3").first().select("span.year").first().text().trim().equals("")) {
						
						int an=Integer.parseInt(pos.select("div.fanart").first().select("div.titles").first().select("h3").first().select("span.year").first().text().trim());
						String tit=pos.select("div.fanart").first().select("div.titles").first().select("meta").first().attr("content").trim();
						if(an==anio && titulo_en.equals(tit) ) {
							String code ="";
							if(seccion==1) {
								code=e.attr("data-show-id").trim();
							}else {
								code=e.attr("data-movie-id").trim();
							}
							encontrado=true;
							ficha=this.getFicha(seccion, code);
						}
						
					}
				}
			}
			
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(!ficha.has("imdb")) {
			ficha.put("error", "invalido");
		}
		return ficha;
	}
	
	public JSONObject getFicha(int seccion,String code) {
		String tipoA="movies";
		if(seccion==1) {
			tipoA="shows";
		}
		String url ="https://trakt.tv/"+tipoA+"/"+code;
		
		JSONObject ficha=new JSONObject();
		Document doc;
		try {
			doc = Jsoup.connect(url).timeout(0).get();
			ficha.put("calificacion", this.getCalificacion(doc));
			ficha.put("reparto", this.getReparto(doc));
			if(seccion==0) {
				ficha.put("imdb", this.getImdb(doc));
			}else {
				ficha.put("imdb", this.getTVDB(doc));
			}
			if(tipoA.equals("shows")) {
				//ficha.put("capitulos", this.getCapitulos(code));
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return ficha;
	}
	
	public int getTipo(String t) {
		int tipo=0;
		if(t.equals("show")) {
			tipo=1;
		}
		return tipo;
	}
	
	public ArrayList<JSONObject> buscarPorTitulo(String query){
		ArrayList<JSONObject> ids = new ArrayList<JSONObject>();
		String url="https://trakt.tv/search/?query="+query.replaceAll("\\s+","+");
		try {
			
			Document doc = Jsoup.connect(url).timeout(0).get();
			Elements resultados = doc.select("div.fanarts").first().select("div.grid-item");
			
			for(Element e:resultados) {
				JSONObject id=new JSONObject();
				//id.put("anio", Integer.parseInt(e.select("a").first().select("div.fanart").first().select("div.titles").first().select("h3").first().select("span.year").first().text().trim()));
				Elements enlaces=e.select("a");
				
				for(Element pos:enlaces) {
					if(pos.select("div.fanart").first()!=null && !pos.select("div.fanart").first().select("div.titles").first().select("h3").first().select("span.year").first().text().trim().equals("")) {
						
						id.put("anio", Integer.parseInt(pos.select("div.fanart").first().select("div.titles").first().select("h3").first().select("span.year").first().text().trim()));
						id.put("tipo", this.getTipo(e.attr("data-type").trim()));
						if(id.getInt("tipo")==1) {
							id.put("id", e.attr("data-show-id").trim());
						}else {
							id.put("id", e.attr("data-movie-id").trim());
						}
						ids.add(id);
					}
				}
			}
			
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return ids;
	}
}
