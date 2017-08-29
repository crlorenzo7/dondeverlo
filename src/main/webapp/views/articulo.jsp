<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="com.mediaforyou.*" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="org.json.JSONObject" %>
<%@ page import="org.json.JSONArray" %>

<%
	int tipo = Integer.parseInt(request.getParameter("tipo"));
	String id= request.getParameter("id");
	
	Sistema system=new Sistema();
	JSONObject articulo=system.getArticuloByID(id);
	
	JSONArray streams=articulo.getJSONObject("precios").getJSONArray("stream");
	JSONArray reparto=articulo.getJSONArray("reparto");
	
	int nreparto=reparto.length();
	if(nreparto>20){
		nreparto=20;
	}
%>

<header id="precios-articulo" data="<%=articulo.getString("id")%>">
	<div id="wrapper-precios-articulo">
		<div class="scroller" id="scroller-precios-articulo">
			<ul class="lista-precios-muestra" id="lpmuestra">
			
				<% for(int i=0;i<streams.length();i++){
					JSONObject stream=streams.getJSONObject(i);
					%>
					<a href="<%=stream.getString("url") %>">
						<li class="precio-muestra anim-g">
							<figure class="icono"><img src="<%=stream.getString("imagen") %>"></figure>
						</li>
					</a>
				<% } %>
			</ul>
		</div>
	</div>
	<ul id="menu-precios-articulo">
		<li class="control-precio-articulo mi-control-precio-articulo">
			<select name="servicio" id="servicio">
				<option value="stream" selected>Stream</option>
			<option value="alquiler">Alquiler / Compra</option>
		</select>
	</li>
	<li class="control-precio-articulo">
		<select name="calidad" id="calidad">
			<option value="sd" selected>SD</option>
			<option value="hd">HD</option>
	                 				</select>
	                 			</li>
	                 		</ul>
	                		</header>
	                		<header class="cabecera-articulo">
	    <div class="cabecera-articulo-sup">
	        <h3 class="titulo-articulo-grande texto-grande" id="titulo-articulo-main"><%=articulo.getString("titulo_es") %><span class="anio"><%=articulo.getInt("anio") %></span></h3>
	    </div>
	    <div class="cabecera-articulo-inf">
	        <p class="texto-medio" id="subtitulo-articulo">
	        	<span class="item-subtitulo-articulo left mim"><%=articulo.getString("genero") %></span>
	<span class="item-subtitulo-articulo left mim"><%=articulo.getString("duracion") %></span>
	<span class="left mim"><%=articulo.getDouble("calificacion") %></span>
	        </p>
	        <figure class="star-l adaptar-img"></figure>
	    </div>
</header>
<section id="wrapper-articulo" data-fondo="">
    <div class="scroller" id="scroll-articulo">
        <article id="caratula-con" class="caratula-articulo left anim-g"><img src="<%=articulo.getString("caratula") %>"/></article>
        <section class="small-section left" id="sinopsis">
      
            <h4 class="texto-medio titulo-small-section">Sinopsis</h4>
            <p class="texto-small" id="sinopsis-con">
            	<%=articulo.getString("sinopsis") %>
            </p>
        </section>
        <section class="col-articulo" id="direccion">
            <section class="small-section" id="director">
                <h4 class="texto-medio titulo-small-section">Director</h4>
                <p class="texto-small" id="director-con"><%=articulo.getString("director") %></p>
            </section>
            <section class="small-section" id="trailer">
                <div id="trailer-item">
                <a href="https://www.youtube.com/watch?v=<%=articulo.getString("trailer") %>" target="_blank"><img src="<%=articulo.getString("fondo") %>"/></a>
                </div>
            </section>
        </section>
        <section class="small-section left" id="reparto">
            <header id="cabecera-reparto">
                <h4 class="texto-medio titulo-small-section">Reparto</h4>
                <p class="texto-normal" id="resultados-reparto"><%=nreparto %> actores / actrices</p>
            </header>
            <section id="cuerpo-reparto">
                <div class="scroller" id="scroll-reparto">
                	<% 
                		int npagR = (int)Math.ceil((double)(reparto.length())/2);
                	
                		for(int i=0;i<npagR && i<10;i++){
                			if(i==(npagR-1)){
                				%>
                				<div class="col-reparto">
                				<% 
                			}else{
								%>
                				<div class="col-reparto mr-col-reparto">
                				<% 
                			}
                			for (int j = (i * 2); j < ((i * 2) + 2) && j < reparto.length(); j++) {
                				JSONObject actor=reparto.getJSONObject(j);
                				if((j%2)==0){
                					%>
                					<div nombre="<%=actor.getString("nombreActor") %>" personaje="<%=actor.getString("personaje") %>" class="actor mb-actor">
                					<% 
                				}else{
									%>
                					<div nombre="<%=actor.getString("nombreActor") %>" personaje="<%=actor.getString("personaje") %>" class="actor">
                					<% 
                				}
                				
                				%>
                					<img id="ima<%=j %>" src="<%=actor.getString("imagen") %>" />
                					<div class="cubierta-actor hide"></div>
                				</div>
                				<% 
                			}
                	%>
					</div>
					
					<% } %>
                </div>
            </section>
            <footer id="footer-reparto">
            	<h4 class="texto-grande tit-capi" id="nombre-actor"></h4>
    			<p class="texto-medio" id="personaje"></p>
            </footer>
        </section>
    </div>
</section>
<footer class="footer-articulo">
    <fugure class="boton-atras anim-g adaptar-img" id="atras-articulo"></fugure>
    <ul id="menu-articulo">
        <a href="ficha">
            <li class="item-menu-articulo-activo anim-g">Ficha</li>
        </a>
        <a href="reparto">
            <li class="item-menu-articulo anim-g">Reparto</li>
        </a>
    </ul>
</footer>
<script type="text/javascript">
	$(document).ready(function(){
		adaptar();
		articulo();
		enlaces();
	});
	adaptar();
	$('body').css('background-image','url(<%=articulo.getString("fondo") %>)');
</script>