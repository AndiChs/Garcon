<?php

require_once '../includes/DbOperations.php';
require_once '../includes/Auth.php';

if($_SERVER['REQUEST_METHOD'] == 'POST'){
	if(isset($_POST['name']) && isset($_POST['restaurantId'])){

		$db = new DbOperations();
		$result = $db->acceptUserRequest($_POST['name'], $_POST['restaurantId']);
		
		if($result == 0){
			$response['error'] = true;
			$response['message'] = "This user has not made an application.";
		} else if($result == 1){
			$response['error'] = false;
			$response['message'] = "The user has been added to the restaurant.";
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
