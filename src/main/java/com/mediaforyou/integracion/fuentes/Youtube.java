package com.mediaforyou.integracion.fuentes;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.mediaforyou.misc.Texto;

public class Youtube {

	public Youtube() {
		
	}
	
	public String getVideo(String busqueda) {
		
		Texto formateador=new Texto();
		busqueda=formateador.quitarTildes(busqueda).replaceAll(" ", "+");
		
		String url="https://www.youtube.com/results?search_query="+busqueda;
		
		Document doc;
		String videoCode="";
		try {
			doc = Jsoup.connect(url).timeout(0).get();
			Elements lista_videos = doc.select("ol.item-section").select("li");
			
			
			
			boolean encontrado=false;
			
			for(int i=0;i<lista_videos.size() && !encontrado;i++) {
				Element video = lista_videos.get(i);
				if(!(video.select("div.yt-lockup").isEmpty())) {
					videoCode = video.select("div.yt-lockup").first().attr("data-context-item-id");
					encontrado=true;
				}
			}
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return videoCode;
		
	}
}
