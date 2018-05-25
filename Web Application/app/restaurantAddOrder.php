<?php

require_once '../includes/DbOperations.php';
require_once '../includes/Auth.php';

if($_SERVER['REQUEST_METHOD'] == 'POST'){
	if(isset($_POST['restaurantId']) && isset($_POST['orderPrice']) && isset($_POST['orderDescription']) && isset($_POST['userId']) && isset($_POST['tableId'])){

		$db = new DbOperations();
		$result = $db->addRestaurantOrder($_POST['userId'], $_POST['orderPrice'], $_POST['orderDescription'], $_POST['tableId'], $_POST['restaurantId']);

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
