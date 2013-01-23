<?php

class funciones_BD {
 
    private $db;
 
    // constructor

    function __construct() {
        require_once 'connectbd.php';
        // connecting to database

        $this->db = new DB_Connect();
    }
	
	public function addSeller($username, $password, $name, $lastname) {
		$idInserted = -1;
		$sql = "INSERT INTO seller(Username, Password, Name, LastName) VALUES('$username', '$password', '$name', '$lastname')";
		
		try {
			$this->db->connect();
			
			mysql_query($sql);
			$idInserted = mysql_insert_id();
			
			$this->db->close();
		} catch (Exception  $e) {
			$idInserted = -1;
		}
		
		return $idInserted;
	}
	
	public function updateSeller($id, $username, $password, $name, $lastname) {
		$sql = "UPDATE seller SET Username='$username', Password='$password', Name='$name', LastName='$lastname' WHERE IdSeller=$id";
		try {
			$this->db->connect();
			mysql_query($sql);
			$this->db->close();
			
			return true;
		} catch (Exception  $e) {
			return false;
		}
	}
	
	public function addCustomer($name, $lastname, $address, $city, $state, $phone, $cellphone, $email) {
		$idInserted = -1;
		$sql = "INSERT INTO customer(Name, LastName, Address, City, State, Phone, Cellphone, eMail) 
			VALUES('$name', '$lastname', '$address', '$city', '$state', '$phone', '$cellphone', '$email')";
		
		try {
			$this->db->connect();
			
			mysql_query($sql);
			$idInserted = mysql_insert_id();
			
			$this->db->close();
		} catch (Exception  $e) {
			$idInserted = -1;
		}
		
		return $idInserted;
	}
	
	public function updateCustomer($id, $name, $lastname, $address, $city, $state, $phone, $cellphone, $email) {
		$sql = "UPDATE customer SET Name='$name', LastName='$lastname', Address='$address', City='$city', State='$state', 
			Phone='$phone', Cellphone='$cellphone', eMail='$email' WHERE IdCustomer=$id";
		try {
			$this->db->connect();
			mysql_query($sql);
			$this->db->close();
			
			return true;
		} catch (Exception  $e) {
			return false;
		}
	}
	
	public function addProduct($name, $brand, $model, $price, $desc) {
		$idInserted = -1;
		$sql = "INSERT INTO product(Name, Brand, Model, Price, Description) VALUES('$name', '$brand', '$model', '$price', '$desc')";
		
		try {
			$this->db->connect();
			
			mysql_query($sql);
			$idInserted = mysql_insert_id();
			
			$this->db->close();
		} catch (Exception  $e) {
			$idInserted = -1;
		}
		
		return $idInserted;
	}
	
	public function updateProduct($id, $name, $brand, $model, $price, $desc) {
		$sql = "UPDATE product SET Name='$name', Brand='$brand', Model='$model', Price='$price', Description='$desc' WHERE IdProduct=$id";
		try {
			$this->db->connect();
			mysql_query($sql);
			$this->db->close();
			
			return true;
		} catch (Exception  $e) {
			return false;
		}
	}
	
	public function addSell($idCustomer, $chargetype, $daytocharge, $hourtocharge, $totalpayment, $agreedpayment, $nextpayment, $state) {
		$idInserted = -1;
		$sql = "INSERT INTO sell(IdCustomer, ChargeType, DayToCharge, HourToCharge, TotalPayment, AgreedPayment, NextPayment, State) 
			VALUES('$idCustomer', '$chargetype', '$daytocharge', '$hourtocharge', '$totalpayment', '$agreedpayment', '$nextpayment', '$state')";
		
		try {
			$this->db->connect();
			
			mysql_query($sql);
			$idInserted = mysql_insert_id();
			
			$this->db->close();
		} catch (Exception  $e) {
			$idInserted = -1;
		}
		
		return $idInserted;
	}
	
