<?php

require_once '../includes/DbOperations.php';
require_once '../includes/Auth.php';

if($_SERVER['REQUEST_METHOD'] == 'POST'){
	if(isset($_POST['userId'])){

		$db = new DbOperations();
		$result = $db->getWaiterShifts($_POST['userId']);
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
