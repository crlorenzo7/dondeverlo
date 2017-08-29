package com.mediaforyou;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.mediaforyou.logica.modelos.*;

/**
 * Servlet implementation class Inicio
 */
public class Inicio extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		OfyHelper ofy=new OfyHelper();
		ArrayList<Genero> generos=new ArrayList<Genero>();
		generos.add(new Genero("Acción","Accion"));
		generos.add(new Genero("Animación","Animacion"));
		generos.add(new Genero("Aventuras","Aventuras"));
		generos.add(new Genero("Ciencia Ficción","Ciencia Ficcion"));
		generos.add(new Genero("Comedia","Comedia"));
		generos.add(new Genero("Drama","Drama"));
		generos.add(new Genero("Fantástico","Fantastico"));
		generos.add(new Genero("Infantil","Infantil"));
		generos.add(new Genero("Intriga","Intriga"));
		generos.add(new Genero("Musical","Musical"));
		generos.add(new Genero("Romance","Romance"));
		generos.add(new Genero("Thriller","Thriller"));
		
		for(Genero g:generos) {
			g.save();
		}
		
		ArrayList<Proveedor> proveedores=new ArrayList<Proveedor>();
		proveedores.add(new Proveedor("google","img/interfaz/google-g.png","img/interfaz/google-p.png"));
		proveedores.add(new Proveedor("itunes","img/interfaz/itunes-g.png","img/interfaz/itunes-p.png"));
		proveedores.add(new Proveedor("playstation","img/interfaz/playstation-g.png","img/interfaz/playstation-p.png"));
		proveedores.add(new Proveedor("wuaki","img/interfaz/wuaki-g.png","img/interfaz/wuaki-p.png"));
		proveedores.add(new Proveedor("netflix","img/interfaz/netflix-g.png","img/interfaz/netflix-p.png"));
		proveedores.add(new Proveedor("microsoft","img/interfaz/microsoft-g.png","img/interfaz/microsoft-p.png"));
		proveedores.add(new Proveedor("hbo","img/interfaz/hbo-g.png","img/interfaz/hbo-p.png"));
		proveedores.add(new Proveedor("amazon","img/interfaz/amazon-g.png","img/interfaz/amazon-p.png"));
		proveedores.add(new Proveedor("movistar","img/interfaz/movistar-g.png","img/interfaz/movistar-p.png"));
		
		for(Proveedor p:proveedores) {
			p.save();
		}
		
		response.getWriter().append("iniciado correctamente");
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
