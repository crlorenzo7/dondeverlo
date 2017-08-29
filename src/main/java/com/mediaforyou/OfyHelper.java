package com.mediaforyou;

import com.googlecode.objectify.ObjectifyService;
import com.mediaforyou.logica.modelos.*;

public class OfyHelper {

		public OfyHelper() {
			ObjectifyService.register(Articulo.class);
		    ObjectifyService.register(Actor.class);
		    ObjectifyService.register(Interpretacion.class);
		    ObjectifyService.register(Genero.class);
		    ObjectifyService.register(Precio.class);
		    ObjectifyService.register(Proveedor.class);
		    ObjectifyService.register(Capitulo.class);
		}
}
