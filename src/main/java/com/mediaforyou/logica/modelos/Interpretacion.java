package com.mediaforyou.logica.modelos;

import org.json.JSONObject;

import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.annotation.*;

@Entity
public class Interpretacion {
	@Id String id;
	@Index String nombreActor;
	@Index String personaje;
	@Index String idArticulo;
	@Index int orden;
	
	public Interpretacion(String nombreActor, String idArticulo, String personaje, int orden) {
		this.id = id;
		this.nombreActor = nombreActor;
		this.idArticulo = idArticulo;
		this.personaje = personaje;
		this.orden=orden;
	}
	
	public String crearID(String ar,String na) {
		String id=ar+"_"+na.replaceAll("\\s+", "");
		return id;
	}
	
	public Interpretacion() {
		
	}
	
	public Interpretacion(String ida,JSONObject a) {
		this.id=crearID(ida,a.getString("nombre"));
		this.nombreActor=a.getString("nombre");
		this.personaje=a.getString("personaje");
		this.idArticulo=ida;
		this.orden=a.getInt("orden");
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNombreActor() {
		return nombreActor;
	}

	public void setNombreActor(String nombreActor) {
		this.nombreActor = nombreActor;
	}

	public String getPersonaje() {
		return personaje;
	}

	public void setPersonaje(String personaje) {
		this.personaje = personaje;
	}

	public String getIdArticulo() {
		return idArticulo;
	}

	public void setIdArticulo(String idArticulo) {
		this.idArticulo = idArticulo;
	}

	public int getOrden() {
		return orden;
	}

	public void setOrden(int orden) {
		this.orden = orden;
	}
	
	public JSONObject toJSON() {
		JSONObject trabajo=new JSONObject();
		trabajo.put("idArticulo", this.idArticulo);
		trabajo.put("nombreActor", this.nombreActor);
		trabajo.put("orden", this.orden);
		trabajo.put("personaje", this.personaje);
		return trabajo;
	}
	
	public void save() {
		ObjectifyService.ofy().save().entity(this).now();
	}
	
	
	
}
