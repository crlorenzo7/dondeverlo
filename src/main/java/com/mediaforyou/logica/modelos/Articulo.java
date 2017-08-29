package com.mediaforyou.logica.modelos;

import java.util.Date;



import org.json.JSONArray;
import org.json.JSONObject;

import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.annotation.*;
import com.mediaforyou.misc.Texto;

@Entity
public class Articulo {
	@Id String id;
	@Index String titulo;
	@Index String titulo_en;
	@Index int anio;
	String sinopsis;
	String trailer;
	String genero;
	String duracion;
	@Index  double calificacion_media;
	@Index int nfavs;
	@Index int nvistas;
	String etiquetas;
	@Index  Date fechaActualizacion;
	String caratula;
	String fondo;
	String codeFA;
	String codeTTV;
	String director;
	@Index int tipo;
	
	public Articulo(String id, String titulo, String titulo_en, int anio, String sinopsis, String trailer,
			String genero, String duracion, double calificacion_media, String etiquetas,
			Date fechaActualizacion, String caratula, String fondo, String codeFA, String codeTTV, String director,
			int tipo) {

		this.id = id;
		this.titulo = titulo;
		this.titulo_en = titulo_en;
		this.anio = anio;
		this.sinopsis = sinopsis;
		this.trailer = trailer;
		this.genero = genero;
		this.duracion = duracion;
		this.calificacion_media = calificacion_media;
		this.nfavs = 0;
		this.nvistas = 0;
		this.etiquetas = etiquetas;
		this.fechaActualizacion = fechaActualizacion;
		this.caratula = caratula;
		this.fondo = fondo;
		this.codeFA = codeFA;
		this.codeTTV = codeTTV;
		this.director = director;
		this.tipo = tipo;
	}
	
	public String unificarGeneros(JSONArray datos) {
		Texto t=new Texto();
		String generos="";
		for(int i=0;i<datos.length();i++) {
			if(i==datos.length()-1) {
				generos+=t.quitarTildes(datos.getString(i));
			}else {
				generos+=t.quitarTildes(datos.getString(i))+", ";
			}
		}
		
		return generos;
	}
	
	public String unificarDirectores(JSONArray datos) {
		Texto t=new Texto();
		String directores="";
		for(int i=0;i<datos.length();i++) {
			if(i==datos.length()-1) {
				directores+=t.quitarTildes(datos.getString(i));
			}else {
				directores+=t.quitarTildes(datos.getString(i))+", ";
			}
		}
		
		return directores;
	}
	
	public String unificarReparto(JSONArray datos) {
		String reparto="";
		
		for(int i=0;i<datos.length();i++) {
			JSONObject dato=datos.getJSONObject(i);
			reparto+=dato.getString("nombre")+", ";

			if(i==(datos.length()-1)) {
				reparto+=dato.getString("personaje");
			}else {
				reparto+=dato.getString("personaje")+", ";
			}
		}
		
		return reparto;
	}
	
	public Articulo(JSONObject ficha) {
		this.id=ficha.getString("id");
		this.titulo=ficha.getString("titulo_es");
		this.titulo_en=ficha.getString("titulo_en");
		this.anio=ficha.getInt("anio");
		this.sinopsis=ficha.getString("sinopsis");
		this.trailer=ficha.getString("trailer");
		this.genero = unificarGeneros(ficha.getJSONArray("generos"));
		this.director = unificarDirectores(ficha.getJSONArray("directores"));
		this.duracion=ficha.getString("duracion");
		this.calificacion_media=ficha.getDouble("calificacion");
		this.nfavs = 0;
		this.nvistas = 0;
		
		String etiquetas=this.titulo+", "+this.titulo_en+", "+unificarReparto(ficha.getJSONArray("reparto"))+", "+this.genero+", "+this.director;
		this.etiquetas=etiquetas;
		this.fechaActualizacion=new Date();
		this.caratula=ficha.getString("caratula");
		this.fondo=ficha.getString("fondo");
		this.tipo=ficha.getInt("tipo");
		this.codeFA=ficha.getString("idFA");
		this.codeTTV=ficha.getString("idTTV");

	}

