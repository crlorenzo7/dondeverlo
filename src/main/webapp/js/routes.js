function analizar_enlaces(){
	var hr = $(this).attr('href');
    trozos = hr.split('/');
    if(trozos[0]=='https:'){
    	window.open(hr, '_blank');
    }
    if(trozos[0]=='index.jsp'){
    	window.open(hr, '_self');
    }
    if (trozos.length == 1) {

        if (trozos[0] == 'ficha' || trozos[0] == 'reparto') {
            desplazar_articulo(trozos[0]);
        }
    }
    
    if (trozos.length == 2) {
        if (trozos[0] == 'series' || trozos[0] == 'peliculas' || trozos[0] == 'musica' || trozos[0] == 'libros') {
            var tipo = '0';
            if (trozos[0] == 'series') {
                tipo = '1';
            }
            if (trozos[0] == 'musica') {
                tipo = '3';
            }
            if (trozos[0] == 'libros') {
                tipo = '2';
            }
            var id = trozos[1];
            cargar_articulo(id, tipo);
            if(pantalla=="inicio"){
				mover_inicio();
			}
        }
    }
}

function cargar_articulo(id, tipo) {
    var url_carga = 'views/articulo.jsp?tipo=' + tipo + '&id=' + id;
    $('#articulo').fadeOut(250,function(){
		var contenido='';
		contenido+='<div id="padd-car"><div id="animacion-carga">'+
				   		'<div id="animacion-externo" class="adaptar-img"></div>'+
				   		'<div id="animacion-interno" class="adaptar-img"></div>'+
					'</div></div>';
		$('#articulo').html(contenido);
		$('#articulo').fadeIn(0);
	});
    $.ajax({
		type: 'GET',
        url: url_carga,
        success: function(data) {
        	$('#articulo').fadeOut(500,function(){
        		$('#articulo').html(data);
        		$('#articulo').fadeIn(500);
        		adaptar();
        		enlaces();
        	});
        	adaptar();
        	
        }
	});
    window.location.hash = url_carga;
    desplazar_pagina(2);

}