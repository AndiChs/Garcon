<?php

require_once '../includes/DbOperations.php';

if($_SERVER['REQUEST_METHOD'] == 'POST'){
	if(isset($_POST['username']) && 
			isset($_POST['password']) &&
				isset($_POST['name'])
		){
		// operate the date further 
		$db = new DbOperations();

		$result = 	$db->createUser(
					$_POST['username'], 
					$_POST['password'], 
					$_POST['name']);

		if($result == 1){
			$response['error'] = false;
			$response['message'] = "User registred successfully.";
		}
		
		else if($result == 2){
			$response['error'] = true;
			$response['message'] = "Some error occured.";
		}

		else {
			$response['error'] = true;
			$response['message'] = "It seems you are already registered.";
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