<%@ page contentType="text/html; charset=UTF-8" %>

 <%@ page import="com.mediaforyou.*" %>
 <%@ page import="com.mediaforyou.misc.*" %>
 <%@ page import="org.json.*" %>
 <%@ page import="java.util.ArrayList" %>
 
 	<% 
 	
 	Sistema system=new Sistema();
 	JSONObject articulo = system.getMuestraByID(request.getParameter("id"));
 	ArrayList<JSONObject> precios =new ArrayList<JSONObject>();
 	JSONArray stream = articulo.getJSONObject("precios").getJSONArray("stream");
 	JSONArray compraSD = articulo.getJSONObject("precios").getJSONArray("compraSD");
 	for(int i=0;i<stream.length();i++){
 		JSONObject p=stream.getJSONObject(i);
 		precios.add(p);
 	}
 	for(int i=0;i<compraSD.length();i++){
 		JSONObject p=compraSD.getJSONObject(i);
 		precios.add(p);
 	}
 	%>
 	
	<div id="zona-titulo-muestra">
		<h3 id="titulo-muestra"><%=articulo.getString("titulo_es") %></h3>
		<p class="texto-medio bold green-c" id="anio-muestra"><%=articulo.getInt("anio") %></p>
		<p class="texto-medio" id="duracion-muestra"><%=articulo.getString("duracion") %></p>
		<div class="clear"></div>
	</div>

	<ul id="lista-precios-muestra">
	
	<%
 	for(int i=0;i<precios.size();i++){
 		JSONObject pre=precios.get(i);
 	%>
 		<a href="<%=pre.getString("url") %>">
	 		<li class="precio-muestra anim-g">
				<figure class="icono"><img src="<%=pre.getString("imagen") %>"></figure>
				<% if(!pre.getString("servicio").equals("stream")){ %>
				<div class="descripcion">
					<% if(pre.has("precioAlquiler")){ %>
						<p class="linea">Alquiler <span class="resaltado"><%=pre.getDouble("precioAlquiler") %> EUR</span></p>
					<% } %>
					<% if(pre.has("precioCompra")){ %>
						<p class="linea">Compra <span class="resaltado"><%=pre.getDouble("precioCompra") %> EUR</span></p>
					<% } %>
				</div>
				<% } %>
			</li>
		</a>
 	<% } %>
	</ul>
	<script type="text/javascript">
    	$(document).ready(function(){
    		enlaces();;
    	});
    </script>
	