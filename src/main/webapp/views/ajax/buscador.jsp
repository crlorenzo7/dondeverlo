
  <%@ page contentType="text/html; charset=UTF-8" %>

  <%@ page import="com.mediaforyou.*" %>
  <%@ page import="com.mediaforyou.misc.*" %>
  <%@ page import="org.json.*" %>
  <%@ page import="java.util.ArrayList" %>
  
  	<% 
  	Sistema system=new Sistema();
  	String query="";
  	if(request.getParameter("query")!=null){
		query=request.getParameter("query");
  	}

	ArrayList<JSONObject> fichas = system.buscarArticulosPorTitulo(query,0);
	
	if(fichas.size()>0){
		JSONObject articulo=fichas.get(0);
		String sec="peliculas";
		if(articulo.getInt("tipo")==1){
			sec="series";
		}
		String url=sec+"/"+articulo.getString("id");
		
		%>
		<a class="enlace-articulo" href="<%=url%>">
			<article class="main-card left anim-g">
				<img src="<%=articulo.getString("caratula") %>" />
				<div class="cubierta-cards hide">
					<div class="play anim-g adaptar-img"></div>
				</div>
			</article>
		</a>
		
	<% 
		int npag = (int)Math.ceil((double)(fichas.size()-1)/2);
		
		for(int i=1;i<=npag;i++){
			if (i == (npag)) { %>
	            <section class="pagina">
	       <% } else { %>
	            <section class="pagina mr-pagina">
	       <% }
			
			for (int j = ((i * 2) - 1); j < (((i * 2) - 1) + 2) && j < fichas.size(); j++) {
				articulo=fichas.get(j);
				sec="peliculas";
				if(articulo.getInt("tipo")==1){
					sec="series";
				}
				url=sec+"/"+articulo.getString("id");
				
				if (j % 2 != 0) { %>
				<a class="enlace-articulo" href="<%=url%>">
					<article class="card mi-card anim-g">
      					<img src="<%=articulo.getString("caratula") %>" />
        				<div class="cubierta-cards hide">
        						<div class="play2 anim-g adaptar-img"></div>
        				</div>
    				</article>
    			</a>
					
				<% }else{ %>
				<a class="enlace-articulo" href="<%=url%>">
					<article class="card anim-g">
      					<img src="<%=articulo.getString("caratula") %>" />
        				<div class="cubierta-cards hide">
        						<div class="play2 anim-g adaptar-img"></div>
        				</div>
    				</article>
    			</a>
				<% }
			}%>
			</section>
		<% }
	
	}else{
	
	%>
		<p class="resultado-busqueda">no hay resultados para "<%=query %>"</p>
	<% } %>
	<script type="text/javascript">
		$(document).ready(function(){
			mostrador();
            enlaces();
            adaptar();
		});
	</script>	
	
