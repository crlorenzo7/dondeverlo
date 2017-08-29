package com.mediaforyou;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import com.mediaforyou.integracion.GestionIntegracion;
import com.mediaforyou.logica.GestionLogica;

public class Sistema {
	
	private GestionLogica GL;
	private GestionIntegracion GI;
	
	public Sistema() {
		GL = new GestionLogica();
		GI= new GestionIntegracion();
	}
	
	public ArrayList<JSONObject> buscarArticulosPorTitulo(String query,int n){
		ArrayList<JSONObject> datos=new ArrayList<JSONObject>();
		datos=GL.buscarArticulos(query,n);
		if(datos.isEmpty()) {
			datos = GI.buscar(query);
			
			if(!datos.isEmpty()) {
				for(JSONObject ficha:datos) {
					GL.guardarArticulo(ficha);
				}
				datos=GL.buscarArticulos(query,0);
			}
		}
		
		return datos;
	}
	
	public ArrayList<JSONObject> buscarMejores(int n,int skip){
		ArrayList<JSONObject> datos=GL.buscarMejores(n,skip);
		return datos;
	}
	
	public ArrayList<JSONObject> getPrecios(String id,String servicio,String calidad) {
		ArrayList<JSONObject> datos=GL.getPrecios(id,servicio,calidad);
		return datos;
	}
	
	public JSONObject getArticuloByID(String id) {
		JSONObject articulo=GL.getArticuloByID(id);
		return articulo;
	}
	
	public JSONObject getMuestraByID(String id) {
		JSONObject articulo = GL.getArticuloMuestraByID(id);
		return articulo;
	}
	
	public ArrayList<JSONObject> getMostrador(int tipo, String orden, String genero, String busqueda, int n){
		
		ArrayList<JSONObject> datos=GL.getQueryMostrador(tipo, orden, genero, busqueda, n);
		
		
		return datos;
	}
	
}
