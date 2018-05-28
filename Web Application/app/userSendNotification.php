<?php

require_once '../includes/DbOperations.php';
//require_once '../includes/Auth.php';

if($_SERVER['REQUEST_METHOD'] == 'POST'){
	if(isset($_POST['message']) && isset($_POST['restaurantId'])){
		// operate the date further 
		$db = new DbOperations();
		$token = $db->getRestaurantMembers($_POST['restaurantId'], $_POST['message']);
		$message = array("message" => $_POST['message']);
		$response = $db->sendNotification($token, $message);
		$db->sendNotificationToAllMembers($_POST['restaurantId'], $_POST['message']);
		

	} else{
		$response['error'] = true;
		$response['message'] = "Required fields are missing.";
	}
	
} else{
	$response['error'] = true;
	$response['message'] = "Invalid Request.";
}

echo json_encode($response);