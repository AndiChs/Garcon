<?php
require_once '../includes/UserAgent.php';
require_once '../includes/DbOperations.php';
require_once '../includes/Auth.php';

if($_SERVER['REQUEST_METHOD'] == 'POST'){
	if(isset($_POST['restaurantId']) && 
		isset($_POST['userId'])){

		$db = new DbOperations();
		$result = $db->createRestaurantRequest($_POST['userId'], $_POST['restaurantId']);
		if($result == 1){
			$response['error'] = false;
		} else if($result == 0){
			$response['error'] = true;
			$response['message'] = "You have already sent an application.";
		} else if($result == 2){
			$response['error'] = true;
			$response['message'] = "This restaurant does not exist in our database.";
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
