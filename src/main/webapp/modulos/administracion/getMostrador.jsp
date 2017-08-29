<%@ page contentType="text/html; charset=UTF-8" %>
    <%@ page import="com.mediaforyou.*" %>
        <%@ page import="java.util.ArrayList" %>
            <%@ page import="org.json.JSONObject" %>

                <%

Sistema system=new Sistema();

int tipo=Integer.parseInt(request.getParameter("tipo"));
String orden=request.getParameter("orden");
String genero=request.getParameter("genero");
String busqueda=request.getParameter("busqueda");
int n=Integer.parseInt(request.getParameter("n"));

ArrayList<JSONObject> datos=system.getMostrador(tipo, orden, genero, busqueda, n);

int nr = datos.size();
if(nr>0){
	
	JSONObject articulo=datos.get(0);
	String sec="peliculas";
	if(tipo==1){
		sec="series";
	}
	String url=sec+"/"+articulo.getString("id");
	%>
	<a class="enlace-articulo" href="<%=url%>">
		<article class="main-card left anim-g">
			<img src="<%=articulo.getString("caratula")%>" />
			<div class="cubierta-cards hide">
				<div class="play anim-g adaptar-img"></div>
			</div>
		</article>
	</a>

<%

	if(nr>1){
		
		int npag = Math.round((nr - 1) / 2);
	    if ((nr - 1) > 20) {
	        npag = 10;
	    }
	    
	    for(int i=1;i<=npag;i++){
	    	if (i == (npag)) { %>
	            <section class="col-mostrador left">
	        <% } else { %>
	            <section class="col-mostrador left mr-col-mostrador">
	        <% }
	    	for (int j = ((i * 2) - 1); j < (((i * 2) - 1) + 2) && j < nr; j++) {
	    		articulo=datos.get(j);
	    		sec="peliculas";
	    		if(tipo==1){
	    			sec="series";
	    		}
	    		url=sec+"/"+articulo.getString("id");

	            if (j % 2 != 0) { %>
	                <a class="enlace-articulo" href="<%=url%>">
	                    <article class="card mi-card anim-g">
	                    	<img src="<%=articulo.getString("caratula")%>" />
	                    	<div class="cubierta-cards hide">
	                    		<div class="play2 anim-g adaptar-img"></div>
	                    	</div>
	                    </article>
	                 </a>
	            <% } else { %>
	                <a class="enlace-articulo" href="<%=url%>">
	                    <article class="card anim-g">
	                    	<img src="<%=articulo.getString("caratula")%>" />
	                    	<div class="cubierta-cards hide">
	                    		<div class="play2 anim-g adaptar-img"></div>
	                    	</div>
	                    </article>
	                 </a>
	            <% }
	    	}
	    	%>
	    	</section>
	    <%}
	}

}else{
	%>
	<div class="texto-medio"> no se han encontrado elementos</div>
<% 
}

%>

                    <%="$$%$$video|"+datos.size()+" Resultados" %>