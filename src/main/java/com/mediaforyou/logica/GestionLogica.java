package com.mediaforyou.logica;

import com.googlecode.objectify.ObjectifyService;
import com.mediaforyou.logica.modelos.*;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;


public class GestionLogica {

	public GestionLogica() {
		
	}
	
	public boolean filtrarArticulosPorTitulo(String query,Articulo a) {
		boolean valido=false;

		if(a.getTitulo_en().toLowerCase().equals(query.toLowerCase()) || a.getTitulo().toLowerCase().equals(query.toLowerCase()) || a.getTitulo().toLowerCase().split(query.toLowerCase()).length>1 || a.getTitulo_en().toLowerCase().split(query.toLowerCase()).length>1 || a.getEtiquetas().toLowerCase().split(query.toLowerCase()).length>1 ) {
			valido=true;
		}
		return valido;
	}
	
	public ArrayList<JSONObject> getPrecios(String id,String servicio,String calidad) {
		ArrayList<JSONObject> datos=new ArrayList<JSONObject>();
		
		if(servicio.equals("stream")) {
			List<Precio> listaStream=ObjectifyService.ofy().load().type(Precio.class).filter("idArticulo",id).filter("servicio","stream").list();
			datos=quitarStreamRepetidos(listaStream);
		}else {
			List<Precio> listaACSD = ObjectifyService.ofy().load().type(Precio.class).filter("idArticulo",id).filter("servicio !=","stream").filter("calidad",calidad).list();
			datos=quitarAlquilerRepetidos(listaACSD);
		}
		
		for(int i=0;i<datos.size();i++) {
			JSONObject precio = datos.get(i);
			precio.put("imagen", this.getImagenProveedor(precio.getString("empresa")));
		}
		
		
		
		return datos;
	}
	
	
	
	public ArrayList<ArrayList<JSONObject>> getCapitulosByIDA(String ida) {
		ArrayList<ArrayList<JSONObject>> capitulos=new ArrayList<ArrayList<JSONObject>>();
		
		List<Capitulo> caps=ObjectifyService.ofy().load().type(Capitulo.class).filter("idArticulo",ida).order("-temporada").list();
		int nt=caps.get(0).getTemporada();
		for(int i=1;i<=nt;i++) {
			ArrayList<JSONObject> temporada=getCapitulosTemporadaByIDA(ida,i);
			capitulos.add(temporada);
		}
		return capitulos;
	}
	
	public ArrayList<JSONObject> getCapitulosTemporadaByIDA(String id,int i){
		ArrayList<JSONObject> temporada = new ArrayList<JSONObject>();
		List<Capitulo> capitulos=ObjectifyService.ofy().load().type(Capitulo.class).filter("idArticulo",id).filter("temporada",i).order("orden").list();
		for(int j=0;j<capitulos.size();j++) {
			Capitulo c=capitulos.get(j);
			temporada.add(c.toJSON());
		}
		return temporada;
	}
	

	
	public String getImagenActorByName(String nombre) {
		String imagen="";
		Actor a=ObjectifyService.ofy().load().type(Actor.class).id(nombre).now();
		imagen=a.getImagen();
		return imagen;
	}
	
	public ArrayList<JSONObject> getRepartoByIDA(String ida){
		ArrayList<JSONObject> reparto=new ArrayList<JSONObject>();
		List<Interpretacion> trabajos=ObjectifyService.ofy().load().type(Interpretacion.class).filter("idArticulo",ida).order("orden").list();
		for(int i=0;i<trabajos.size();i++) {
			JSONObject trabajo=trabajos.get(i).toJSON();
			trabajo.put("imagen", getImagenActorByName(trabajo.getString("nombreActor")));
			reparto.add(trabajo);
		}
		return reparto;
	}
	
