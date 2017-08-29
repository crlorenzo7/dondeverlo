package com.mediaforyou.logica.modelos;

import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.annotation.*;

@Entity
public class Proveedor {
	@Id String nombre;
	String imagenGrande;
	String imagenPeq;
	
	public Proveedor(String nombre,String imagenGrande,String imagenPeq) {
		this.nombre=nombre;
		this.imagenGrande=imagenGrande;
		this.imagenPeq=imagenPeq;
	}
	
	public Proveedor() {
		
	}
	
	public String getNombre() {
		return nombre;
	}



	public void setNombre(String nombre) {
		this.nombre = nombre;
	}



	public String getImagenGrande() {
		return imagenGrande;
	}



	public void setImagenGrande(String imagenGrande) {
		this.imagenGrande = imagenGrande;
	}



	public String getImagenPeq() {
		return imagenPeq;
	}



	public void setImagenPeq(String imagenPeq) {
		this.imagenPeq = imagenPeq;
	}



	public void save() {
		ObjectifyService.ofy().save().entity(this).now();
	}
}
