<?php

require_once '../includes/DbOperations.php';

if($_SERVER['REQUEST_METHOD'] == 'POST'){
	if(isset($_POST['restaurantId'])){

		$db = new DbOperations();
		$result = $db->getRestaurantItems($_POST['restaurantId']);
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
