package com.mediaforyou.logica.modelos;

import java.util.Date;

import org.json.JSONObject;

import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.annotation.*;

@Entity
public class Capitulo {
	
	@Id String id;
	@Index String idArticulo;
	@Index int temporada;
	@Index int orden;
	@Index String titulo;
	String imagen;
	Date fecha;
	int nvistas;
	
	
	public Capitulo() {
		
	}

	public Capitulo(String ida,JSONObject cap) {
		this.id=ida+"_"+cap.getInt("temporada")+"_"+cap.getInt("orden");
		this.idArticulo=ida;
		this.temporada=cap.getInt("temporada");
		this.orden=cap.getInt("orden");
		this.titulo=cap.getString("titulo");
		this.imagen=cap.getString("imagen");
		this.fecha=new Date();
		this.nvistas=0;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getIdArticulo() {
		return idArticulo;
	}

	public void setIdArticulo(String idArticulo) {
		this.idArticulo = idArticulo;
	}

	public int getTemporada() {
		return temporada;
	}

	public void setTemporada(int temporada) {
		this.temporada = temporada;
	}

	public int getOrden() {
		return orden;
	}

	public void setOrden(int orden) {
		this.orden = orden;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getImagen() {
		return imagen;
	}

	public void setImagen(String imagen) {
		this.imagen = imagen;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public int getNvistas() {
		return nvistas;
	}

	public void setNvistas(int nvistas) {
		this.nvistas = nvistas;
	}
	
	public JSONObject toJSON() {
		JSONObject c=new JSONObject();
		
		c.put("id", this.id);
		c.put("idArticulo", this.idArticulo);
		c.put("temporada", this.temporada);
		c.put("orden", this.orden);
		c.put("titulo", this.titulo);
		c.put("imagen", this.imagen);
		c.put("nvistas", this.nvistas);
		c.put("fecha", this.fecha);
		return c;
	}
	
	public void save() {
		ObjectifyService.ofy().save().entity(this).now();
	}

}
