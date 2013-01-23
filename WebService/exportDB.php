<?php
	require_once 'funciones_bd.php';

	$db = new funciones_BD();
	$sellers_json;
	$customers_json;
	$products_json;
	$sells_json;
	$sells_products_json;
	$payments_json;
	$db_json = null;

	$table = $db->getAllElementsFromTableName("seller");
	if ($table != null && mysql_num_rows($table) > 0) {
		while ($row = mysql_fetch_assoc($table)) {
			$sellers_json[] = $row;
		}
		
		$db_json["seller"] = $sellers_json;
	}

	$table = $db->getAllElementsFromTableName("customer");
	if ($table != null && mysql_num_rows($table) > 0) {
		while ($row = mysql_fetch_assoc($table)) {
			$customers_json[] = $row;
		}
		
		$db_json["customer"] = $customers_json;
	}

	$table = $db->getAllElementsFromTableName("product");
	if ($table != null && mysql_num_rows($table) > 0) {
		while ($row = mysql_fetch_assoc($table)) {
			$products_json[] = $row;
		}
		
		$db_json["product"] = $products_json;
	}

	$table = $db->getAllElementsFromTableName("sell");
	if ($table != null && mysql_num_rows($table) > 0) {
		while ($row = mysql_fetch_assoc($table)) {
			$sells_json[] = $row;
		}
		
		$db_json["sell"] = $sells_json;
	}

	$table = $db->getAllElementsFromTableName("sell_product");
	if ($table != null && mysql_num_rows($table) > 0) {
		while ($row = mysql_fetch_assoc($table)) {
			$sells_products_json[] = $row;
		}
		
		$db_json["sell_product"] = $sells_products_json;
	}

	$table = $db->getAllElementsFromTableName("payment");
	if ($table != null && mysql_num_rows($table) > 0) {
		while ($row = mysql_fetch_assoc($table)) {
			$payments_json[] = $row;
		}
		
		$db_json["payment"] = $payments_json;
	}

	if ($db_json != null)
		$db_json["status"] = array("status"=>1);
	else
		$db_json["status"] = array("status"=>0);

	echo json_encode($db_json);
?>