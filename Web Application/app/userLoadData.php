<?php

require_once '../includes/DbOperations.php';
require_once '../includes/Auth.php';

if($_SERVER['REQUEST_METHOD'] == 'POST'){
	if(isset($_POST['username'])){

		$db = new DbOperations();
			$user = $db->getUser($_POST['username']);
			$response['error'] = false;
			$response['id'] = $user['id'];
			$response['name'] = $user['name'];
			$response['username'] = $user['username'];
			$response['restaurantId'] = $user['restaurant_id'];
			$response['adminLevel'] = $user['admin_level'];
			$response['restaurantManager'] = $user['restaurant_manager'];
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
