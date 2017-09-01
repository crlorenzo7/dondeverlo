package com.mediaforyou.integracion.fuentes;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.json.*;
import com.google.gson.JsonObject;

public class Filmaffinity {

	public Filmaffinity() {
		
	}
	
	public String formatearSinopsis(String texto) {
		String sinopsis = texto.replace("(FILMAFFINITY)", "").replace("'", "&#39;").replaceAll("\"", "&#39;").replaceAll("\\u2026", "...").trim();
		sinopsis = sinopsis.replaceAll("\n", "").replace("\t","");
		return sinopsis;
	}
	
	public String getTituloEn(Element info) {
		Element tituloSpan = info;
		tituloSpan = info.select("span").first();
		String tituloEn="";
		if(tituloSpan !=null) {
			tituloEn = info.html().split("<span")[0].replaceAll("\\(Serie de TV\\)","").replaceAll("'", "&#39;").replaceAll("\"", "&#39;").trim();
			return tituloEn;
		}
		tituloEn = info.text().replaceAll("\\(TV Series\\)","").replaceAll("'", "&#39;").replaceAll("\"", "&#39;").trim();
		return tituloEn;
	}
	
	public ArrayList<String> getDirectores(String data) {
		ArrayList<String> directores=new ArrayList<String>();
		String[] directoresParse=data.split(",");
		
		for(int i=0;i<directoresParse.length && i<2;i++) {
			String director = directoresParse[i].replaceAll("'","&#39;").replaceAll("\"", "&#39;").trim();
			directores.add(director);
		}
		return directores;

	}
	
	public ArrayList<String> getGeneros(String data) {
		ArrayList<String> generos=new ArrayList<String>();
		
		String[] generosParse=data.replaceAll("\\|", ".").split("\\.");
		
		for(int i=0;i<generosParse.length;i++) {
			generos.add(generosParse[i].trim());
		}
		
		return generos;
		
	}
	
	public String getID(String titulo,int anio) {
		return (titulo.replace(" ","").trim()+"_"+anio).trim();
	}
	
	public JSONObject getFicha(String codigo) {
		JSONObject ficha=new JSONObject();
		try {//Jsoup.parse(new URL("http://filmaffinity.com/es/film"+codigo+".html").openStream(), "utf-8", "http://filmaffinity.com/es/film"+codigo+".html");
			Document doc = Jsoup.connect("http://filmaffinity.com/es/film"+codigo+".html").timeout(0).get();
			String titulo_es = (doc.select("h1#main-title").text().replaceAll("\\(Serie de TV\\)","").split("\\(")[0]).trim();

			
			ficha.put("titulo_es", titulo_es);
			
			Element tabla = doc.select("dl.movie-info").first();
			Elements lista_atributos = tabla.select("dt");
			Elements lista_valores = tabla.select("dd");
			
			ficha.put("duracion", "");
			
			for(int i = 0; i<lista_atributos.size();i++) {
				
				String tituloOr = new String("Título original".getBytes("UTF-8"), "UTF-8");
				String year = new String("Año".getBytes("UTF-8"), "UTF-8");
				String duration = new String("Duración".getBytes("UTF-8"), "UTF-8");
				String country = new String("País".getBytes("UTF-8"), "UTF-8");
				String direction = new String("Director".getBytes("UTF-8"), "UTF-8");
				String genre = new String("Género".getBytes("UTF-8"), "UTF-8");
				String overview = new String("Sinopsis".getBytes("UTF-8"), "UTF-8");
				
				if(lista_atributos.get(i).text().trim().equals(tituloOr) ){
					Element infoTitulo = lista_valores.get(i);
					ficha.put("titulo_en", this.getTituloEn(infoTitulo));
				}
				
				if(lista_atributos.get(i).text().trim().equals(year) ){
					ficha.put("anio", Integer.parseInt(lista_valores.get(i).text().trim()));
				}
				
				if(lista_atributos.get(i).text().trim().equals(duration) ){
					ficha.put("duracion", lista_valores.get(i).text().trim());
				}
				
				if(lista_atributos.get(i).text().trim().equals(country) ){
					ficha.put("pais", lista_valores.get(i).select("img").first().attr("title"));
				}
				
				if(lista_atributos.get(i).text().trim().equals(direction) ){
					String directoresData = lista_valores.get(i).text().trim();
					ficha.put("directores",this.getDirectores(directoresData));
				}
				
				if(lista_atributos.get(i).text().trim().equals(genre) ){
					String generosData = lista_valores.get(i).text().trim();
					ficha.put("generos",this.getGeneros(generosData));
				}
				
				if(lista_atributos.get(i).text().trim().equals(overview) ){
					ficha.put("sinopsis", this.formatearSinopsis(lista_valores.get(i).text().trim()));
				}
				
			}
			
			if(ficha.getString("duracion").equals("")) {
				ficha.put("duracion","60 min.");
			}
			
			ficha.put("id", this.getID(ficha.getString("titulo_en"),ficha.getInt("anio")));
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ficha;
	}
	
	public int getTipo(String titulo) {
		int tipo=0;
		
		String[] trozos = titulo.split("serie de tv");
		if(trozos.length>1) {
			tipo=1;
		}
		
		return tipo;
	}
	
	public ArrayList<JSONObject> buscarPorTitulo(String busqueda) {
		ArrayList<JSONObject> ids = new ArrayList<JSONObject>();
		String url="https://www.filmaffinity.com/es/search.php?stype=title&stext="+busqueda.replaceAll("\\s+","+");
		try {
			
			Document doc = Jsoup.connect(url).timeout(0).get();
			Element resul=doc.select("div.main-search-wrapper").first();
			
			
			if(resul!=null) {
				Elements resultados = doc.select("div.main-search-wrapper").first().select("div.se-it");
				for(int i=0;i<resultados.size() && i<10;i++) {
					Element e=resultados.get(i);
					JSONObject id=new JSONObject();
					id.put("anio", Integer.parseInt(e.select("div.ye-w").first().text().trim()));
					id.put("id", e.select("div.movie-card").first().attr("data-movie-id").trim());
					id.put("tipo", this.getTipo(e.select("div.movie-card").first().select("div.mc-info-container").first().select("div.mc-title").text().trim().toLowerCase()));
					ids.add(id);
				}
			}else {
				Element tabla = doc.select("dl.movie-info").first();
				if(tabla!=null) {
					Element rateMovieBox = doc.select("div.rate-movie-box").first();
					String id="";
					int anio=0;
					int tipo =0;
					
					id=rateMovieBox.attr("data-movie-id").trim();
					
					String titulo_es = doc.select("h1#main-title").text().toLowerCase().trim();
					if(titulo_es!=null) {
						tipo=this.getTipo(titulo_es);
					}
					
					Elements lista_atributos = tabla.select("dt");
					Elements lista_valores = tabla.select("dd");
					
					
					
					for(int i = 0; i<lista_atributos.size();i++) {

						String year = new String("Año".getBytes("UTF-8"), "UTF-8");
						
						if(lista_atributos.get(i).text().trim().equals(year) ){
							anio=Integer.parseInt(lista_valores.get(i).text().trim());
						}
						
						
						
					}
					JSONObject idp=new JSONObject();
					idp.put("anio", anio);
					idp.put("id", id);
					idp.put("tipo", tipo);
					ids.add(idp);
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
