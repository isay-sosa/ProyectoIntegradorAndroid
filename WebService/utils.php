<?php
function __($Cadena){
	$Cadena = str_replace('�','&aacute;',$Cadena);
	$Cadena = str_replace('�','&eacute;',$Cadena);
	$Cadena = str_replace('�','&iacute;',$Cadena);
	$Cadena = str_replace('�','&oacute;',$Cadena);
	$Cadena = str_replace('�','&uacute;',$Cadena);
	$Cadena = str_replace('�','&Aacute;',$Cadena);
	$Cadena = str_replace('�','&Eacute;',$Cadena);
	$Cadena = str_replace('�','&Iacute;',$Cadena);
	$Cadena = str_replace('�','&Oacute;',$Cadena);
	$Cadena = str_replace('�','&Uacute;',$Cadena);
	$Cadena = str_replace('�','&ntilde;',$Cadena);
	$Cadena = str_replace('�','&Ntilde;',$Cadena);
	$Cadena = str_replace('�','&auml;',$Cadena);
	$Cadena = str_replace('�','&euml;',$Cadena);
	$Cadena = str_replace('�','&iuml;',$Cadena);
	$Cadena = str_replace('�','&ouml;',$Cadena);
	$Cadena = str_replace('�','&uuml;',$Cadena);
	$Cadena = str_replace('�','&Auml;',$Cadena);
	$Cadena = str_replace('�','&Euml;',$Cadena);
	$Cadena = str_replace('�','&Iuml;',$Cadena);
	$Cadena = str_replace('�','&Ouml;',$Cadena);
	$Cadena = str_replace('�','&Uuml;',$Cadena);
	$Cadena = str_replace('�','&sup2;',$Cadena);
	$Cadena = str_replace('�','&ntilde;',$Cadena);
	$Cadena = str_replace('�','&Ntilde;',$Cadena);
	
	return $Cadena;
}
?>