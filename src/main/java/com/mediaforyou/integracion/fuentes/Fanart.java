package com.mediaforyou.integracion.fuentes;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Fanart {

	private String apikey="9df56df7c2120d11dc24bdeffffd2988";
	private String urlbase="http://webservice.fanart.tv/v3/";
	public Fanart() {
		
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
	
	public JSONObject getImagenes(String code,int tipo) {
		JSONObject imagenes = new JSONObject();
		String tipoS="movies";
		if(tipo==1) {
			tipoS="tv";
		}
		String url=urlbase+tipoS+"/"+code+"?api_key="+apikey;
		
		String respuesta = this.realizarPeticion(url, "GET");
		
		JSONObject json = new JSONObject(respuesta);

		JSONArray posters = new JSONArray();
		JSONArray fondos = new JSONArray();
		
		
		if(!json.has("error message")) {
			if(tipoS.equals("movies")) {
				posters=null;
				fondos=null;
				if(json.has("movieposter")) {
					posters = json.getJSONArray("movieposter");
				}
				if(json.has("moviebackground")) {
					fondos = json.getJSONArray("moviebackground");
				}
			}else {
				posters=null;
				fondos=null;
				if(json.has("tvposter")) {
					posters = json.getJSONArray("tvposter");
				}
				if(json.has("showbackground")) {
					fondos = json.getJSONArray("showbackground");
				}
			}
				
			if(fondos!=null){
				JSONObject fondo = fondos.getJSONObject(0);
				imagenes.put("fondo", fondo.getString("url"));
			}else {
				imagenes.put("fondo", "");
			}
			
			if(posters!=null){
				JSONObject poster = posters.getJSONObject(0);
				imagenes.put("caratula", poster.getString("url"));
			}else {
				imagenes.put("caratula", "");
			}
		}else {
			imagenes.put("error", "muchos");
		}
		
		return imagenes;
	}
}
