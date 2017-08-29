package com.mediaforyou.integracion;

import java.util.ArrayList;

import org.json.JSONObject;

import com.mediaforyou.integracion.fuentes.*;
import com.mediaforyou.misc.Texto;

public class GestionIntegracion {

	private Fanart far;
	private Youtube yt;
	private Filmaffinity fa;
	private Trakt ttv;
	private Justwatch jw;
	private Texto t;
	
	public GestionIntegracion() {
		far = new Fanart();
		yt = new Youtube();
		fa = new Filmaffinity();
		ttv = new Trakt();
		t=new Texto();
		jw=new Justwatch();
	}
	
	public ArrayList<JSONObject> buscar(String query) {
		ArrayList<JSONObject> idsFA = fa.buscarPorTitulo(query);
		ArrayList<JSONObject> idsTTV =ttv.buscarPorTitulo(query);
		
		ArrayList<Integer> toerase=new ArrayList<Integer>();
		
		
		for(JSONObject s:idsFA) {
			boolean encontrado=false;
			for(int i=0;i<idsTTV.size() && !encontrado;i++){
				JSONObject idt=idsTTV.get(i);
				if(s.getInt("anio")==idt.getInt("anio") && s.getInt("tipo")==idt.getInt("tipo")){
					s.put("idTTV",idt.getString("id"));
					encontrado=true;
					idt.put("anio",0);
				}
			}
			if(!encontrado){
				int index=idsFA.indexOf(s);
				toerase.add(index);
			}
		}
		
		int contador=0;
		for(int j:toerase){
			idsFA.remove((j-contador));
			contador++;
		}
		
		
		
		ArrayList<JSONObject> fichas = new ArrayList<JSONObject>();
		
		for(JSONObject s:idsFA) {
			JSONObject fichaFA = fa.getFicha(s.getString("id"));
			JSONObject fichaTTV = ttv.getFicha(s.getInt("tipo"), s.getString("idTTV"));
			JSONObject imagenes = far.getImagenes(fichaTTV.getString("imdb"), s.getInt("tipo"));
			ArrayList<JSONObject> precios = jw.buscarPrecios(fichaFA.getString("titulo_es"),fichaFA.getString("titulo_en"), fichaFA.getInt("anio"));
			if(!precios.isEmpty()) {
				if(!imagenes.has("error")){
					String titulo = t.quitarTildes(fichaFA.getString("titulo_es"));
					
					fichaFA.put("trailer", yt.getVideo("trailer " + titulo + "castellano"));
					fichaFA.put("calificacion",fichaTTV.getString("calificacion"));
					fichaFA.put("reparto",fichaTTV.getJSONArray("reparto"));
					fichaFA.put("imdb",fichaTTV.getString("imdb"));
					
					fichaFA.put("fondo",imagenes.getString("fondo"));
					fichaFA.put("caratula",imagenes.getString("caratula"));
					if(s.getInt("tipo")==1){
						fichaFA.put("capitulos",fichaTTV.getJSONArray("capitulos"));
					}
					fichaFA.put("tipo",s.getInt("tipo"));
					fichaFA.put("idFA",s.getString("id"));
					fichaFA.put("idTTV", s.getString("idTTV"));
					fichaFA.put("precios", precios);
					
					fichas.add(fichaFA);
				}
			}
		}
		
		return fichas;
	}
}
