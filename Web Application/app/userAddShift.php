<?php

require_once '../includes/DbOperations.php';
require_once '../includes/Auth.php';

if($_SERVER['REQUEST_METHOD'] == 'POST'){
	if(isset($_POST['username']) && 
			isset($_POST['workingHours']) &&
				isset($_POST['dateStart']) && 
					isset($_POST['restaurantId'])
		){
		// operate the date further 
		$db = new DbOperations();
		$user = $db->getUser($_POST['username']);
		if($_POST['restaurantId'] != $user['restaurant_id']){
			$response['error'] = true;
			$response['message'] = "This user is not a member of the restaurant.";
		} else{
			$result = 	$db->addShift(
					$user['id'], 
					$_POST['dateStart'], 
					$_POST['workingHours']);

			if($result == 1){
				$response['error'] = false;
				$response['message'] = "Shift added successfully.";
			}
			
			else if($result == 0){
				$response['error'] = true;
				$response['message'] = "Some error occured.";
			}
		}
		

	} else{
		$response['error'] = true;
		$response['message'] = "Required fields are missing.";
	}
	
} else{
	$response['error'] = true;
	$response['message'] = "Invalid Request.";
}

echo json_encode($response);