	public function updateSell($id, $idCustomer, $chargetype, $daytocharge, $hourtocharge, $totalpayment, $agreedpayment, $nextpayment, $state) {
		$sql = "UPDATE sell SET IdCustomer='$idCustomer', ChargeType='$chargetype', DayToCharge='$daytocharge', HourToCharge='$hourtocharge', 
			TotalPayment='$totalpayment', AgreedPayment='$agreedpayment', NextPayment='$nextpayment', State='$state' WHERE IdSell='$id'";
		try {
			$this->db->connect();
			mysql_query($sql);
			$this->db->close();
			
			return true;
		} catch (Exception  $e) {
			return false;
		}
	}
	
	public function addSellProduct($idSell, $idProduct, $amount) {
		$idInserted = -1;
		$sql = "INSERT INTO sell_product(IdSell, IdProduct, Amount) VALUES('$idSell', '$idProduct', '$amount')";
		
		try {
			$this->db->connect();
			
			mysql_query($sql);
			$idInserted = mysql_insert_id();
			
			$this->db->close();
		} catch (Exception  $e) {
			$idInserted = -1;
		}
		
		return $idInserted;
	}
	
	public function updateSellProduct($id, $idSell, $idProduct, $amount) {
		$sql = "UPDATE sell_product SET IdSell='$idSell', IdProduct='$idProduct', Amount='$amount' WHERE IdSellProduct='$id'";
		try {
			$this->db->connect();
			mysql_query($sql);
			$this->db->close();
			
			return true;
		} catch (Exception  $e) {
			return false;
		}
	}
	
	public function addPayment($idSell, $payment, $payment_date, $payment_hour) {
		$idInserted = -1;
		$sql = "INSERT INTO payment(IdSell, Payment, Payment_Date, Payment_Hour) VALUES('$idSell', '$payment', '$payment_date', '$payment_hour')";
		
		try {
			$this->db->connect();
			
			mysql_query($sql);
			$idInserted = mysql_insert_id();
			
			$this->db->close();
		} catch (Exception  $e) {
			$idInserted = -1;
		}
		
		return $idInserted;
	}
	
	public function updatePayment($id, $idSell, $payment, $payment_date, $payment_hour) {
		$sql = "UPDATE payment SET IdSell='$idSell', Payment='$payment', Payment_Date='$payment_date', Payment_Hour='$payment_hour' 
			WHERE IdPayment='$id'";
		try {
			$this->db->connect();
			mysql_query($sql);
			$this->db->close();
			
			return true;
		} catch (Exception  $e) {
			return false;
		}
	}
	
	public function deleteElementFromTableById($id, $table) {
		$id_table = "Id" . ucfirst($table);
		if ($table == "sell_product")
			$id_table = "IdSellProduct";
		$sql = "DELETE FROM $table WHERE $id_table='$id'";
		
		try {
			$this->db->connect();
			mysql_query($sql);
			$this->db->close();
			
			return true;
		} catch (Exception  $e) {
			return false;
		}
	}
	
	public function getAllElementsFromTableName($table) {
		$elements;
		
		if ($table == "customer") {
			$sql = "SELECT IdCustomer, Name, LastName, Address, City, State, IF(Phone IS NOT null, Phone, '') AS Phone, 
				IF(Cellphone IS NOT null, Cellphone, '') AS Cellphone, IF(eMail IS NOT null, eMail, '') AS eMail FROM customer";
		} else if ($table == "product") {
			$sql = "SELECT IdProduct, Name, IF(Brand IS NOT null, Brand, '') AS Brand, IF(Model IS NOT null, Model, '') AS Model, 
				Price, IF(Description IS NOT null, Description, '') AS Description FROM product;";
		} else {
			$sql = "SELECT * FROM $table";
		}
		
		try {
			$this->db->connect();
			
			$elements = mysql_query($sql);
			
			$this->db->close();
		} catch (Exception $e) {
			$elements = null;
		}
		
		return $elements;
	}
}
 
?>
