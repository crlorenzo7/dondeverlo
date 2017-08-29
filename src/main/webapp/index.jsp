<!doctype html>
<html>

<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <title>Dondeverlo - descubre los mejores precios</title>
    <%@ page contentType="text/html; charset=UTF-8" %>
        <link rel="stylesheet" type="text/css" href="css/style.css?ver=189.0">
        <script type="text/javascript" src="js/jquery.min.js"></script>
        <script type="text/javascript" src="js/mousewheel.js?v4"></script>
        <script type="text/javascript" src="js/routes.js?ver=155.0"></script>
        <script type="text/javascript" src="js/java.js?ver=186.0"></script>

</head>
<%@ page import="com.mediaforyou.*" %>
    <%@ page import="java.util.ArrayList" %>
        <%@ page import="org.json.JSONObject" %>

            <%
    OfyHelper ofy=new OfyHelper();
	Sistema system=new Sistema();
	ArrayList<JSONObject> fichas_primer = system.buscarMejores(5,0);
	ArrayList<JSONObject> fichas_segundo = system.buscarMejores(5,5);
%>

                <body class="adaptar-img-fondo">
                    <div id="wrapper">

                        <div id="scroller">
                        	<section id="sobra" class="seccion-principal left">
                        		<section class="primer_valoracion margen_abajo">
                        		<%
                        		JSONObject articulo=new JSONObject();
                        		String sec="";
                        		String url="";
                        		for (int j = 0;j < fichas_primer.size(); j++) {
									articulo=fichas_primer.get(j);
									sec="peliculas";
									if(articulo.getInt("tipo")==1){
										sec="series";
									}
									url=sec+"/"+articulo.getString("id");
									if(j==fichas_primer.size()-1){
								%>
									
									<a class="enlace-articulo" href="<%=url %>">
										<article class="card-por anim-g">
					      					<img src="<%=articulo.getString("caratula") %>" />
					        				<div class="cubierta-cards hide">
					        						<div class="play2 anim-g adaptar-img"></div>
					        				</div>
					    				</article>
					    			</a>
								<%
									}else{
								%>
									<a class="enlace-articulo" href="<%=url %>">
										<article class="card-por anim-g mr-pagina-por">
					      					<img src="<%=articulo.getString("caratula") %>" />
					        				<div class="cubierta-cards hide">
					        						<div class="play2 anim-g adaptar-img"></div>
					        				</div>
					    				</article>
					    			</a>
								<%
									}
                        		}
								%>
                        			
                        		</section>
                        		<section class="primer_valoracion">
                        				<%

		                        		for (int j = 0;j < fichas_segundo.size(); j++) {
											articulo=fichas_segundo.get(j);
											sec="peliculas";
											if(articulo.getInt("tipo")==1){
												sec="series";
											}
											url=sec+"/"+articulo.getString("id");
											if(j==fichas_primer.size()-1){
										%>
											
											<a class="enlace-articulo" href="<%=url %>">
												<article class="card-por anim-g">
							      					<img src="<%=articulo.getString("caratula") %>" />
							        				<div class="cubierta-cards hide">
							        						<div class="play2 anim-g adaptar-img"></div>
							        				</div>
							    				</article>
							    			</a>
										<%
											}else{
										%>
											<a class="enlace-articulo" href="<%=url %>">
												<article class="card-por anim-g mr-pagina-por">
							      					<img src="<%=articulo.getString("caratula") %>" />
							        				<div class="cubierta-cards hide">
							        						<div class="play2 anim-g adaptar-img"></div>
							        				</div>
							    				</article>
							    			</a>
										<%
											}
		                        		}
										%>
                        		</section>
                        	</section>
                        	<section id="mostrador" class="seccion-principal left">
                        		<header id="muestra">
                        			<div id="cubierta-muestra">
	                        			
                        			</div>
                        		</header>
                        		<div id="wrapper-mostrador" data="0">
                        			<div id="scroller-mostrador" class="scroller">
                        			<% 
                        				String query="";
									  	if(request.getParameter("query")!=null){
											query=request.getParameter("query");
									  	}
									
										ArrayList<JSONObject> fichas = system.buscarArticulosPorTitulo(query,1);
										
										if(fichas.size()>0){
											articulo=fichas.get(0);
											sec="peliculas";
											if(articulo.getInt("tipo")==1){
												sec="series";
											}
											url=sec+"/"+articulo.getString("id");
											
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
                        			</div>
                        		</div>
                        	</section>
                        	<section id="articulo" class="seccion-principal left">
                        		
                        	</section>
                        </div>

                    </div>
                    <header id="main-header">
                    	<a href="index.jsp"><figure id="logo">Donde<span class="green">verlo</span></figure></a>
                    	<div id="zona-buscador">
                    		<figure id="lupa" class="anim-g"></figure>
                    		<div id="cubre-input-buscador">
                    			<input type="search" id="buscador" name="buscador" placeholder="buscar tu pelicula o serie... press enter">
                    		</div>
                    	</div>
                    	
                    	<ul id="zona-filtros" class="hide">
                    		<li class="filtro"></li>
                    	</ul>
                    </header>
                </body>
                <script type="text/javascript">
                	$(document).ready(function(){
                		inicio();
                		mostrador();
                        enlaces();
                		adaptar();
                	});
                </script>

</html>