window.onload = function() {
    adaptar();
}

window.onresize = function() {
    adaptar();
}
function adaptar() {
	var alto = window.innerHeight;
    var ancho = window.innerWidth;

    adaptar_mostrador(10);
    if ($('#wrapper-articulo').length > 0) {
        adaptar_articulo(10);
    }
    if($('.primer_valoracion').length>0){
    	var alto_base = 310;
        var ancho_base = 209;
        var ancho_ar=($('.primer_valoracion').height()*ancho_base)/alto_base;
        var ancho_total=((ancho_ar+20)*5)-20;
        $('.primer_valoracion').width(ancho_total);
        $('.card-por').width(ancho_ar);
    }
    /*if ($('#wrapper-novedades').length > 0) {
        var prop16_9_ancho = 160 / 90;
        var prop16_9_alto = 90 / 160;
        var alto_novedad = $('#wrapper-novedades').height();
        var ancho_to = $('#wrapper-novedades').width();
        var ancho_novedad = alto_novedad * prop16_9_ancho;
        $('.imagen-novedad').height(alto_novedad);
        $('.imagen-novedad').width(ancho_novedad);
        $('.descripcion-novedad').width(ancho_to - ancho_novedad - 42);

    }
    if ($('#wrapper-articulo').length > 0) {
        adaptar_articulo();
    }*/
}

function adaptar_mostrador(n){
	var alto_base = 310;
    var ancho_base = 209;

    var alto_efectivo = $('#mostrador').height() - 283;
    var ancho_efectivo = $('#mostrador').width();

    var ancho_article = (ancho_efectivo) / n - 1;
    var alto_article = (ancho_article * alto_base) / (ancho_base);

    var ancho_principal = (ancho_article * 2) + 1;
    var alto_principal = alto_article * 2;

    $('#scroller-mostrador').height(alto_principal);

    $('.main-card').width(Math.ceil(ancho_principal));
    $('.pagina').width(Math.ceil(ancho_article));
    
    if (alto_efectivo > (alto_principal)) {
        $('#wrapper-mostrador').height(alto_efectivo);
    } else {
        adaptar_mostrador((n+1));
    }
}

function adaptar_articulo(n){
	var alto_base = 310;
    var ancho_base = 209;


    var alto_efectivo = window.innerHeight - 358;
    var ancho_efectivo =  window.innerWidth - 50;


    var ancho_article = (ancho_efectivo) / n - 1;
    var alto_article = (ancho_article * alto_base) / (ancho_base);


    var ancho_principal = (ancho_base * alto_efectivo) / alto_base;
    var alto_principal = alto_article * 2;
    
    
    var ancho_scroll_articulo=(ancho_efectivo*2)+1;
    $('#scroll-articulo').height(alto_efectivo);

    
    if ($('.caratula-articulo').length > 0) {
        $('.caratula-articulo').width(Math.ceil(ancho_principal));
        var alto_trailer = alto_efectivo - 69;
        var proporcion_169=160.0/90;
        var ancho_direccion=alto_trailer * proporcion_169;
        var ancho_sinopsis = (window.innerWidth - 50) - ($('#caratula-con').width() + 2 + ancho_direccion);
        $('#sinopsis').width((ancho_sinopsis-30));
        $('#direccion').width(ancho_direccion);
        $('#trailer').height(alto_trailer);
    }


    if ($('#reparto').length > 0) {
        $('#reparto').width(ancho_efectivo);

        var alto_cuerpo_reparto = $('#scroll-articulo').height() - 139;
        $('#cuerpo-reparto').height(alto_cuerpo_reparto);

        var ancho_base_actor = ((alto_cuerpo_reparto - 2) / 2);
        var numero_base_actores = Math.ceil(ancho_efectivo / ancho_base_actor);
        var ancho_def_actor = (ancho_efectivo / numero_base_actores) - 1;
        $('.col-reparto').width(ancho_def_actor);
        $('.actor').height(ancho_def_actor);


    }

    
    $('#scroll-articulo').width(ancho_scroll_articulo);
    
    /*if (alto_efectivo > (alto_principal)) {
        $('#wrapper-articulo').height(alto_efectivo);
    } else {
    	adaptar_articulo((n+1));
    }*/
    
}

var pantalla="inicio";

function inicio(){
	$('#buscador').keypress(function(e){
		if(e.which==13){
			buscar($(this).val());
			if(pantalla=="inicio"){
				mover_inicio();
			}
		}
	});
	enlaces();
	$('#wrapper-mostrador').mousewheel(function(event, delta) {

        this.scrollLeft -= (delta * 30);

        event.preventDefault();

    });
	$('.card-por').unbind();

    $('.card-por').hover(mostrar_seleccion, ocultar_seleccion);
}

function buscar(busqueda){
	var ancho=window.innerWidth;
	$('#main-header').promise().done(function(){
		$('#scroller').animate({"left":"-"+ancho+"px"},500);
	});
	var variables={}
	variables['query']=busqueda;
	$('#scroller-mostrador').fadeOut(250,function(){
		var contenido='';
		contenido+='<div id="animacion-carga">'+
				   		'<div id="animacion-externo" class="adaptar-img"></div>'+
				   		'<div id="animacion-interno" class="adaptar-img"></div>'+
					'</div>';
		$('#scroller-mostrador').html(contenido);
		$('#scroller-mostrador').fadeIn(0);
	});
	$.ajax({
		type: 'POST',
        url: 'views/ajax/buscador.jsp',
        data: variables,
        success: function(data) {
        	$('#scroller-mostrador').fadeOut(500,function(){
        		$('#scroller-mostrador').html(data);
        		$('#scroller-mostrador').fadeIn(500);
        	
	            n = parseInt($('#wrapper-mostrador').attr('data'));
	            var ncolumnas = $('.pagina').length;
	            if (ncolumnas == 0) {
	                $('#scroller-mostrador').width(500);
	            } else {
	                var ancho_columna = parseFloat($('.pagina').width() * 1.0);
	                var ancho_main = $('.main-card').width() * 1.0;
	                var ancho_pro = (((ancho_columna + 1) * (ncolumnas)) + (ancho_columna * 2) + 2) + n;
	
	                $('#scroller-mostrador').width(ancho_pro);
	            }
        	});
        }
	});
}

