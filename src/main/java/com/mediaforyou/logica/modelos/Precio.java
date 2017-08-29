package com.mediaforyou.logica.modelos;

import org.json.JSONObject;

import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.annotation.*;

@Entity
public class Precio {
	
	@Id String id;
	@Index String idArticulo;
	@Index String empresa;
	@Index String servicio;
	@Index String calidad;
	@Index Double precio;
	String url;
	
	public String crearID(String ida,String empresa,String calidad,String servicio) {
		String id=ida+"_"+empresa.replaceAll("\\s+","").trim()+"_"+calidad+"_"+servicio;
		return id;
	}
	
	public Precio(String ida,JSONObject precio) {
		this.id=this.crearID(ida, precio.getString("empresa"), precio.getString("calidad"), precio.getString("servicio"));
		this.idArticulo=ida;
		this.empresa=precio.getString("empresa");
		this.servicio=precio.getString("servicio");
		this.calidad=precio.getString("calidad");
		this.precio=precio.getDouble("precio");
		this.url=precio.getString("url");
	}
	
	public Precio() {
		
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

	public String getEmpresa() {
		return empresa;
	}

	public void setEmpresa(String empresa) {
		this.empresa = empresa;
	}

	public String getServicio() {
		return servicio;
	}

	public void setServicio(String servicio) {
		this.servicio = servicio;
	}

	public String getCalidad() {
		return calidad;
	}

	public void setCalidad(String calidad) {
		this.calidad = calidad;
	}

	public Double getPrecio() {
		return precio;
	}

	public void setPrecio(Double precio) {
		this.precio = precio;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public JSONObject toJSON() {
		JSONObject p=new JSONObject();
		
		p.put("idArticulo", this.idArticulo);
		p.put("empresa", this.empresa);
		p.put("servicio", this.servicio);
		p.put("calidad", this.calidad);
		if(this.servicio.equals("alquiler")) {
			p.put("precioAlquiler", this.precio);
		}else {
			p.put("precioCompra", this.precio);
		}
		p.put("url", this.url);
		
		return p;
	}
	
	public void save() {
		ObjectifyService.ofy().save().entity(this).now();
	}

}
