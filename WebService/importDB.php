<?php
	require_once 'funciones_bd.php';
	include 'utils.php';
	
	$json_string = __($_POST['importDB_JSON']);
	$json = json_decode($json_string);
	$customers_id = null;
	$products_id = null;
	$sells_id = null;
	$db_json = null;
	$temp_array = null;
	
	foreach ($json as $sync) {
		$temp = null;
		
		if ($sync->TableName == "seller")
			$temp = seller_json($sync);
		else if ($sync->TableName == "customer")
			$temp = customer_json($sync);
		else if ($sync->TableName == "product")
			$temp = product_json($sync);
		else if ($sync->TableName == "sell")
			$temp = sell_json($sync);
		else if ($sync->TableName == "sell_product")
			$temp = sell_product_json($sync);
		else if ($sync->TableName == "payment")
			$temp = payment_json($sync);
		
		if ($temp != null)
			$temp_array[] = $temp;
	}
	
	if ($temp_array != null)
		$db_json["sync"] = $temp_array;
	
	if ($db_json != null)
		$db_json["status"] = array("status"=>1);
	else
		$db_json["status"] = array("status"=>0);
	
	echo json_encode($db_json);

	function seller_json($seller) {
		$db = new funciones_BD();
		$result = null;
		
		if ($seller->Action == "insert") {
			$seller->IdSeller_Server = $db->addSeller(__($seller->Username), __($seller->Password), __($seller->Name), __($seller->LastName));
			
			$result = array("TableName"=>$seller->TableName, "IdSeller"=>$seller->IdSeller, "IdSeller_Server"=>$seller->IdSeller_Server);
		} else if ($seller->Action == "update") {
			$db->updateSeller($seller->IdSeller_Server, __($seller->Username), __($seller->Password), __($seller->Name), __($seller->LastName));
		} else if ($seller->Action == "delete") {
			$db->deleteElementFromTableById($seller->IdSeller_Server, $seller->TableName);
		}
		
		$db = null;
		return $result;
	}
	
	function customer_json($customer) {
		$db = new funciones_BD();
		$result = null;
		
		if ($customer->Action == "insert") {
			$customer->IdCustomer_Server = $db->addCustomer(__($customer->Name), __($customer->LastName), 
				__($customer->Address), __($customer->City), __($customer->State), $customer->Phone, $customer->Cellphone, 
				__($customer->eMail));
				
			global $customers_id;
			$customers_id["$customer->IdCustomer"] = $customer->IdCustomer_Server;
			
			$result = array("TableName"=>$customer->TableName, "IdCustomer"=>$customer->IdCustomer, 
				"IdCustomer_Server"=>$customer->IdCustomer_Server);
		} else if ($customer->Action == "update") {
			$db->updateCustomer($customer->IdCustomer_Server, __($customer->Name), __($customer->LastName), __($customer->Address), 
				__($customer->City), __($customer->State), $customer->Phone, $customer->Cellphone, __($customer->eMail));
		} else if ($customer->Action == "delete") {
			$db->deleteElementFromTableById($customer->IdCustomer_Server, $customer->TableName);
		}
		
		$db = null;
		return $result;
	}
	
	function product_json($product) {
		$db = new funciones_BD();
		$result = null;
		
		if ($product->Action == "insert") {
			$product->IdProduct_Server = $db->addProduct(__($product->Name), __($product->Brand), __($product->Model), $product->Price, 
				__($product->Description));
				
			global $products_id;
			$products_id["$product->IdProduct"] = $product->IdProduct_Server;
				
			$result = array("TableName"=>$product->TableName, "IdProduct"=>$product->IdProduct, "IdProduct_Server"=>$product->IdProduct_Server);
		} else if ($product->Action == "update") {
			$db->updateProduct($product->IdProduct_Server, __($product->Name), __($product->Brand), __($product->Model), 
				$product->Price, __($product->Description));
		} else if ($product->Action == "delete") {
			$db->deleteElementFromTableById($product->IdProduct_Server, $product->TableName);
		}
		
		$db = null;
		return $result;
	}
	
	function sell_json($sell) {
		$db = new funciones_BD();
		$result = null;
		
		if ($sell->Action == "insert") {
			if ($sell->IdCustomer_Server == 0) {
				global $customers_id;
				$sell->IdSell_Server = $db->addSell($customers_id["$sell->IdCustomer"], $sell->ChargeType, $sell->DayToCharge, $sell->HourToCharge, 
					$sell->TotalPayment, $sell->AgreedPayment, $sell->NextPayment, $sell->State);
			} else {
				$sell->IdSell_Server = $db->addSell($sell->IdCustomer_Server, $sell->ChargeType, $sell->DayToCharge, $sell->HourToCharge, 
					$sell->TotalPayment, $sell->AgreedPayment, $sell->NextPayment, $sell->State);
			}

			global $sells_id;
			$sells_id["$sell->IdSell"] = $sell->IdSell_Server;
				
			$result = array("TableName"=>$sell->TableName, "IdSell"=>$sell->IdSell, "IdSell_Server"=>$sell->IdSell_Server);
		} else if ($sell->Action == "update") {
			$db->updateSell($sell->IdSell_Server, $sell->IdCustomer_Server, $sell->ChargeType, $sell->DayToCharge, $sell->HourToCharge, 
				$sell->TotalPayment, $sell->AgreedPayment, $sell->NextPayment, $sell->State);
		} else if ($sell->Action == "delete") {
			$db->deleteElementFromTableById($sell->IdSell_Server, $sell->TableName);
		}
		
		$db = null;
		return $result;
	}
	
	function sell_product_json($sell_product) {
		$db = new funciones_BD();
		$result = null;
		
		if ($sell_product->Action == "insert") {
			if ($sell_product->IdSell_Server == 0) {
				global $sells_id;
				$idSell = $sells_id["$sell_product->IdSell"];
			} else {
				$idSell = $sell_product->IdSell_Server;
			}
			
			if ($sell_product->IdProduct_Server == 0) {
				global $products_id;
				$idProduct = $products_id["$sell_product->IdProduct"];
			} else {
				$idProduct = $sell_product->IdProduct_Server;
			}
			
			$sell_product->IdSellProduct_Server = $db->addSellProduct($idSell, $idProduct, $sell_product->Amount);
			
			$result = array("TableName"=>$sell_product->TableName, "IdSellProduct"=>$sell_product->IdSellProduct, 
				"IdSellProduct_Server"=>$sell_product->IdSellProduct_Server);
		} else if ($sell_product->Action == "update") {
			$db->updateSellProduct($sell_product->IdSellProduct_Server, $sell_product->IdSell_Server, $sell_product->IdProduct_Server, 
				$sell_product->Amount);
		} else if ($sell_product->Action == "delete") {
			$db->deleteElementFromTableById($sell_product->IdSellProduct_Server, $sell_product->TableName);
		}
		
		$db = null;
		return $result;
	}
	
	function payment_json($payment) {
		$db = new funciones_BD();
		$result = null;
		
		if ($payment->Action == "insert") {
			if ($payment->IdSell_Server == 0) {
				global $sells_id;
				$payment->IdPayment_Server = $db->addPayment($sells_id["$payment->IdSell"], $payment->Payment, $payment->Payment_Date, 
					$payment->Payment_Hour);
			} else {
				$payment->IdPayment_Server = $db->addPayment($payment->IdSell_Server, $payment->Payment, $payment->Payment_Date, 
					$payment->Payment_Hour);
			}
			
			
			$result = array("TableName"=>$payment->TableName, "IdPayment"=>$payment->IdPayment, "IdPayment_Server"=>$payment->IdPayment_Server);
		} else if ($payment->Action == "update") {
			$db->updatePayment($payment->IdPayment_Server, $payment->IdSell_Server, $payment->Payment, $payment->Payment_Date, 
				$payment->Payment_Hour);
		} else if ($payment->Action == "delete") {
			$db->deleteElementFromTableById($payment->IdPayment_Server, $payment->TableName);
		}
		
		$db = null;
		return $result;
	}
?>