	public ArrayList<JSONObject> quitarAlquilerRepetidos(List<Precio> streams){
		ArrayList<JSONObject> streamsDef=new ArrayList<JSONObject>();
		
		for(int i=0;i<streams.size();i++) {
			Precio p=streams.get(i);
			if(!p.getEmpresa().equals("")) {
				JSONObject precio=p.toJSON();
				
				for(int j=(i+1);j<streams.size();j++) {
					Precio pr=streams.get(j);
					if(pr.getEmpresa().equals(p.getEmpresa())) {
						if(p.getServicio().equals("alquiler")) {
							precio.put("precioCompra", pr.getPrecio());
						}else {
							precio.put("precioAlquiler", pr.getPrecio());
						}
						
						streams.remove(j);
					}
				}
				streamsDef.add(precio);
			}
		}
		
		return streamsDef;
	}
	
	public ArrayList<JSONObject> quitarStreamRepetidos(List<Precio> streams){
		ArrayList<JSONObject> alquilerDef=new ArrayList<JSONObject>();
		
		for(int i=0;i<streams.size();i++) {
			Precio p=streams.get(i);
			if(!p.getEmpresa().equals("")) {
				JSONObject precio=p.toJSON();
				alquilerDef.add(precio);
				for(int j=(i+1);j<streams.size();j++) {
					Precio pr=streams.get(j);
					if(pr.getEmpresa().equals(p.getEmpresa())) {
						streams.remove(j);
					}
				}
			}
		}
		
		return alquilerDef;
	}
	
	public String getImagenProveedor(String empresa) {
		String imagen="";
		
		Proveedor p=ObjectifyService.ofy().load().type(Proveedor.class).id(empresa).now();
		imagen=p.getImagenGrande();
		return imagen;
	}
	
	public JSONObject getPreciosByIDA(String ida) {
		JSONObject precios=new JSONObject();
		List<Precio> listaStream=ObjectifyService.ofy().load().type(Precio.class).filter("idArticulo",ida).filter("servicio","stream").list();
		List<Precio> listaACSD = ObjectifyService.ofy().load().type(Precio.class).filter("idArticulo",ida).filter("servicio !=","stream").filter("calidad","sd").list();
		List<Precio> listaACHD = ObjectifyService.ofy().load().type(Precio.class).filter("idArticulo",ida).filter("servicio !=","stream").filter("calidad","hd").list();
		
		precios.put("stream", quitarStreamRepetidos(listaStream));
		precios.put("compraHD", quitarAlquilerRepetidos(listaACHD));
		precios.put("compraSD",quitarAlquilerRepetidos(listaACSD));
		
		JSONArray stream=precios.getJSONArray("stream");
		JSONArray compraHD=precios.getJSONArray("compraHD");
		JSONArray compraSD=precios.getJSONArray("compraSD");
		
		for(int i=0;i<stream.length();i++) {
			JSONObject precio = stream.getJSONObject(i);
			precio.put("imagen", this.getImagenProveedor(precio.getString("empresa")));
		}
		
		for(int i=0;i<compraHD.length();i++) {
			JSONObject precio = compraHD.getJSONObject(i);
			precio.put("imagen", this.getImagenProveedor(precio.getString("empresa")));
		}
		
		for(int i=0;i<compraSD.length();i++) {
			JSONObject precio = compraSD.getJSONObject(i);
			precio.put("imagen", this.getImagenProveedor(precio.getString("empresa")));
		}
		
		return precios;
	}
	
	public ArrayList<JSONObject> buscarMejores(int n,int skip){
		ArrayList<JSONObject> articulos=new ArrayList<JSONObject>();
		List<Articulo> art=ObjectifyService.ofy().load().type(Articulo.class).offset(skip).limit(n).order("-calificacion_media").list();
		
		for(int i=0;i<art.size();i++) {
			Articulo a=art.get(i);
			JSONObject aJSON = a.toJSON();
			aJSON.put("precios", getPreciosByIDA(aJSON.getString("id")));
			articulos.add(aJSON);
		}
		
		return articulos;
	}
	
