<?php

require_once '../includes/DbOperations.php';
require_once '../includes/Auth.php';

if($_SERVER['REQUEST_METHOD'] == 'POST'){

		$db = new DbOperations();
		$response['message'] = $db->userUpdateToken($_POST['username'], $_POST['tokenFirebase']);

} else {
	$response['error'] = true;
	$response['message'] = "Invalid Request.";
}

echo json_encode($response);
