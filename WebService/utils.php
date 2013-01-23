<?php
function __($Cadena){
	$Cadena = str_replace('á','&aacute;',$Cadena);
	$Cadena = str_replace('é','&eacute;',$Cadena);
	$Cadena = str_replace('í','&iacute;',$Cadena);
	$Cadena = str_replace('ó','&oacute;',$Cadena);
	$Cadena = str_replace('ú','&uacute;',$Cadena);
	$Cadena = str_replace('Á','&Aacute;',$Cadena);
	$Cadena = str_replace('É','&Eacute;',$Cadena);
	$Cadena = str_replace('Í','&Iacute;',$Cadena);
	$Cadena = str_replace('Ó','&Oacute;',$Cadena);
	$Cadena = str_replace('Ú','&Uacute;',$Cadena);
	$Cadena = str_replace('ñ','&ntilde;',$Cadena);
	$Cadena = str_replace('Ñ','&Ntilde;',$Cadena);
	$Cadena = str_replace('ä','&auml;',$Cadena);
	$Cadena = str_replace('ë','&euml;',$Cadena);
	$Cadena = str_replace('ï','&iuml;',$Cadena);
	$Cadena = str_replace('ö','&ouml;',$Cadena);
	$Cadena = str_replace('ü','&uuml;',$Cadena);
	$Cadena = str_replace('Ä','&Auml;',$Cadena);
	$Cadena = str_replace('Ë','&Euml;',$Cadena);
	$Cadena = str_replace('Ï','&Iuml;',$Cadena);
	$Cadena = str_replace('Ö','&Ouml;',$Cadena);
	$Cadena = str_replace('Ü','&Uuml;',$Cadena);
	$Cadena = str_replace('²','&sup2;',$Cadena);
	$Cadena = str_replace('ñ','&ntilde;',$Cadena);
	$Cadena = str_replace('Ñ','&Ntilde;',$Cadena);
	
	return $Cadena;
}
?>