package com.mediaforyou.integracion.fuentes;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class Justwatch {
	
	private String URLBASE="https://apis.justwatch.com/content/titles/es_ES/popular?body=%7B%";
	private ArrayList<String> empresas=new ArrayList();

	public Justwatch() {
		empresas.add("google");
		empresas.add("itunes");
		empresas.add("playstation");
		empresas.add("wuaki");
		empresas.add("netflix");
		empresas.add("microsoft");
		empresas.add("hbo");
		empresas.add("amazon");
		empresas.add("movistar");
	}
	
	public String realizarPeticion(String url,String metodo) {
		String respuesta="";
		
		URL u;
		try {
			u = new URL(url);
			HttpURLConnection conn = (HttpURLConnection) u.openConnection();
			conn.setConnectTimeout(180*1000);
            conn.setReadTimeout(180*1000);
		    conn.setRequestMethod(metodo);
		    BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		    String line;
		    while ((line = rd.readLine()) != null) {
		       respuesta+=line;
		    }
		    rd.close();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return respuesta;
	}
	
	public String getServicio(String servicio) {
		String s="stream";
		if(servicio.equals("rent")) {
			s="alquiler";
		}
		if(servicio.equals("buy")) {
			s="compra";
		}
		
		return s;
	}
	
	public String getEmpresa(String url) {
		String e="none";
		boolean encontrado=false;
		for(int i=0;i<empresas.size() && !encontrado;i++) {
			String s=empresas.get(i);
			String[] trozos=url.split(s);
			
			if(trozos.length==2){
				encontrado=true;
				e=s;
			}
		}
		
		return e;
	}
	
	public ArrayList<JSONObject> buscarPrecios(String titulo,String to,int anio) {
		ArrayList<JSONObject> precios=new ArrayList();
		String url=URLBASE+"22age_certifications%22:null,"+
				   "%22content_types%22:null,"+
				   "%22genres%22:null,"+
				   "%22languages%22:null,"+
				   "%22max_price%22:null,"+
				   "%22min_price%22:null,"+
				   "%22presentation_types%22:null,"+
				   "%22providers%22:null,"+
				   "%22query%22:%22"+titulo.replaceAll("\\s+","+")+"%22,"+
				   "%22release_year_from%22:"+(anio-2)+","+
				   "%22release_year_until%22:"+anio+","+
				   "%22scoring_filter_types%22:null%7D";
		
		System.out.println(url);
	
		String respuesta = this.realizarPeticion(url, "GET");
		
		JSONObject json = new JSONObject(respuesta);

		JSONArray resultados=json.getJSONArray("items");
		
		for(int i=0;i<resultados.length();i++) {
			JSONObject r=resultados.getJSONObject(i);
			if((r.getString("original_title").toLowerCase().replaceAll("\\s+","")).equals(to.toLowerCase().replaceAll("\\s+",""))) {
				ArrayList<JSONObject> subcripcion=new ArrayList();
				JSONObject alquiler = new JSONObject();
				JSONObject compra=new JSONObject();
				
				if(!r.isNull("offers")) {
					JSONArray ofertas = r.getJSONArray("offers");
					
					for(int j=0;j<ofertas.length();j++) {
						JSONObject oferta=ofertas.getJSONObject(j);
						
						JSONObject precio=new JSONObject();
						precio.put("servicio",this.getServicio(oferta.getString("monetization_type")));
						precio.put("calidad", oferta.getString("presentation_type"));
						precio.put("url", oferta.getJSONObject("urls").getString("standard_web"));
						precio.put("empresa", this.getEmpresa(oferta.getJSONObject("urls").getString("standard_web")));
						
						if(!precio.getString("empresa").equals("none")) {
							if(!precio.getString("servicio").equals("stream")) {
								precio.put("precio", oferta.getDouble("retail_price"));
								precios.add(precio);
							}else {
								precio.put("precio", 0);
								precios.add(precio);
							}
						}
						
					}
				}
				
			}
		}
		
		
		return precios;
	}
}
