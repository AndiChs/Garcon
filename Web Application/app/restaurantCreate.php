<?php

require_once '../includes/DbOperations.php';

if($_SERVER['REQUEST_METHOD'] == 'POST'){
	if(isset($_POST['cvr']) && 
		isset($_POST['address']) && 
			isset($_POST['name'])){

		$db = new DbOperations();
		if($db->createRestaurant($_POST['cvr'], $_POST['name'], $_POST['address'])){
			$restaurant = $db->getRestaurantByCvr($_POST['cvr']);
			$response['error'] = false;
			$response['restaurantId'] = $restaurant['id'];
		} else{
			$response['error'] = true;
			$response['message'] = "It seems this CVR number is already used.";
		}
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
