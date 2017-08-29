package com.mediaforyou.misc;

import java.text.Normalizer;

public class Texto {

	public Texto() {
		
	}
	
	public String quitarTildes(String texto) {
		texto = Normalizer.normalize(texto, Normalizer.Form.NFD);
        texto = texto.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
        return texto;
	}
}
