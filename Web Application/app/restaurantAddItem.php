<?php

require_once '../includes/DbOperations.php';
require_once '../includes/Auth.php';

if($_SERVER['REQUEST_METHOD'] == 'POST'){
	if(isset($_POST['restaurantId']) && isset($_POST['price']) && isset($_POST['name']) && isset($_POST['description'])){
		
		$db = new DbOperations();
		$result = $db->addRestaurantItem($_POST['restaurantId'], $_POST['price'], $_POST['name'], $_POST['description']);

		$response['error'] = false;
		$response['message'] = $result;
	}
	else{
		$response['error'] = true;
		$response['message'] = "Invalid information sent.";
	}

} else {
	$response['error'] = true;
	$response['message'] = "Invalid Request.";
}

echo json_encode($response);