	public ArrayList<JSONObject> buscarArticulos(String query,int n){
		ArrayList<JSONObject> articulos=new ArrayList<JSONObject>();
		List<Articulo> art=new ArrayList<Articulo>();
		if(n==1) {
			art=ObjectifyService.ofy().load().type(Articulo.class).order("-fechaActualizacion").limit(21).list();
		}else {
			art=ObjectifyService.ofy().load().type(Articulo.class).order("-fechaActualizacion").list();
		}
		
		for(int i=0;i<art.size();i++) {
			Articulo a=art.get(i);
			if(filtrarArticulosPorTitulo(query,a)) {
				JSONObject aJSON = a.toJSON();
				aJSON.put("reparto", getRepartoByIDA(aJSON.getString("id")));
				aJSON.put("precios", getPreciosByIDA(aJSON.getString("id")));
				if(aJSON.getInt("tipo")==1) {
					//aJSON.put("capitulos", getCapitulosByIDA(aJSON.getString("id")));
				}
				articulos.add(aJSON);
			}
			
		}
		
		return articulos;
	}
	
	public ArrayList<JSONObject> buscarArticulosPortada(){
		ArrayList<JSONObject> datos=new ArrayList<JSONObject>();
		List<Articulo> art=ObjectifyService.ofy().load().type(Articulo.class).order("-fechaActualizacion").list();
		if(art.size()>7) {
			for(int i=0;i<art.size();i++) {
				Articulo a=art.get(i);
				datos.add(a.toJSON());
			}
		}
		return datos;
	}
	
	public void guardarArticulo(JSONObject articulo) {
		Articulo ar=new Articulo(articulo);
		ar.save();

		guardarReparto(articulo.getString("id"),articulo.getJSONArray("reparto"));
		guardarPrecios(articulo.getString("id"),articulo.getJSONArray("precios"));
		if(articulo.getInt("tipo")==1) {
			//guardarCapitulos(articulo.getString("id"),articulo.getJSONArray("capitulos"));
		}
	}
	
	public void guardarCapitulos(String id,JSONArray caps) {
		for(int i=0;i<caps.length();i++) {
			JSONObject cap=caps.getJSONObject(i);
			guardarCapitulo(id,cap);
		}
	}
	
	public void guardarCapitulo(String id,JSONObject c) {
		Capitulo cap=new Capitulo(id,c);
		cap.save();
	}
	
	public void guardarReparto(String id,JSONArray reparto) {
		
		for(int i=0;i<reparto.length();i++) {
			JSONObject actor=reparto.getJSONObject(i);
			guardarActor(actor);
			guardarInterpretacion(id,actor);
		}
	}
	
	public void guardarActor(JSONObject actor) {
		Actor ac=new Actor(actor);
		ac.save();
	}
	
	public void guardarInterpretacion(String id,JSONObject actor) {
		Interpretacion inter=new Interpretacion(id,actor);
		inter.save();
	}
	
	public void guardarPrecio(String id,JSONObject precio) {
		Precio p=new Precio(id,precio);
		p.save();
	}
	
	public void guardarPrecios(String id,JSONArray precios) {
		for(int i=0;i<precios.length();i++) {
			JSONObject precio=precios.getJSONObject(i);
			guardarPrecio(id,precio);
		}
	}
	
	public List<Articulo> filtrarPorBusqueda(List<Articulo> ar,String query){
		List<Articulo> resultados=new ArrayList<Articulo>();
		for(int i=0;i<ar.size();i++) {
			Articulo a=ar.get(i);
			if(a.getGenero().toLowerCase().split(query.toLowerCase()).length>1 || a.getTitulo_en().toLowerCase().equals(query.toLowerCase()) || a.getTitulo().toLowerCase().equals(query.toLowerCase()) || a.getTitulo().toLowerCase().split(query.toLowerCase()).length>1 || a.getTitulo_en().toLowerCase().split(query.toLowerCase()).length>1 || a.getEtiquetas().toLowerCase().split(query.toLowerCase()).length>1 ) {
				resultados.add(a);
			}
		}
		
		return resultados;
	}
	