function mover_inicio(){
	pantalla="segunda";
	var alto=window.innerHeight;
	var altoHeader=$('#main-header').height();
	var top=alto-118;
	
	var ancho=window.innerWidth;
	$('#logo').css("float","left");
	$('#logo').css("padding-top","10px");
	$('#main-header').css("height","auto");
	$('#main-header').animate({"bottom":0+"px"},500);
}


function mostrar_seleccion_caratula() {
    $(this).find('.cubierta-cards').fadeIn(250);

    $(this).find('.play').css('top', 'calc(50% - 32px)');
}

function ocultar_seleccion_caratula() {
    $(this).find('.cubierta-cards').fadeOut(250);

    $(this).find('.play').css('top', 'calc(70% - 32px)');

}

function mostrador() {
    $('.main-card').unbind();
    $('.card').unbind();

    $('.main-card').hover(mostrar_seleccion, ocultar_seleccion);
    $('.card').hover(mostrar_seleccion, ocultar_seleccion);
    $('.main-card').hover(cargarMuestra, nada);
    $('.card').hover(cargarMuestra, nada);
    
}

function nada(){
	
}

function cargarMuestra(){
	var href=$(this).parent('a').attr('href').split('/');
	var id=href[1];
	variables={}
	variables['id']=id;
	
	$.ajax({
		type: 'POST',
        url: 'views/ajax/muestra.jsp',
        data: variables,
        success: function(data) {
        	$('#cubierta-muestra').fadeOut(500,function(){
        		$('#cubierta-muestra').html(data);
        		$('#cubierta-muestra').fadeIn(500);
        	});
        	
        }
	});
}

function desplazar_pagina(n){
	var ancho=window.innerWidth;
	$('#scroller').animate({"left":"-"+(ancho*n)+"px"},500);
}

function desplazar_articulo(seccion){
	
	var ancho=window.innerWidth-50;
	if(seccion=='ficha'){
		$('#scroll-articulo').animate({"left":0+"px"},500);
	}else{
		$('#scroll-articulo').animate({"left":"-"+ancho+"px"},500);
	}
}

function mostrar_seleccion() {
    $(this).find('.cubierta-cards').fadeIn(250);

    if ($(this).hasClass('main-card')) {
        $(this).find('.play').css('top', 'calc(50% - 32px)');
    } else {
        $(this).find('.play2').css('top', 'calc(50% - 17px)');
    }
}

function ocultar_seleccion() {
    $(this).find('.cubierta-cards').fadeOut(250);
    if ($(this).hasClass('main-card')) {
        $(this).find('.play').css('top', 'calc(70% - 32px)');
    } else {
        $(this).find('.play2').css('top', 'calc(70% - 17px)');
    }
}

function enlaces(){
	$('a').unbind();
	$('a').click(function(e){
		e.preventDefault();
	});
	$('a').click(analizar_enlaces);
}

function articulo(){
	$('#servicio').unbind();
	$('#calidad').unbind();
	$('#servicio').change(filtrarPrecios);
	$('#calidad').change(filtrarPrecios);
	$('.actor').unbind();
	$('.actor').hover(muestraReparto,nada);
	$('#atras-articulo').unbind();
    $('#atras-articulo').click(desplazar_pagina_atras);

    $('.item-menu-articulo').unbind();
    $('.item-menu-articulo').click(seleccionar_seccion_articulo);
    $('.item-menu-articulo-activo').unbind();
    $('.item-menu-articulo-activo').click(seleccionar_seccion_articulo);
}

function filtrarPrecios(){
	var servicio=$('#servicio').val();
	var calidad =$('#calidad').val();
	var id=$('#precios-articulo').attr('data');
	
	if(servicio=='stream'){
		calidad='0';
	}
	
	$.ajax({
		type: 'GET',
        url: 'views/ajax/precios.jsp?id='+id+'&servicio='+servicio+'&calidad='+calidad,
        success: function(data) {
        	console.log(data);
        	$('#lpmuestra').fadeOut(500,function(){
        		$('#lpmuestra').html(data);
        		$('#lpmuestra').fadeIn(500);
        	});
        	
        }
	});
	
}

function muestraReparto(){
	var nombre=$(this).attr('nombre');
	var personaje=$(this).attr('personaje');
	
	$('#footer-reparto').fadeOut(500,function(){
		$('#nombre-actor').html(nombre);
		$('#personaje').html(personaje);
		$('#footer-reparto').fadeIn(500);
	})
	
}

function seleccionar_seccion_articulo() {
    $('.item-menu-articulo-activo').addClass('item-menu-articulo').removeClass('item-menu-articulo-activo');
    $(this).addClass('item-menu-articulo-activo').removeClass('item-menu-articulo');
}

function desplazar_pagina_atras() {
	var ancho=window.innerWidth;
	$('#scroller').animate({ "left": "-"+ancho+"px" }, 500).promise().done(function() {
        $('#articulo').html('');
    });

}