	@Override
	public String toString() {
		return "Articulo [id=" + id + ", titulo=" + titulo + ", titulo_en=" + titulo_en + ", anio=" + anio
				+ ", sinopsis=" + sinopsis + ", trailer=" + trailer + ", genero=" + genero + ", duracion=" + duracion
				+ ", calificacion_media=" + calificacion_media + ", nfavs=" + nfavs + ", nvistas=" + nvistas
				+ ", etiquetas=" + etiquetas + ", fechaActualizacion=" + fechaActualizacion + ", caratula=" + caratula
				+ ", fondo=" + fondo + ", codeFA=" + codeFA + ", codeTTV=" + codeTTV + ", director=" + director
				+ ", tipo=" + tipo + "]";
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getTitulo_en() {
		return titulo_en;
	}

	public void setTitulo_en(String titulo_en) {
		this.titulo_en = titulo_en;
	}

	public int getAnio() {
		return anio;
	}

	public void setAnio(int anio) {
		this.anio = anio;
	}

	public String getSinopsis() {
		return sinopsis;
	}

	public void setSinopsis(String sinopsis) {
		this.sinopsis = sinopsis;
	}

	public String getTrailer() {
		return trailer;
	}

	public void setTrailer(String trailer) {
		this.trailer = trailer;
	}

	public String getGenero() {
		return genero;
	}

	public void setGenero(String genero) {
		this.genero = genero;
	}

	public String getDuracion() {
		return duracion;
	}

	public void setDuracion(String duracion) {
		this.duracion = duracion;
	}

	public double getCalificacion_media() {
		return calificacion_media;
	}

	public void setCalificacion_media(double calificacion_media) {
		this.calificacion_media = calificacion_media;
	}

	public int getNfavs() {
		return nfavs;
	}

	public void setNfavs(int nfavs) {
		this.nfavs = nfavs;
	}

	public int getNvistas() {
		return nvistas;
	}

	public void setNvistas(int nvistas) {
		this.nvistas = nvistas;
	}

	public String getEtiquetas() {
		return etiquetas;
	}

	public void setEtiquetas(String etiquetas) {
		this.etiquetas = etiquetas;
	}

	public Date getFechaActualizacion() {
		return fechaActualizacion;
	}

	public void setFechaActualizacion(Date fechaActualizacion) {
		this.fechaActualizacion = fechaActualizacion;
	}

	public String getCaratula() {
		return caratula;
	}

	public void setCaratula(String caratula) {
		this.caratula = caratula;
	}

	public String getFondo() {
		return fondo;
	}

	public void setFondo(String fondo) {
		this.fondo = fondo;
	}

	public String getCodeFA() {
		return codeFA;
	}

	public void setCodeFA(String codeFA) {
		this.codeFA = codeFA;
	}

	public String getCodeTTV() {
		return codeTTV;
	}

	public void setCodeTTV(String codeTTV) {
		this.codeTTV = codeTTV;
	}

	public String getDirector() {
		return director;
	}

	public void setDirector(String director) {
		this.director = director;
	}

	public int getTipo() {
		return tipo;
	}

	public void setTipo(int tipo) {
		this.tipo = tipo;
	}
	
	public Articulo() {
		
	}
	
	public JSONObject toJSON() {
		JSONObject a=new JSONObject();
		
		a.put("id", this.id);
		a.put("titulo_es", this.titulo);
		a.put("titulo_en", this.titulo_en);
		a.put("anio", this.anio);
		a.put("sinopsis", this.sinopsis);
		a.put("trailer", this.trailer);
		a.put("genero", this.genero);
		a.put("duracion", this.duracion);
		a.put("calificacion", this.calificacion_media);
		a.put("nfavs", this.nfavs);
		a.put("nvistas", this.nvistas);
		a.put("etiquetas", this.etiquetas);
		a.put("fecha", this.fechaActualizacion);
		a.put("caratula", this.caratula);
		a.put("fondo", this.fondo);
		a.put("director", this.director);
		a.put("tipo", this.tipo);
		return a;
	}
	
	public void save() {
		ObjectifyService.ofy().save().entity(this).now();
	}

}
