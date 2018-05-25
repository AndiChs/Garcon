<?php

require_once '../includes/DbOperations.php';
require_once '../includes/Auth.php';

if($_SERVER['REQUEST_METHOD'] == 'POST'){
	if(isset($_POST['name']) && isset($_POST['restaurantId'])){

		$db = new DbOperations();
		$result = $db->deleteRestaurantItem($_POST['name'], $_POST['restaurantId']);
		$response['error'] = false;
		$response['message'] = "The item has been deleted.";
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
