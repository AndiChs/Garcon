<?php

require_once '../includes/DbOperations.php';

if($_SERVER['REQUEST_METHOD'] == 'POST'){
	if(isset($_POST['username']) && isset($_POST['password'])){

		$db = new DbOperations();
		if($db->checkCredentials($_POST['username'], $_POST['password'])){
			$user = $db->getUser($_POST['username']);
			$response['error'] = false;
			$response['id'] = $user['id'];
			$response['name'] = $user['name'];
			$response['username'] = $user['username'];
			$response['restaurantId'] = $user['restaurant_id'];
			$response['adminLevel'] = $user['admin_level'];
			$response['restaurantManager'] = $user['restaurant_manager'];
			$response['token'] = $token = bin2hex(openssl_random_pseudo_bytes(72));
			$db->updateUserAuthToken($token, $user['username']);
		} else{
			$response['error'] = true;
			$response['message'] = "Invalid username or password.";
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
