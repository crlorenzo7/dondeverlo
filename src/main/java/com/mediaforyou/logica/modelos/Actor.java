package com.mediaforyou.logica.modelos;

import org.json.JSONObject;

import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.annotation.*;

@Entity
public class Actor {
	@Id String nombre;
	String imagen;
	
	public Actor() {
		
	}
	public Actor(String nombre, String imagen) {
		this.nombre = nombre;
		this.imagen = imagen;
	}
	
	public Actor(JSONObject a) {
		this.nombre=a.getString("nombre");
		this.imagen=a.getString("imagen");
	}
	
	
	
	@Override
	public String toString() {
		return "Actor [nombre=" + nombre + ", imagen=" + imagen + "]";
	}



	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getImagen() {
		return imagen;
	}
	public void setImagen(String imagen) {
		this.imagen = imagen;
	}
	public void save() {
		ObjectifyService.ofy().save().entity(this).now();
	}
	
	
}
