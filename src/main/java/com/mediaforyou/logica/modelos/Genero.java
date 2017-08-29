package com.mediaforyou.logica.modelos;

import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.annotation.*;

@Entity
public class Genero {
	
	@Id String valor;
	@Index String nombre;
	
	public Genero(String valor,String nombre) {
		this.valor=valor;
		this.nombre=nombre;
	}

	public String getValor() {
		return valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	public void save() {
		ObjectifyService.ofy().save().entity(this).now();
	}
}
