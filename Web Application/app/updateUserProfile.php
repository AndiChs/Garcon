<?php

require_once '../includes/DbOperations.php';

if($_SERVER['REQUEST_METHOD'] == 'POST'){
	if(isset($_POST['userId'])){

		$db = new DbOperations();
		$result = $db->updateUserProfile($_POST['userId'], $_POST['name'], $_POST['newPassword'], $_POST['password'], $_POST['newUsername'], $_POST['username']);
		
		if($result == 0){
			$response['error'] = true;
			$response['message'] = "Invalid password.";
		} else if($result == 1){
			$response['error'] = false;
			$response['message'] = "Changes has been made.";
		} else if($result == -1){
			$response['error'] = true;
			$response['message'] = "It seems this email is already used.";
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
