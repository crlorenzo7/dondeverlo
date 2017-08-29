<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="com.mediaforyou.*" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="org.json.JSONObject" %>
<%@ page import="org.json.JSONArray" %>

<%
	Sistema system=new Sistema();
	String id=request.getParameter("id");
	String servicio=request.getParameter("servicio");
	String calidad = request.getParameter("calidad");
	
	ArrayList<JSONObject> precios=system.getPrecios(id, servicio, calidad);
	
	
%>

<% for(int i=0;i<precios.size();i++){
	JSONObject stream=precios.get(i);
	%>
	<a href="<%=stream.getString("url") %>">
		<li class="precio-muestra anim-g">
			<figure class="icono"><img src="<%=stream.getString("imagen") %>"></figure>
			<% if(!stream.getString("servicio").equals("stream")){ %>
			<div class="descripcion">
				<% if(stream.has("precioAlquiler")){ %>
					<p class="linea">Alquiler <span class="resaltado"><%=stream.getDouble("precioAlquiler") %> EUR</span></p>
				<% } %>
				<% if(stream.has("precioCompra")){ %>
					<p class="linea">Compra <span class="resaltado"><%=stream.getDouble("precioCompra") %> EUR</span></p>
				<% } %>
			</div>
			<% } %>
		</li>
	</a>
<% } %>