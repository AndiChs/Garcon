<?php

if(isset($_POST['token'])){
	$db = new DbOperations();
	if(!$db->checkToken($_POST['token'])){
		$response['error'] = true;
		$response['message'] = "Invalid token.";
		die(json_encode($response));
	}
}
else {
	$response['error'] = true;
	$response['message'] = "Unauthorized access.";
	die(json_encode($response));
}