	public ArrayList<JSONObject> getQueryMostrador(int tipo,String orden,String genero,String busqueda,int n) {
		ArrayList<JSONObject> resdef=new ArrayList<JSONObject>();
		List<Articulo> resultados=new ArrayList<Articulo>();
	
		if(tipo==-1) {
			if(n>0) {
				if(orden.equals("estrenos")) {
					resultados=ObjectifyService.ofy().load().type(Articulo.class).limit(20).offset((20*n)).order("-fechaActualizacion").list();
				}
				if(orden.equals("valoracion")) {
					resultados=ObjectifyService.ofy().load().type(Articulo.class).limit(20).offset((20*n)).order("-calificacion_media").list();
				}
				if(orden.equals("visitas")) {
					resultados=ObjectifyService.ofy().load().type(Articulo.class).limit(20).offset((20*n)).order("-nvistas").list();
				}
			}else {
				if(orden.equals("estrenos")) {
					resultados=ObjectifyService.ofy().load().type(Articulo.class).limit(20).order("-fechaActualizacion").list();
				}
				if(orden.equals("valoracion")) {
					resultados=ObjectifyService.ofy().load().type(Articulo.class).limit(20).order("-calificacion_media").list();
				}
				if(orden.equals("visitas")) {
					resultados=ObjectifyService.ofy().load().type(Articulo.class).limit(20).order("-nvistas").list();
				}
			}
		}else {
			if(n>0) {
				if(orden.equals("estrenos")) {
					resultados=ObjectifyService.ofy().load().type(Articulo.class).filter("tipo",tipo).limit(20).offset((20*n)).order("-fechaActualizacion").list();
				}
				if(orden.equals("valoracion")) {
					resultados=ObjectifyService.ofy().load().type(Articulo.class).filter("tipo",tipo).limit(20).offset((20*n)).order("-calificacion_media").list();
				}
				if(orden.equals("visitas")) {
					resultados=ObjectifyService.ofy().load().type(Articulo.class).filter("tipo",tipo).limit(20).offset((20*n)).order("-nvistas").list();
				}
			}else {
				if(orden.equals("estrenos")) {
					resultados=ObjectifyService.ofy().load().type(Articulo.class).filter("tipo",tipo).limit(20).order("-fechaActualizacion").list();
				}
				if(orden.equals("valoracion")) {
					resultados=ObjectifyService.ofy().load().type(Articulo.class).filter("tipo",tipo).limit(20).order("-calificacion_media").list();
				}
				if(orden.equals("visitas")) {
					resultados=ObjectifyService.ofy().load().type(Articulo.class).filter("tipo",tipo).limit(20).order("-nvistas").list();
				}
			}
		}
		
		if(!busqueda.equals("")) {
			resultados=filtrarPorBusqueda(resultados,busqueda);
		}
		if(!genero.equals("todos")) {
			resultados=filtrarPorBusqueda(resultados,genero);
		}
		
		for(Articulo a:resultados) {
			resdef.add(a.toJSON());
		}
			
		return resdef;
	}
	
	public JSONObject getArticuloByID(String id) {
		Articulo a=ObjectifyService.ofy().load().type(Articulo.class).id(id).now();
		JSONObject articulo=a.toJSON();
		
		articulo.put("reparto", getRepartoByIDA(articulo.getString("id")));
		articulo.put("precios", getPreciosByIDA(articulo.getString("id")));
		/*if(articulo.getInt("tipo")==1) {
			articulo.put("capitulos", getCapitulosByIDA(articulo.getString("id")));
		}*/
		
		return articulo;
	}
	
	public JSONObject getArticuloMuestraByID(String id) {
		Articulo a=ObjectifyService.ofy().load().type(Articulo.class).id(id).now();
		JSONObject articulo=a.toJSON();
		
		articulo.put("precios", getPreciosByIDA(articulo.getString("id")));
		
		return articulo;
	}
	
	
}
