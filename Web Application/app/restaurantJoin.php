<?php

require_once '../includes/DbOperations.php';

if($_SERVER['REQUEST_METHOD'] == 'POST'){
	if(isset($_POST['restaurantId']) && 
		isset($_POST['userId'])){

		$db = new DbOperations();
		if($db->createRestaurantRequest($_POST['userId'], $_POST['restaurantId'])){
			$response['error'] = false;
		} else{
			$response['error'] = true;
			$response['message'] = "You have already sent an application.";
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
