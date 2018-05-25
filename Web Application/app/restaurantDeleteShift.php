<?php

require_once '../includes/DbOperations.php';
require_once '../includes/Auth.php';

if($_SERVER['REQUEST_METHOD'] == 'POST'){
	if(isset($_POST['date']) && isset($_POST['working_hours'])){

		$db = new DbOperations();
		$result = $db->deleteRestaurantShift($_POST['date'], $_POST['working_hours']);
		$response['error'] = false;
		$response['message'] = "The shift has been deleted.";
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
